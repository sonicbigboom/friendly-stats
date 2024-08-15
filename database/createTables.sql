-- Create Stats DB
USE [master];
CREATE DATABASE [stats];
GO

USE [stats];
GO

-- Person
CREATE TABLE [Person] (
	ID INT IDENTITY(1,1) NOT NULL,
	Email VARCHAR(255) NOT NULL,
	Username VARCHAR(255) NOT NULL,
	FirstName VARCHAR(255) NULL,
	LastName VARCHAR(255) NULL,
	Nickname VARCHAR(255) NULL, 
	PRIMARY KEY (ID),
	CONSTRAINT AK_Person_Email UNIQUE(Email),
	CONSTRAINT AK_Person_Username UNIQUE(Username)
);
GO

-- Club
-- StoredCash represents the bank for the club.
CREATE TABLE [Club] (
	ID INT IDENTITY(1,1) NOT NULL,
	Name VARCHAR(255) NOT NULL,
	OwnerPersonID INT NOT NULL,
	StoredCash INT DEFAULT 0 NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (OwnerPersonID) REFERENCES [Person](ID)
);
GO

-- Membership
-- Person to Club many-to-many relationship
CREATE TABLE [Membership] (
	PersonID INT NOT NULL,
	ClubID INT NOT NULL,
	CashBalance INT DEFAULT 0 NOT NULL,
	IsCashAdmin BIT DEFAULT 0 NOT NULL,
	IsGameAdmin BIT DEFAULT 0 NOT NULL,
	PRIMARY KEY (PersonID, ClubID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID)
);
GO

-- GameType
-- Represents a single game like: Poker, Mahjong, or Fantasy Football.
CREATE TABLE [GameType] (
	ID INT IDENTITY(1,1) NOT NULL,
	Name VARCHAR(255) NOT NULL,
	IsZeroSum BIT NOT NULL,
	PRIMARY KEY (ID)
);
GO

-- Score
CREATE TABLE [Score] (
	PersonID INT NOT NULL,
	ClubID INT NOT NULL,
	GameTypeID INT NOT NULL,
	Total INT NOT NULL,
	PRIMARY KEY (PersonID, ClubID, GameTypeID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID),
	FOREIGN KEY (GameTypeID) REFERENCES [GameType](ID)
);
GO

-- Game
-- A single game like: one night of Poker, or one match of Mahjong. 
-- If EndDate is NULL, the game is still on-going.
CREATE TABLE [Game] (
	ID INT IDENTITY(1,1) NOT NULL,
	ClubID INT NOT NULL,
	GameTypeID INT NOT NULL,
	ForCash BIT DEFAULT 0 NOT NULL,
	AccumulateScore BIT DEFAULT 0 NOT NULL,
	StartDate DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	EndDate DATETIME NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID),
	FOREIGN KEY (GameTypeID) REFERENCES [GameType](ID)
);
GO

-- GameRecord
-- A single event in a game like: a player buys in for Poker, or a Mahjong round has ended and points are distributed.
CREATE TABLE [GameRecord] (
	ID INT IDENTITY(1,1) NOT NULL,
	GameID INT NOT NULL,
	PersonID INT NOT NULL,
	ScoreChange INT NOT NULL,
	CreatedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CreatedByPersonID INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (GameID) REFERENCES [Game](ID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	FOREIGN KEY (CreatedByPersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ModifiedByPersonID) REFERENCES [Person](ID)
);
GO

-- CashTransaction
-- A cash transaction between players.
CREATE TABLE [CashTransaction] (
	ID INT IDENTITY(1,1) NOT NULL,
	OriginPersonID INT NOT NULL,
	TargetPersonID INT NOT NULL,
	ClubID INT NOT NULL,
	Amount INT NOT NULL,
	CreatedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CreatedByPersonID INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (OriginPersonID) REFERENCES [Person](ID),
	FOREIGN KEY (TargetPersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID),
	FOREIGN KEY (CreatedByPersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ModifiedByPersonID) REFERENCES [Person](ID)
);
GO

-- BankCashTransaction
-- A player to bank transaction.
CREATE TABLE [BankCashTransaction] (
	ID INT IDENTITY(1,1) NOT NULL,
	PersonID INT NOT NULL,
	ClubID INT NOT NULL,
	Deposit INT NOT NULL,
	CreatedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CreatedByPersonID INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID),
	FOREIGN KEY (CreatedByPersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ModifiedByPersonID) REFERENCES [Person](ID)
);
GO

-- ========================== --
-- ===== Authentication ===== --
-- ========================== -- 

-- Username + Password
CREATE TABLE [AuthLocalPassword] (
	PersonID INT NOT NULL,
	Password VARCHAR(255) NOT NULL,
	PRIMARY KEY (PersonID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID)
);
GO

CREATE VIEW [AuthLocal] AS
	SELECT P.Email Email, P.Username Username, A.Password Password, A.PersonID PersonID
	FROM [Person] P INNER JOIN [AuthLocalPassword] A ON P.ID = A.PersonID;
GO

-- Google oAuth
CREATE TABLE [AuthGoogle] (
	AuthID VARCHAR(255) NOT NULL, 
	PersonID INT NOT NULL,
	PRIMARY KEY (AuthID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	CONSTRAINT AK_AuthGoogle_PersonID UNIQUE(PersonID)
);
GO

-- ========================== --
-- ======== Triggers ======== --
-- ========================== -- 

-- Cash Transaction Trigger
-- Updates user and bank balances.
CREATE TRIGGER [CASH_TRANSACTION_TRIGGER]
	ON [CashTransaction]
	AFTER INSERT, UPDATE
AS
	-- If this is an update, undo the previous transaction first.
	IF EXISTS ( SELECT 0 FROM [Deleted] ) BEGIN
		-- Undo giving away money.
		UPDATE M
		SET M.CashBalance = M.CashBalance + D.Amount
		FROM [Membership] M INNER JOIN [Deleted] D ON M.ClubID = D.ClubID AND M.PersonID = D.OriginPersonID;

		-- Undo receiving money.
		UPDATE M
		SET M.CashBalance = M.CashBalance - D.Amount
		FROM [Membership] M INNER JOIN [Deleted] D ON M.ClubID = D.ClubID AND M.PersonID = D.TargetPersonID;
	END

	-- Give away money.
	UPDATE M
	SET M.CashBalance = M.CashBalance - I.Amount
	FROM [Membership] M INNER JOIN [Inserted] I ON M.ClubID = I.ClubID AND M.PersonID = I.OriginPersonID;

	-- Receive money.
	UPDATE M
	SET M.CashBalance = M.CashBalance + I.Amount
	FROM [Membership] M INNER JOIN [Inserted] I ON M.ClubID = I.ClubID AND M.PersonID = I.TargetPersonID;
GO

-- Bank Transaction Trigger
-- Updates user and bank balances.
CREATE TRIGGER [BANK_CASH_TRANSACTION_TRIGGER]
	ON [BankCashTransaction]
	AFTER INSERT, UPDATE
AS
	-- If this is an update, undo the previous transaction first.
	IF EXISTS ( SELECT 0 FROM [Deleted] ) BEGIN

		-- Undo giving away money.
		UPDATE M
		SET M.CashBalance = M.CashBalance + D.Deposit
		FROM [Membership] M INNER JOIN [Deleted] D ON M.ClubID = D.ClubID AND M.PersonID = D.PersonID;

		-- Undo money withdrawl.
		UPDATE M
		SET M.StoredCash = M.StoredCash + D.Deposit
		FROM [Club] M INNER JOIN [Inserted] D ON M.ID = D.ClubID;
	END

	-- Give away money.
	UPDATE M
	SET M.CashBalance = M.CashBalance - I.Deposit
	FROM [Membership] M INNER JOIN [Inserted] I ON M.ClubID = I.ClubID AND M.PersonID = I.PersonID;

	-- Withdraw money.
	UPDATE M
	SET M.StoredCash = M.StoredCash - I.Deposit
	FROM [Club] M INNER JOIN [Inserted] I ON M.ID = I.ClubID;
GO

-- ========================== --
-- ======== Audit Log ======= --
-- ========================== -- 

-- Game Record Audit
CREATE TABLE [GameRecordAudit] (
	ID INT IDENTITY(1,1) NOT NULL,
	GameRecordID INT NOT NULL,
	GameID INT NOT NULL,
	PersonID INT NOT NULL,
	ScoreChange INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (GameRecordID) REFERENCES [GameRecord](ID),
	FOREIGN KEY (GameID) REFERENCES [Game](ID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ModifiedByPersonID) REFERENCES [Person](ID)
);
GO

-- Game Record Audit Trigger
CREATE TRIGGER [GAME_RECORD_AUDIT_TRIGGER]
	ON [GameRecord]
	AFTER INSERT, UPDATE
AS
	INSERT INTO [GameRecordAudit] (GameRecordID, GameID, PersonID, ScoreChange, ModifiedTime, ModifiedByPersonID)
	SELECT I.ID, I.GameID, I.PersonID, I.ScoreChange, I.ModifiedTime, I.ModifiedByPersonID FROM [Inserted] I;
GO

-- Cash Transaction Audit
CREATE TABLE [CashTransactionAudit] (
	ID INT IDENTITY(1,1) NOT NULL,
	CashTransactionID INT NOT NULL,
	OriginPersonID INT NOT NULL,
	TargetPersonID INT NULL,
	ClubID INT NOT NULL,
	Amount INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (CashTransactionID) REFERENCES [CashTransaction](ID),
	FOREIGN KEY (OriginPersonID) REFERENCES [Person](ID),
	FOREIGN KEY (TargetPersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID),
	FOREIGN KEY (ModifiedByPersonID) REFERENCES [Person](ID)
);
GO

-- Cash Transaction Audit Trigger
CREATE TRIGGER [CASH_TRANSACTION_AUDIT_TRIGGER]
	ON [CashTransaction]
	AFTER INSERT, UPDATE
AS
	INSERT INTO [CashTransactionAudit] (CashTransactionID, OriginPersonID, TargetPersonID, ClubID, Amount, ModifiedTime, ModifiedByPersonID)
	SELECT I.ID, I.OriginPersonID, I.TargetPersonID, I.ClubID, I.Amount, I.ModifiedTime, I.ModifiedByPersonID FROM [Inserted] I;
GO

-- Bank Cash Transaction Audit
CREATE TABLE [BankCashTransactionAudit] (
	ID INT IDENTITY(1,1) NOT NULL,
	CashTransactionID INT NOT NULL,
	PersonID INT NOT NULL,
	ClubID INT NOT NULL,
	Deposit INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (CashTransactionID) REFERENCES [CashTransaction](ID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID),
	FOREIGN KEY (ModifiedByPersonID) REFERENCES [Person](ID)
);
GO

-- Bank Cash Transaction Audit Trigger
CREATE TRIGGER [BANK_CASH_TRANSACTION_AUDIT_TRIGGER]
	ON [BankCashTransaction]
	AFTER INSERT, UPDATE
AS
	INSERT INTO [BankCashTransactionAudit] (CashTransactionID, PersonID, ClubID, Deposit, ModifiedTime, ModifiedByPersonID)
	SELECT I.ID, I.PersonID, I.ClubID, I.Deposit, I.ModifiedTime, I.ModifiedByPersonID FROM [Inserted] I;
GO
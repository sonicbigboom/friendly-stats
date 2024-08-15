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
	ForCash BIT NOT NULL,
	Total INT NOT NULL,
	PRIMARY KEY (PersonID, ClubID, GameTypeID, ForCash),
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
	NetScoreChange INT DEFAULT 0 NOT NULL,
	StartDate DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	EndDate DATETIME NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (ClubID) REFERENCES [Club](ID),
	FOREIGN KEY (GameTypeID) REFERENCES [GameType](ID)
);
GO

-- GamePlayer
CREATE TABLE [GamePlayer] (
	GameID INT NOT NULL,
	PersonID INT NOT NULL,
	PRIMARY KEY (GameID, PersonID),
	FOREIGN KEY (PersonID) REFERENCES [Person](ID),
	FOREIGN KEY (GameID) REFERENCES [Game](ID)
)

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
	SourcePersonID INT NOT NULL,
	TargetPersonID INT NOT NULL,
	ClubID INT NOT NULL,
	Amount INT NOT NULL,
	CreatedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CreatedByPersonID INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (SourcePersonID) REFERENCES [Person](ID),
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

-- Game Score Trigger
CREATE TRIGGER [GAME_PLAYER_SCORE_TRIGGER]
	ON [GamePlayer]
	AFTER INSERT
AS
	INSERT INTO [Score] (PersonID, ClubID, GameTypeID, ForCash, Total)
	SELECT I.PersonID, G.ClubID, G.GameTypeID, G.ForCash, 0
	FROM [Inserted] I INNER JOIN [Game] G ON I.GameID = G.ID
	WHERE G.AccumulateScore = 1;
GO

-- Game Record Trigger
CREATE TRIGGER [GAME_RECORD_TRIGGER]
	ON [GameRecord]
	AFTER INSERT, UPDATE
AS
	-- If this is an update, undo the previous transaction first.
	IF EXISTS ( SELECT 0 FROM [Deleted] ) BEGIN
		-- Undo player cash balance change.
		UPDATE M
		SET M.CashBalance = M.CashBalance - R.Total
		FROM [Membership] M INNER JOIN 
		(
			SELECT G.ClubID ClubID, D.PersonID PersonID, SUM(D.ScoreChange) Total
			FROM [Deleted] D INNER JOIN [Game] G ON D.GameID = G.ID
			WHERE G.ForCash = 1
			GROUP BY G.ClubID, D.PersonID
		) R
		ON M.ClubID = R.ClubID AND M.PersonID = R.PersonID;

		-- Undo game net score change.
		UPDATE G
		SET G.NetScoreChange = G.NetScoreChange - R.Total
		FROM [Game] G INNER JOIN 
		(
			SELECT GameID, SUM(ScoreChange) Total
			FROM [Deleted]
			GROUP BY GameID
		) R
		ON G.ID = R.GameID;

		-- Undo player score update.
		UPDATE S
		SET S.Total = S.Total - R.Total
		FROM [Score] S INNER JOIN 
		(
			SELECT I.PersonID PersonID, G.ClubID ClubID, G.GameTypeID GameTypeID, G.ForCash ForCash, SUM(I.ScoreChange) Total
			FROM [Inserted] I INNER JOIN [Game] G ON I.GameID = G.ID
			WHERE G.AccumulateScore = 1
			GROUP BY I.PersonID, G.ClubID, G.GameTypeID, G.ForCash
		) R
		ON S.PersonID = R.PersonID AND S.ClubID = R.ClubID AND S.GameTypeID = R.GameTypeID AND S.ForCash = R.ForCash;
	END

	-- Update player cash balance.
	UPDATE M
	SET M.CashBalance = M.CashBalance + R.Total
	FROM [Membership] M INNER JOIN 
	(
		SELECT G.ClubID ClubID, I.PersonID PersonID, SUM(I.ScoreChange) Total
		FROM [Inserted] I INNER JOIN [Game] G ON I.GameID = G.ID
		WHERE G.ForCash = 1
		GROUP BY G.ClubID, I.PersonID
	) R
	ON M.ClubID = R.ClubID AND M.PersonID = R.PersonID;

	-- Update game net score change.
	UPDATE G
	SET G.NetScoreChange = G.NetScoreChange + R.Total
	FROM [Game] G INNER JOIN 
	(
		SELECT GameID, SUM(ScoreChange) Total
		FROM [Inserted]
		GROUP BY GameID
	) R
	ON G.ID = R.GameID;

	-- Update player score.
	UPDATE S
	SET S.Total = S.Total + R.Total
	FROM [Score] S INNER JOIN 
	(
		SELECT I.PersonID PersonID, G.ClubID ClubID, G.GameTypeID GameTypeID, G.ForCash ForCash, SUM(I.ScoreChange) Total
		FROM [Inserted] I INNER JOIN [Game] G ON I.GameID = G.ID
		WHERE G.AccumulateScore = 1
		GROUP BY I.PersonID, G.ClubID, G.GameTypeID, G.ForCash
	) R
	ON S.PersonID = R.PersonID AND S.ClubID = R.ClubID AND S.GameTypeID = R.GameTypeID AND S.ForCash = R.ForCash;
GO

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
		SET M.CashBalance = M.CashBalance + D.Total
		FROM [Membership] M INNER JOIN
		(
			SELECT ClubID, SourcePersonID, SUM(Amount) Total
			FROM [Deleted]
			GROUP BY ClubID, SourcePersonID
		) D  
		ON M.ClubID = D.ClubID AND M.PersonID = D.SourcePersonID;

		-- Undo receiving money.
		UPDATE M
		SET M.CashBalance = M.CashBalance - D.Total
		FROM [Membership] M INNER JOIN
		(
			SELECT ClubID, TargetPersonID, SUM(Amount) Total
			FROM [Deleted]
			GROUP BY ClubID, TargetPersonID
		) D  
		ON M.ClubID = D.ClubID AND M.PersonID = D.TargetPersonID;
	END

	-- Give away money.
	UPDATE M
	SET M.CashBalance = M.CashBalance - I.Total
	FROM [Membership] M INNER JOIN
	(
		SELECT ClubID, SourcePersonID, SUM(Amount) Total
		FROM [Inserted]
		GROUP BY ClubID, SourcePersonID
	) I 
	ON M.ClubID = I.ClubID AND M.PersonID = I.SourcePersonID;

	-- Receive money.
	UPDATE M
	SET M.CashBalance = M.CashBalance + I.Total
	FROM [Membership] M INNER JOIN
	(
		SELECT ClubID, TargetPersonID, SUM(Amount) Total
		FROM [Inserted]
		GROUP BY ClubID, TargetPersonID
	) I 
	ON M.ClubID = I.ClubID AND M.PersonID = I.TargetPersonID;
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
		SET M.CashBalance = M.CashBalance - D.Total
		FROM [Membership] M INNER JOIN 
		(
			SELECT ClubID, PersonID, SUM(Deposit) Total
			FROM [Deleted]
			GROUP BY ClubID, PersonID
		) D 
		ON M.ClubID = D.ClubID AND M.PersonID = D.PersonID;

		-- Undo money withdrawl.
		UPDATE C
		SET C.StoredCash = C.StoredCash - D.Total
		FROM [Club] C INNER JOIN 
		(
			SELECT ClubID, SUM(Deposit) Total
			FROM [Deleted]
			GROUP BY ClubID
		) D
		ON C.ID = D.ClubID;
	END

	-- Give away money.
	UPDATE M
	SET M.CashBalance = M.CashBalance + I.Total
	FROM [Membership] M INNER JOIN 
	(
		SELECT ClubID, PersonID, SUM(Deposit) Total
		FROM [Inserted]
		GROUP BY ClubID, PersonID
	) I 
	ON M.ClubID = I.ClubID AND M.PersonID = I.PersonID;

	-- Withdraw money.
	UPDATE C
	SET C.StoredCash = C.StoredCash + I.Total
	FROM [Club] C INNER JOIN 
	(
		SELECT ClubID, SUM(Deposit) Total
		FROM [Inserted]
		GROUP BY ClubID
	) I
	ON C.ID = I.ClubID;
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
	SourcePersonID INT NOT NULL,
	TargetPersonID INT NULL,
	ClubID INT NOT NULL,
	Amount INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (CashTransactionID) REFERENCES [CashTransaction](ID),
	FOREIGN KEY (SourcePersonID) REFERENCES [Person](ID),
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
	INSERT INTO [CashTransactionAudit] (CashTransactionID, SourcePersonID, TargetPersonID, ClubID, Amount, ModifiedTime, ModifiedByPersonID)
	SELECT I.ID, I.SourcePersonID, I.TargetPersonID, I.ClubID, I.Amount, I.ModifiedTime, I.ModifiedByPersonID FROM [Inserted] I;
GO

-- Bank Cash Transaction Audit
CREATE TABLE [BankCashTransactionAudit] (
	ID INT IDENTITY(1,1) NOT NULL,
	BankCashTransactionID INT NOT NULL,
	PersonID INT NOT NULL,
	ClubID INT NOT NULL,
	Deposit INT NOT NULL,
	ModifiedTime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
	ModifiedByPersonID INT NOT NULL,
	PRIMARY KEY (ID),
	FOREIGN KEY (BankCashTransactionID) REFERENCES [BankCashTransaction](ID),
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
	INSERT INTO [BankCashTransactionAudit] (BankCashTransactionID, PersonID, ClubID, Deposit, ModifiedTime, ModifiedByPersonID)
	SELECT I.ID, I.PersonID, I.ClubID, I.Deposit, I.ModifiedTime, I.ModifiedByPersonID FROM [Inserted] I;
GO
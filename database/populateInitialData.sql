SET IDENTITY_INSERT [Person] ON
INSERT INTO [Person] ([ID], [Email], [Username], [FirstName], [LastName], [Nickname], [IsDisabled], [IsDeleted]) VALUES
	('1', '', 'System', NULL, NULL, NULL, '0', '0');
SET IDENTITY_INSERT [Person] OFF
GO

SET IDENTITY_INSERT [Season] ON
INSERT INTO [Season] ([ID], [Name], [ClubID], [IsDeleted]) VALUES
	('1', 'Season 0', NULL, '0');
SET IDENTITY_INSERT [Season] OFF
GO

SET IDENTITY_INSERT [Club] ON
INSERT INTO [Club] ([ID], [Name], [OwnerPersonID], [StoredCash], [CurrentSeasonID], [IsDeleted]) VALUES
	('1', 'System Club', '1', '0', '1', '0');
SET IDENTITY_INSERT [Club] OFF
GO

INSERT INTO [PersonRole] ([Name]) VALUES
	('Player'),
	('Game Admin'),
	('Cash Admin'),
	('Co-Owner');
GO

SET IDENTITY_INSERT [GameScoringType] ON
INSERT INTO [GameScoringType] ([ID], [Name], [ScoringCode], [ClubID], [IsDeleted]) VALUES
	('1', 'ZeroSum', NULL, NULL, '0'),
	('2', 'Winner', NULL, NULL, '0'),
	('3', 'Bet', NULL, NULL, '0'),
	('4', 'Tournament', NULL, NULL, '0');
SET IDENTITY_INSERT [GameScoringType] OFF
GO

SET IDENTITY_INSERT [GameType] ON
INSERT INTO [GameType] ([ID], [Name], [GameScoringTypeID], [ClubID], [IsDeleted]) VALUES
	('1', 'Poker', '1', NULL, '0'),
	('2', 'Mahjong', '1', NULL, '0'),
	('3', 'Gin Rummy', '2', NULL, '0'),
	('4', 'Fantasy Football', '4', NULL, '0');
SET IDENTITY_INSERT [GameType] OFF
GO

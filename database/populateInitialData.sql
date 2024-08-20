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

SET IDENTITY_INSERT [Season] ON
INSERT INTO [Season] ([ID], [Name], [ClubID]) VALUES
	('1', 'Season 0', NULL);
SET IDENTITY_INSERT [Season] OFF
GO

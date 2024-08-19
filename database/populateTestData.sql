USE [stats];
GO

SET IDENTITY_INSERT [Person] ON
INSERT INTO [Person] ([ID], [Email], [Username], [FirstName], [LastName], [Nickname]) VALUES
	('1', 'jane@gmail.com', 'janedoe', 'Jane ', 'Doe', NULL),
	('2', 'wizardgwyn@gmail.com', 'Gwynndolyn', 'Gwydristopher', 'Bagwen', 'Gwynn'),
	('3', 'horuso@yahoo.com', 'horuso1', 'Horus', 'Owens', NULL),
	('4', 'jesuscolumbo14@gmail.com', 'ColumnJ', 'Jesus', 'Columbo', 'Column'),
	('5', 'anubismiller@gmail.com', 'AnubyMil', 'Anubis', 'Miller', NULL),
	('6', 'sungod@outlook.com', 'xXsungodraXx', 'Ra', 'Rodriguez', NULL),
	('7', 'irisgarc@outlook.com', 'iris.garcia7', 'Iris', 'Garcia', NULL),
	('8', 'hathorm@yahoo.com', 'hathormurphy', 'Hathor', 'Murphy', NULL),
	('9', 'jb1301@gmail.com', 'memehub', 'Jack ', 'Brown', NULL),
	('10', 'swilliams6@outlook.com', 'stevewwills', 'Steve', 'Williams', 'stevie'),
	('11', 'lili@yahoo.com', 'sillycattos', 'Li', 'Li', NULL),
	('12', 'lwilson99@yahoo.com', 'winter.lew13', 'Lewis', 'Wilson', 'Rod'),
	('13', 'jessiebrown10@hotmail.com', 'Jessie_brownn', 'Jessica ', 'Brown', 'Jessie'),
	('14', 'spatel@gmail.com', 'soph.patel202', 'Sophia', 'Patel', 'Sophia'),
	('15', 'charbrooks@yahoo.com', 'charlottebrooks', 'Charlotte', 'Brooks', 'Charlie'),
	('16', 'emolucy@gmail.com', 'grayworld', 'Lucy', 'Gray', NULL),
	('17', 'oliviaprice44@outlook.com', 'Olivia_price', 'Olivia', 'Price', 'Liv'),
	('18', 'livysmiths@gmail.com', 'Livylifelove', 'Olivia', 'Smith', 'Livy'),
	('19', 'sophierogers@yahoo.com', 'sophierogers', 'Sophie', 'Rogers', 'Soph'),
	('20', 'chloeanderson69@yahoo.com', 'chloeanderson', 'Chloe', 'Anderson', NULL),
	('21', 'alexanderkim@outlook.com', 'alexwiththehat', 'Alexander', 'Kim', NULL),
	('22', 'longthedragon@gmail.com', 'longthedragon', 'Charles', 'Long', 'Long'),
	('23', 'alexnakamura7@hotmail.com', 'Betteralex', 'Alexander', 'Nakamura', 'Alex'),
	('24', 'jameswang@yahoo.com', 'jameswang', 'James', 'Wang', NULL),
	('25', 'andrewpark88@hotmail.com', 'koreanpotato', 'Andrew', 'Park', NULL),
	('26', 'noahgoodwin10@gmail.com', 'n.goodwin56', 'Noah', 'Goodwin', NULL),
	('27', 'kev_whittaker@outlook.com', 'kev_whittaker', 'Kevin', 'Whittaker', NULL),
	('28', 'jackomoore@hotmail.com', 'JackWithAnM', 'Jack ', 'Moore', 'Jack M'),
	('29', 'ilovetomholland@gmail.com', 'tomhollandfanpage49401', 'Megan', 'Holland', NULL),
	('30', 'Rebecca1hacther@gmail.com', 'BeccaThatch', 'Rebecca', 'Thatcher', NULL),
	('31', 'clown@gmail.com', 'clown', NULL, NULL, NULL);
SET IDENTITY_INSERT [Person] OFF
GO

SET IDENTITY_INSERT [Club] ON
INSERT INTO [Club] ([ID], [Name], [OwnerPersonID], [StoredCash]) VALUES
	('1', 'Egyptians', '6', '0'),
	('2', 'Book', '7', '0'),
	('3', 'Card/Board Games', '25', '0'),
	('4', 'Gambling', '11', '0');
SET IDENTITY_INSERT [Club] OFF
GO

INSERT INTO [Membership] ([PersonID], [ClubID], [CashBalance], [IsMember], [IsCashAdmin], [IsGameAdmin]) VALUES
	('6', '1', '0', '1', '1', '1'),
	('3', '1', '0', '1', '0', '0'),
	('5', '1', '0', '1', '0', '0'),
	('8', '1', '0', '1', '0', '0'),
	('7', '2', '0', '1', '1', '1'),
	('13', '2', '0', '1', '0', '0'),
	('15', '2', '0', '1', '0', '0'),
	('17', '2', '0', '1', '1', '0'),
	('18', '2', '0', '1', '0', '0'),
	('25', '3', '0', '1', '1', '1'),
	('2', '3', '0', '1', '0', '0'),
	('10', '3', '0', '1', '0', '0'),
	('3', '3', '0', '1', '0', '0'),
	('4', '3', '0', '1', '0', '0'),
	('1', '3', '0', '1', '0', '0'),
	('12', '3', '0', '1', '0', '0'),
	('11', '4', '0', '1', '1', '1'),
	('24', '4', '0', '1', '0', '0'),
	('9', '4', '0', '1', '0', '0'),
	('30', '4', '0', '1', '0', '0'),
	('16', '4', '0', '1', '0', '0'),
	('22', '4', '0', '1', '0', '0'),
	('6', '3', '0', '1', '0', '0'),
	('21', '1', '0', '1', '0', '0');
GO

SET IDENTITY_INSERT [GameType] ON
INSERT INTO [GameType] ([ID], [Name], [IsZeroSum]) VALUES
	('1', 'Poker', '1'),
	('2', 'Mahjong', '1'),
	('3', 'Gin Rummy', '0'),
	('4', 'Black Jack', '0'),
	('5', 'Fantasy Football', '1');
SET IDENTITY_INSERT [GameType] OFF
GO

SET IDENTITY_INSERT [Game] ON
INSERT INTO [Game] ([ID], [ClubID], [GameTypeID], [Name], [ForCash], [AccumulateScore], [NetScoreChange], [StartDate], [EndDate]) VALUES
	('1', '1', '1', 'Mistake', '0', '1', '0', '2024-08-09 20:00:00.000', '2024-08-09 20:03:00.000'),
	('2', '1', '1', 'Poker Game', '1', '1', '0', '2024-08-09 20:04:00.000', '2024-08-09 23:31:00.000'),
	('3', '2', '5', 'Football League', '1', '1', '0', '2024-08-15 19:12:00.000', NULL);
SET IDENTITY_INSERT [Game] OFF
GO

INSERT INTO [GamePlayer] ([GameID], [PersonID]) VALUES
	('2', '6'),
	('2', '5'),
	('2', '3'),
	('2', '8'),
	('3', '7'),
	('3', '13'),
	('3', '15'),
	('3', '17'),
	('3', '18');
GO

SET IDENTITY_INSERT [GameRecord] ON
INSERT INTO [GameRecord] ([ID], [GameID], [PersonID], [ScoreChange], [CreatedTime], [CreatedByPersonID], [ModifiedTime], [ModifiedByPersonID]) VALUES
	('1', '2', '6', '-500', '2024-08-09 20:05:00.000', '6', '2024-08-09 20:05:00.000', '6'),
	('2', '2', '5', '-500', '2024-08-09 20:05:01.000', '6', '2024-08-09 20:05:01.000', '6'),
	('3', '2', '3', '-500', '2024-08-09 20:05:02.000', '6', '2024-08-09 20:05:02.000', '6'),
	('4', '2', '8', '-500', '2024-08-09 20:05:03.000', '6', '2024-08-09 20:05:03.000', '6'),
	('5', '2', '3', '-500', '2024-08-09 20:25:00.000', '6', '2024-08-09 20:25:00.000', '6'),
	('6', '2', '6', '-500', '2024-08-09 20:40:00.000', '6', '2024-08-09 20:40:00.000', '6'),
	('7', '2', '5', '250', '2024-08-09 21:11:00.000', '6', '2024-08-09 21:11:00.000', '6'),
	('8', '2', '3', '-500', '2024-08-09 22:03:00.000', '6', '2024-08-09 22:03:00.000', '6'),
	('9', '2', '8', '1750', '2024-08-09 23:31:00.000', '6', '2024-08-09 23:31:00.000', '6'),
	('10', '2', '6', '750', '2024-08-09 23:31:01.000', '6', '2024-08-09 23:31:01.000', '6'),
	('11', '2', '3', '750', '2024-08-09 23:31:02.000', '6', '2024-08-09 23:31:02.000', '6'),
	('12', '3', '7', '-1000', '2024-08-13 17:00:00.000', '17', '2024-08-13 17:00:00.000', '7'),
	('13', '3', '13', '-1000', '2024-08-13 17:00:00.001', '17', '2024-08-13 17:00:00.001', '7'),
	('14', '3', '15', '-1000', '2024-08-13 17:00:00.002', '17', '2024-08-13 17:00:00.002', '7'),
	('15', '3', '17', '-1000', '2024-08-13 17:00:00.003', '17', '2024-08-13 17:00:00.003', '7'),
	('16', '3', '18', '-1000', '2024-08-13 17:00:00.004', '17', '2024-08-13 17:00:00.004', '7');
SET IDENTITY_INSERT [GameRecord] OFF
GO

SET IDENTITY_INSERT [CashTransaction] ON
INSERT INTO [CashTransaction] ([ID], [SourcePersonID], [TargetPersonID], [ClubID], [Amount], [CreatedTime], [CreatedByPersonID], [ModifiedTime], [ModifiedByPersonID]) VALUES
	('1', '5', '3', '1', '500', '2024-08-09 20:23:00.000', '6', '2024-08-09 20:23:00.000', '6');
SET IDENTITY_INSERT [CashTransaction] OFF
GO

SET IDENTITY_INSERT [BankCashTransaction] ON
INSERT INTO [BankCashTransaction] ([ID], [PersonID], [ClubID], [Deposit], [CreatedTime], [CreatedByPersonID], [ModifiedTime], [ModifiedByPersonID]) VALUES
	('1', '5', '1', '1000', '2024-08-09 20:05:00.050', '6', '2024-08-09 20:05:00.050', '6'),
	('2', '3', '1', '500', '2024-08-09 20:05:00.050', '6', '2024-08-09 20:05:00.050', '6'),
	('3', '8', '1', '500', '2024-08-09 20:05:00.100', '6', '2024-08-09 20:05:00.100', '6'),
	('4', '7', '2', '1000', '2024-08-06 14:00:00.000', '7', '2024-08-06 14:00:00.000', '7'),
	('5', '13', '2', '1000', '2024-08-06 14:00:00.001', '7', '2024-08-06 14:00:00.001', '7'),
	('6', '15', '2', '1000', '2024-08-06 14:00:00.002', '7', '2024-08-06 14:00:00.002', '7'),
	('7', '17', '2', '1000', '2024-08-06 14:00:00.003', '7', '2024-08-06 14:00:00.003', '7'),
	('8', '18', '2', '1000', '2024-08-06 14:00:00.004', '7', '2024-08-06 14:00:00.004', '7'),
	('9', '3', '1', '500', '2024-08-09 22:01:00.000', '6', '2024-08-09 22:01:00.000', '6'),
	('10', '8', '1', '-1500', '2024-08-09 23:32:00.000', '6', '2024-08-09 23:32:00.000', '6');
SET IDENTITY_INSERT [BankCashTransaction] OFF
GO

INSERT INTO [AuthBasicPassword] ([PersonID], [Password]) VALUES
	('1', '{bcrypt}$2a$10$vXIWdLRXFH4YH0zztb58vOFVs6nkasFft7Obtuuenj5qQMvwtN0fa'),
	('2', '{bcrypt}$2a$10$5HDXyKXZexXLfGj9pZUUKeQbinMfW8tVIBa28VYlBs6jjYhrbuaVK'),
	('3', '{bcrypt}$2a$10$..R2MPVWkOrdQ338uLp9R.9MstPCWrvmQmbzJ3EQvNAm3wMpdQyfa'),
	('4', '{bcrypt}$2a$10$dmMNF3tv9JkD/f/Lj8uhBeWiXULcny/cqcERldaDRE5XFq.IE12dW'),
	('5', '{bcrypt}$2a$10$mMTUIr2jj5EEFrGU32VGXeaDUycObOWCX1ro6uz.dZ88yBC.C.Iwy'),
	('6', '{bcrypt}$2a$10$N92gfeqZNi1zVKQl7582Pu8w/jOsUDR3.LqNHrNNYMrCFbZs6Ae7K'),
	('7', '{bcrypt}$2a$10$DAWyaBf6LpNxbx7DAC/kVOd/zfkAHY0lPjZPTqaSdjaLFa0oUv68G'),
	('8', '{bcrypt}$2a$10$eZRtMzTi.S.oqnahkDEXUuH/ZUfObHCh6FefiqmyO/LwwDN5xMI/2'),
	('9', '{bcrypt}$2a$10$cmc6U54VYodAmwQQTByRPeHDoevFbwE0k0JDJ2wvIm..079NoviXu'),
	('10', '{bcrypt}$2a$10$uAe.wyy6ifNwvqFLXkXGzuYwtjmqQ51G8hlC65SrgGCY8M3v1QLo6'),
	('11', '{bcrypt}$2a$10$r1DTT1QjeVEJJlIdBsGdkOXU2SsyxH.NUyVXKbqrF3h51j8Tw2eXe'),
	('12', '{bcrypt}$2a$10$GrJ159Edt3mBgQ206KwvU.p.4W/6JS2.pLboHuhaGUmXq7yfrmlDO'),
	('13', '{bcrypt}$2a$10$PSVliKOR1BGalEOvft7EGOSJGdYlyXyQv4HXpvQKCeMmR.zVTZtGW'),
	('14', '{bcrypt}$2a$10$r5zXCvZx9wYF79musK36aeydEHVES091Xa4/OQZq2u1z95QM1vfRG'),
	('15', '{bcrypt}$2a$10$5f./hcJMZEqHHhJVV.FIj..e1kIbzvqTmmnPJwI.JGNUb/1WTLNJK'),
	('16', '{bcrypt}$2a$10$A6Evn2f1dReReiXZnC9bRuJ4SMa4i0jfzk9/NLFP5Xie2pYoVv2/W'),
	('17', '{bcrypt}$2a$10$xn6j.inAyBKQ1bgrWhyTku3gfC5S4GrDFCVnpNr0jtwcXer6inK1W'),
	('18', '{bcrypt}$2a$10$Tj3I/UtaFpTUVNlqTklf8uz29pPt/a6knIyXWZLzmM2lXVQ0KbnnO'),
	('19', '{bcrypt}$2a$10$po8n3M5yXUPXTZitXb.VEeDmUDLEXcIAoiiWagg8PowRvZoBuafFu'),
	('20', '{bcrypt}$2a$10$l/OYzSXraFM3TT4/cTOAOeerS4ZoMuJ1wVtqVj1rCJpIJyuKMV4Yi'),
	('21', '{bcrypt}$2a$10$lfIIUyxMdR2kexQ1gMDm..DfKC4C9LAAzVJ4I2vS.4CQgRAElempm'),
	('22', '{bcrypt}$2a$10$Ae6g5UvzuUAhf/V0w6Y6b.bSSbQlnX88ff0MUXOc6Ftfjb60QPmdC'),
	('23', '{bcrypt}$2a$10$lZbwPgW7kty.C5FVoub3oOzV95EhPM5SCKgi4RIDShCocRwgEMRW6'),
	('24', '{bcrypt}$2a$10$sgGdP./PU9wa2uwQMxdcleTdffYmJ55Y9tQ5Kul4L./PHvhEdaGLu'),
	('25', '{bcrypt}$2a$10$g45RF0MoaBbDEF7KI.x75eq57UDmbeilHFN9BS5g.68GRRBl/i8Eu'),
	('26', '{bcrypt}$2a$10$eVrwZphoq.CDf8kS1MZISuW1nGJdjOSS/s5YkaEDiDpiR6sHsSQme'),
	('27', '{bcrypt}$2a$10$FEAJqtn9MPUyx8c82yT.6.EgEe59/1H6R66mCOHM4zvEcw7omoEMK'),
	('28', '{bcrypt}$2a$10$.MMnCNJFoj7VIk/Y.tlFg.xsY1Moy7rnu/70m/A.D86f45Cbgmqd.'),
	('29', '{bcrypt}$2a$10$B9s/JYgMWM62PsObP/Wxeua9edLXmqsvx1Qqg3PM1w/Npm5KCAkxu'),
	('30', '{bcrypt}$2a$10$VzCwjjeFdLieQrhb1al4YO3aQzoYkEY9UdTyzmotE.FVyEVK5GEQq'),
	('31', '{bcrypt}$2a$10$u9C6tCo981fCS/M77y1Pve94wVg/DlVq4YhfKfBxUibmbJgXWlzQu');
GO

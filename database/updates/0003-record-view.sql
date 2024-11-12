USE [stats];
GO

CREATE VIEW [GameRecordExpanded] AS
	SELECT R.ID, G.ClubID, R.GameID, G.GameTypeID, G.ForCash, G.SeasonID, R.PersonID, R.ScoreChange, R.CreatedTime Date
  FROM [GameRecord] R JOIN [Game] G ON R.GameID = G.ID
  WHERE R.IsDeleted = 0 AND G.IsDeleted = 0;
GO
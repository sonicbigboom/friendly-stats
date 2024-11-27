export default class GameRecordExpanded {
  id: number;
  groupId: number;
  gameId: number;
  gameTypeId: number;
  forCash: boolean;
  seasonId: number;
  userId: number;
  scoreChange: number;
  date: Date;

  constructor();
  constructor(
    id?: number,
    groupId?: number,
    gameId?: number,
    gameTypeId?: number,
    forCash?: boolean,
    seasonId?: number,
    userId?: number,
    scoreChange?: number,
    date?: Date,
  ) {
    this.id = id ?? -1;
    this.groupId = groupId ?? -1;
    this.gameId = gameId ?? -1;
    this.gameTypeId = gameTypeId ?? -1;
    this.forCash = forCash ?? true;
    this.seasonId = seasonId ?? -1;
    this.userId = userId ?? -1;
    this.scoreChange = scoreChange ?? 0;
    this.date = date ?? new Date();
  }
}
export default class GameRecordExpanded {
  id: number;
  clubID: number;
  gameID: number;
  gameTypeID: number;
  forCash: boolean;
  seasonID: number;
  personID: number;
  scoreChange: number;
  date: Date;

  constructor();
  constructor(
    id?: number,
    clubID?: number,
    gameID?: number,
    gameTypeID?: number,
    forCash?: boolean,
    seasonID?: number,
    personID?: number,
    scoreChange?: number,
    date?: Date,
  ) {
    this.id = id ?? -1;
    this.clubID = clubID ?? -1;
    this.gameID = gameID ?? -1;
    this.gameTypeID = gameTypeID ?? -1;
    this.forCash = forCash ?? true;
    this.seasonID = seasonID ?? -1;
    this.personID = personID ?? -1;
    this.scoreChange = scoreChange ?? 0;
    this.date = date ?? new Date();
  }
}
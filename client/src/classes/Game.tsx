export default class Game {
  id: number;
  clubID: number;
  gameTypeID: number;
  name: string;
  forCash: boolean;
  seasonID: number;
  netScoreChange: number;
  startDate: Date;
  endDate: Date | null;

  constructor();
  constructor(
    id?: number,
    clubID?: number,
    gameTypeID?: number,
    name?: string,
    forCash?: boolean,
    seasonID?: number,
    netScoreChange?: number,
    startDate?: Date,
    endDate?: Date | null,
  ) {
    this.id = id ?? -1;
    this.clubID = clubID ?? -1;
    this.gameTypeID = gameTypeID ?? -1;
    this.name = name ?? "Loading...";
    this.forCash = forCash ?? true;
    this.seasonID = seasonID ?? -1;
    this.netScoreChange = netScoreChange ?? 0;
    this.startDate = startDate ?? new Date();
    this.endDate = endDate ?? null;
  }
}
export default class Game {
  id: number;
  groupId: number;
  gameTypeId: number;
  name: string;
  forCash: boolean;
  seasonId: number;
  netScoreChange: number;
  startDate: Date;
  endDate: Date | null;

  constructor();
  constructor(
    id?: number,
    groupId?: number,
    gameTypeId?: number,
    name?: string,
    forCash?: boolean,
    seasonId?: number,
    netScoreChange?: number,
    startDate?: Date,
    endDate?: Date | null,
  ) {
    this.id = id ?? -1;
    this.groupId = groupId ?? -1;
    this.gameTypeId = gameTypeId ?? -1;
    this.name = name ?? "Loading...";
    this.forCash = forCash ?? true;
    this.seasonId = seasonId ?? -1;
    this.netScoreChange = netScoreChange ?? 0;
    this.startDate = startDate ?? new Date();
    this.endDate = endDate ?? null;
  }
}
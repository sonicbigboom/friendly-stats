export default class Player {
  gameId: number;
  userId: number;
  metadata: string;
  createdTime: Date;
  createdByUserId: number;
  modifiedTime: Date;
  modifiedByUserId: number;

  constructor();
  constructor(
    gameId?: number,
    userId?: number,
    metadata?: string,
    createdTime?: Date,
    createdByUserId?: number,
    modifiedTime?: Date,
    modifiedByUserId?: number
  ) {
    this.gameId = gameId ?? -1;
    this.userId = userId ?? -1;
    this.metadata = metadata ?? "Loading...";
    this.createdTime = createdTime ?? new Date(0);
    this.createdByUserId = createdByUserId ?? -1;
    this.modifiedTime = modifiedTime ?? new Date(0);
    this.modifiedByUserId = modifiedByUserId ?? -1;
  }
}
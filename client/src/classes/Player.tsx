export default class Player {
  gameID: number;
  personID: number;
  metadata: string;
  createdTime: Date;
  createdByPersonID: number;
  modifiedTime: Date;
  modifiedByPersonID: number;

  constructor();
  constructor(
    gameID?: number,
    personID?: number,
    metadata?: string,
    createdTime?: Date,
    createdByPersonID?: number,
    modifiedTime?: Date,
    modifiedByPersonID?: number
  ) {
    this.gameID = gameID ?? -1;
    this.personID = personID ?? -1;
    this.metadata = metadata ?? "Loading...";
    this.createdTime = createdTime ?? new Date(0);
    this.createdByPersonID = createdByPersonID ?? -1;
    this.modifiedTime = modifiedTime ?? new Date(0);
    this.modifiedByPersonID = modifiedByPersonID ?? -1;
  }
}
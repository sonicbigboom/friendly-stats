export default class Group {
  id: number;
  name: string;
  ownerUserId: number;
  storedCash: number | null;

  public constructor();
  public constructor(
    id: number,
    name: string,
    ownerUserId: number,
    storedCash: number
  );
  public constructor(
    id?: number,
    name?: string,
    ownerUserId?: number,
    storedCash?: number
  ) {
    this.id = id ?? -1;
    this.name = name ?? "Loading...";
    this.ownerUserId = ownerUserId ?? -1;
    this.storedCash = storedCash ?? 0;
  }
}
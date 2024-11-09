export default class Group {
  id: number;
  name: string;
  ownerPersonID: number;
  storedCash: number | null;

  public constructor();
  public constructor(
    id: number,
    name: string,
    ownerPersonID: number,
    storedCash: number
  );
  public constructor(
    id?: number,
    name?: string,
    ownerPersonID?: number,
    storedCash?: number
  ) {
    this.id = id ?? -1;
    this.name = name ?? "Loading...";
    this.ownerPersonID = ownerPersonID ?? -1;
    this.storedCash = storedCash ?? 0;
  }
}
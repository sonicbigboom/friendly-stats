export default class Group {
  id: number;
  name: string;
  ownerPersonID: number;
  storedCash: number | null;

  public constructor(
    id: number,
    name: string,
    ownerPersonID: number,
    storedCash: number
  ) {
    this.id = id;
    this.name = name;
    this.ownerPersonID = ownerPersonID;
    this.storedCash = storedCash;
  }
}
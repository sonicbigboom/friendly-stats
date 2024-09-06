export default class Member {
  personID: number;
  clubID: number;
  personRole: PersonRole | null;
  cashBalance: number | null;
  firstName: string | null;
  lastName: string | null;
  nickname: string | null;

  public constructor();
  public constructor(
    personID: number,
    clubID: number,
    personRole: PersonRole | null,
    cashBalance: number | null,
    firstName: string | null,
    lastName: string | null,
    nickname: string | null
  )
  public constructor(
    personID?: number,
    clubID?: number,
    personRole?: PersonRole | null,
    cashBalance?: number | null,
    firstName?: string | null,
    lastName?: string | null,
    nickname?: string | null
  ) {
    this.personID = personID ?? -1;
    this.clubID = clubID ?? -1;
    this.personRole = personRole ?? null
    this.cashBalance = cashBalance ?? null;
    this.firstName = firstName ?? "Loading...";
    this.lastName = lastName ?? "Loading...";
    this.nickname = nickname ?? "Loading...";
  }

  clone() {
    return new Member(this.personID, this.clubID, this.personRole, this.cashBalance, this.firstName, this.lastName, this.nickname);
  }
}

export enum PersonRole {
  Player = "Player",
  GroupAdmin = "Group Admin",
  CashAdmin = "Cash Admin",
  CoOwner = "Co-Owner",
  Owner = "Owner"
}
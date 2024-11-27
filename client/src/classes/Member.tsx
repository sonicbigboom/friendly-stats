export default class Member {
  userId: number;
  groupId: number;
  userRole: UserRole | null;
  cashBalance: number | null;
  firstName: string | null;
  lastName: string | null;
  nickname: string | null;

  public constructor();
  public constructor(
    userId: number,
    groupId: number,
    userRole: UserRole | null,
    cashBalance: number | null,
    firstName: string | null,
    lastName: string | null,
    nickname: string | null
  )
  public constructor(
    userId?: number,
    groupId?: number,
    userRole?: UserRole | null,
    cashBalance?: number | null,
    firstName?: string | null,
    lastName?: string | null,
    nickname?: string | null
  ) {
    this.userId = userId ?? -1;
    this.groupId = groupId ?? -1;
    this.userRole = userRole ?? null
    this.cashBalance = cashBalance ?? null;
    this.firstName = firstName ?? "Loading...";
    this.lastName = lastName ?? "Loading...";
    this.nickname = nickname ?? "Loading...";
  }

  clone() {
    return new Member(this.userId, this.groupId, this.userRole, this.cashBalance, this.firstName, this.lastName, this.nickname);
  }
}

export enum UserRole {
  Player = "Player",
  GroupAdmin = "Group Admin",
  CashAdmin = "Cash Admin",
  CoOwner = "Co-Owner",
  Owner = "Owner"
}
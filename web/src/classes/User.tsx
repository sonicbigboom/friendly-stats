export default class User {
  id: number;
  username: string;
  email: string | null;
  firstName: string | null;
  lastName: string | null;
  nickname: string | null;

  public constructor();
  public constructor(
    id?: number,
    username?: string,
    email?: string | null,
    firstName?: string | null,
    lastName?: string | null,
    nickname?: string | null
  ) {
    this.id = id ?? -1;
    this.username = username ?? "Loading...";
    this.email = email ?? "Loading...";
    this.firstName = firstName ?? "Loading...";
    this.lastName = lastName ?? "Loading...";
    this.nickname = nickname ?? "Loading...";
  }
}
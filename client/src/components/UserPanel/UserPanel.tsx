import User from "../../classes/User";

type Props = {
  user: User;
};

export default function UserPanel({ user }: Props) {
  return (
    <div>
      <p>Username: {user.username}</p>
      <p>First Name: {user.firstName}</p>
      <p>Last Name: {user.lastName}</p>
      <p>Nickname: {user.nickname}</p>
    </div>
  );
}

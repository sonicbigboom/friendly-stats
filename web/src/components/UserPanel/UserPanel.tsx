import { useContext } from "react";
import User from "../../classes/User";
import { UserContext } from "../../data/User/UserContext";

type Props = {
  user: User;
};

export default function UserPanel() {

  const { user } = useContext(UserContext)

  return (
    <div>
      {(user.username) ? <p>Username: {user.username}</p> : <></>}
      {(user.firstName) ? <p>First Name: {user.firstName}</p> : <></>}
      {(user.lastName) ? <p>Last Name: {user.lastName}</p> : <></>}
      {(user.nickname) ? <p>Nickname: {user.nickname}</p> : <></>}
    </div>
  );
}

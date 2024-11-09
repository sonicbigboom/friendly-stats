import { useContext, useEffect, useState } from "react";
import User from "../../classes/User";
import { TokenContext } from "../../data/Token/TokenContext";

type Props = {
  user: User;
};

export default function UserPanel() {

  const { token } = useContext(TokenContext);
  const [user, setUser] = useState<User>(new User());

  useEffect(() => {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/me`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      const json = await response.json();

      setUser(json);
    });
  }, [token]);

  return (
    <div>
      {(user.username) ? <p>Username: {user.username}</p> : <></>}
      {(user.firstName) ? <p>First Name: {user.firstName}</p> : <></>}
      {(user.lastName) ? <p>Last Name: {user.lastName}</p> : <></>}
      {(user.nickname) ? <p>Nickname: {user.nickname}</p> : <></>}
    </div>
  );
}

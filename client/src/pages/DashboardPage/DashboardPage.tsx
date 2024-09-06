import React, { useContext, useEffect, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import User from "../../classes/User";
import GroupsPanel from "../../components/GroupsPanel/GroupsPanel";
import UserPanel from "../../components/UserPanel/UserPanel";

export default function DashboardPage() {
  const token = useContext(TokenContext);

  const [user, setUser] = useState<User>(new User());

  useEffect(() => {
    fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/me`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response;
      }

      const json = await response.json();

      setUser(json);
    });
  }, [token]);

  return (
    <>
      <h2>Dashboard</h2>
      <UserPanel user={user}/>
      <GroupsPanel/>
      <br />
    </>
  );
}





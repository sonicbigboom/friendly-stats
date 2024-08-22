import React, { useContext, useEffect, useState } from "react";
import { TokenContext } from "../Token/TokenContext";

export default function DashboardPage() {
  const token = useContext(TokenContext);

  const [userInfo, setUserInfo] = useState("");

  useEffect(() => {
    fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/me`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response;
      }

      const json = await response.json();

      setUserInfo(JSON.stringify(json));
    });
  }, [token]);

  return (
    <>
      <h2>Dashboard</h2>
      <p>{userInfo}</p>
    </>
  );
}

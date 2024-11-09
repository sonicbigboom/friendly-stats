import React, { useContext, useEffect, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import User from "../../classes/User";
import GroupsPanel from "../../components/GroupsPanel/GroupsPanel";
import UserPanel from "../../components/UserPanel/UserPanel";

export default function DashboardPage() {
  const { token, setToken } = useContext(TokenContext);

  return (
    <>
      <h2>Dashboard</h2>
      <UserPanel/>
      <GroupsPanel/>
      <br />
    </>
  );
}





import { useContext, useEffect, useState } from "react";
import Group from "../../classes/Group"
import { TokenContext } from "../../data/Token/TokenContext";
import { Link } from "react-router-dom";
import { GroupsContext } from "../../data/Groups/GroupsContext";

export default function GroupsPanel() {
  const { token } = useContext(TokenContext);
  const { getGroups, refresh } = useContext(GroupsContext);
  const [newGroupName, setNewGroupName] = useState("");
  
  const listGroups = getGroups().map(group => {
    if (group.id >= 0) {
      return (   
        <li key={group.id}>
          <Link to={`/group/${group.id}`}>{group.name}</Link>
        </li>
      );
    } else {
      return (   
        <li>{group.name}</li>
      );
    }
  })

  async function createGroup() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/groups?name=${newGroupName}`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token }),
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        refresh();
      }
    );
  }

  return (
    <div>
      <h3>Groups:</h3>
      {listGroups}
      <input type="text" onChange={(e) => setNewGroupName(e.target.value)} />
      <button onClick={createGroup}>Create New Group</button>
    </div>
  )
}
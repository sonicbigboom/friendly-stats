import { useContext, useEffect, useState } from "react";
import Group from "../../classes/Group"
import { TokenContext } from "../../data/Token/TokenContext";
import { Link } from "react-router-dom";

export default function GroupsPanel() {
  const token = useContext(TokenContext);
  const [groups, setGroups] = useState<Group[]>([]);
  const [newGroupName, setNewGroupName] = useState("");
  
  const listGroups = groups.map(group => {
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

  useEffect(() => {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      if (response.status === 204) {
        setGroups([]);
        return;
      }

      const json = await response.json();

      setGroups(json);
    });
  }, [token, groups.length]);

  async function createGroup() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups?name=${newGroupName}`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token }),
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        setGroups([...groups, new Group(-1, `Loading ${newGroupName}...`, -1, 0)])
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
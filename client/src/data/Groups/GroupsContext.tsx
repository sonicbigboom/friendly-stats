import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Group from "../../classes/Group";

export const GroupsContext = createContext({getGroups: () => { return [] as Group[] }, refresh: () => {}});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function GroupsContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [groups, setGroups] = useState<Group[]>([]);
  const [refreshDate, setRefreshDate] = useState(new Date(0))

  function refresh() {
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
      setRefreshDate(new Date())
    });
  }

  function getGroups() {

    if (refreshDate.getTime() + REFRESH_RATE < Date.now()) {
      refresh();
    }

    return groups;
  }

  const provider = useMemo(() => ({getGroups: getGroups, refresh: refresh}), [groups])

  return (
    <GroupsContext.Provider value={provider}>
    {children}
    </GroupsContext.Provider>
  )
}

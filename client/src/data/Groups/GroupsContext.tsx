import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Group from "../../classes/Group";

export const GroupsContext = createContext({
  refresh: () => {},
  getGroups: () => { return [] as Group[] }, 
  getGroup: (id: number) => { return new Group(); }
});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function GroupsContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [groups, setGroups] = useState<Group[]>([]);
  const [refreshDate, setRefreshDate] = useState(new Date(0))

  function refresh() {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/groups`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      let json: Group[];
      if (response.status == 204) {
        json = []
      } else {
        json = await response.json();
      }
      
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

  function getGroup(id: number) {
    if (refreshDate.getTime() + REFRESH_RATE < Date.now()) {
      refresh();
    }
    
    const group = groups.find((g:Group) => { return g.id === id; })
    if (!group) {
      return new Group()
    }

    return group;
  }

  const provider = useMemo(() => ({refresh: refresh, getGroups: getGroups, getGroup}), [groups])

  return (
    <GroupsContext.Provider value={provider}>
    {children}
    </GroupsContext.Provider>
  )
}

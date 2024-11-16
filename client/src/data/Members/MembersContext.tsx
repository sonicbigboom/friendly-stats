import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Member from "../../classes/Member";

export const MembersContext = createContext({
  refresh: (groupID: number) => {},
  getMembers: (groupID: number) => { return [] as Member[] },
  getMember: (groupID: number, userID: number) => { return new Member() }
});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function MembersContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [members, setMembers] = useState<{[groupID: number] : Member[]}>({});
  const [refreshDates, setRefreshDates] = useState<{[groupID: number] : Date}>({})

  function refresh(groupID: number) {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/users`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      let json: Member[];
      if (response.status == 204) {
        json = []
      } else {
        json = await response.json();
      }

      setMembers(d => {return {
        ...d,
        [groupID]: json
      }});

      setRefreshDates(d => {return {
        ...d,
        [groupID]: new Date()
      }})
    });
  }

  function getMembers(groupID: number) {
    if (!refreshDates[groupID] || refreshDates[groupID].getTime() + REFRESH_RATE < Date.now()) {
      refresh(groupID);
    }

    const ms = members[groupID];
    if (!ms) {
      return [] as Member[];
    }

    return ms;
  }

  function getMember(groupID: number, userID: number) {
    const ms = getMembers(groupID);

    const member = ms.find((m:Member) => { return m.personID === userID; })
    if (!member) {
      return new Member()
    }

    return member;
  }

  const provider = useMemo(() => ({refresh: refresh, getMembers: getMembers, getMember: getMember}), [members])

  return (
    <MembersContext.Provider value={provider}>
    {children}
    </MembersContext.Provider>
  )
}

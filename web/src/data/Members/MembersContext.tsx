import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Member from "../../classes/Member";

export const MembersContext = createContext({
  refresh: (groupId: number) => {},
  getMembers: (groupId: number) => { return [] as Member[] },
  getMember: (groupId: number, userId: number) => { return new Member() }
});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function MembersContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [members, setMembers] = useState<{[groupId: number] : Member[]}>({});
  const [refreshDates, setRefreshDates] = useState<{[groupId: number] : Date}>({})

  function refresh(groupId: number) {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/groups/${groupId}/users`, {
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
        [groupId]: json
      }});

      setRefreshDates(d => {return {
        ...d,
        [groupId]: new Date()
      }})
    });
  }

  function getMembers(groupId: number) {
    if (!refreshDates[groupId] || refreshDates[groupId].getTime() + REFRESH_RATE < Date.now()) {
      refresh(groupId);
    }

    const ms = members[groupId];
    if (!ms) {
      return [] as Member[];
    }

    return ms;
  }

  function getMember(groupId: number, userId: number) {
    const ms = getMembers(groupId);

    const member = ms.find((m:Member) => { return m.userId === userId; })
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

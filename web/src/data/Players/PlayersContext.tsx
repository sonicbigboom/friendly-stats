import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Player from "../../classes/Player";
import Member from "../../classes/Member";
import { MembersContext } from "../Members/MembersContext";

export const PlayersContext = createContext({
  refresh: (groupId: number, gameId: number) => {},
  getPlayers: (groupId: number, gameId: number) => { return [] as Player[] },
  getPlayerMembers: (groupId: number, gameId: number) => { return [] as Member[] },
  getNonPlayerMembers: (groupId: number, gameId: number) => { return [] as Member[] }
});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function PlayersContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [players, setPlayers] = useState<{[gameId: number] : Player[]}>({});
  const [refreshDates, setRefreshDates] = useState<{[gameId: number] : Date}>({})
  const { getMembers, refresh: refreshMember } = useContext(MembersContext);

  function refresh(groupId: number, gameId: number) {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/games/${gameId}/players`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      let json: Player[];
      if (response.status == 204) {
        json = []
      } else {
        json = await response.json();
      }

      setPlayers(d => { return {
        ...d,
        [gameId]: json
      }});

      setRefreshDates(d => { return {
        ...d,
        [gameId]: new Date()
      }})
    });

    refreshMember(groupId);
  }

  function getPlayers(groupId: number, gameId: number) {
    if (!refreshDates[gameId] || refreshDates[gameId].getTime() + REFRESH_RATE < Date.now()) {
      refresh(groupId, gameId);
    }

    const ps = players[gameId];
    if (!ps) {
      return [] as Player[];
    }

    return ps;
  }

  function getPlayerMembers(groupId: number, gameId: number) {
    const members = getMembers(groupId);
    const players = getPlayers(groupId, gameId);
    const playerMembers = members.filter((m) => {
      for (const player of players) {
        if (m.userId === player.userId) {
          return true;
        }
      }
      return false;
    })

    return playerMembers;
  }

  function getNonPlayerMembers(groupId: number, gameId: number) {
    const members = getMembers(groupId);
    const players = getPlayers(groupId, gameId);
    const playerMembers = members.filter((m) => {
      for (const player of players) {
        if (m.userId === player.userId) {
          return false;
        }
      }
      return true;
    })

    return playerMembers;
  }

  const provider = useMemo(() => ({refresh: refresh, getPlayers: getPlayers, getPlayerMembers: getPlayerMembers, getNonPlayerMembers: getNonPlayerMembers}), [players, getMembers])

  return (
    <PlayersContext.Provider value={provider}>
    {children}
    </PlayersContext.Provider>
  )
}

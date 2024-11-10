import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Player from "../../classes/Player";
import Member from "../../classes/Member";
import { MembersContext } from "../Members/MembersContext";

export const PlayersContext = createContext({
  refresh: (groupID: number, gameID: number) => {},
  getPlayers: (groupID: number, gameID: number) => { return [] as Player[] },
  getPlayerMembers: (groupID: number, gameID: number) => { return [] as Member[] },
  getNonPlayerMembers: (groupID: number, gameID: number) => { return [] as Member[] }
});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function PlayersContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [players, setPlayers] = useState<{[gameID: number] : Player[]}>({});
  const [refreshDates, setRefreshDates] = useState<{[gameID: number] : Date}>({})
  const { getMembers, refresh: refreshMember } = useContext(MembersContext);

  function refresh(groupID: number, gameID: number) {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/games/${gameID}/players`, {
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
        [gameID]: json
      }});

      setRefreshDates(d => { return {
        ...d,
        [gameID]: new Date()
      }})
    });

    refreshMember(groupID);
  }

  function getPlayers(groupID: number, gameID: number) {
    if (!refreshDates[gameID] || refreshDates[gameID].getTime() + REFRESH_RATE < Date.now()) {
      refresh(groupID, gameID);
    }

    const ps = players[gameID];
    if (!ps) {
      return [] as Player[];
    }

    return ps;
  }

  function getPlayerMembers(groupID: number, gameID: number) {
    const members = getMembers(groupID);
    const players = getPlayers(groupID, gameID);
    const playerMembers = members.filter((m) => {
      for (const player of players) {
        if (m.personID === player.personID) {
          return true;
        }
      }
      return false;
    })

    return playerMembers;
  }

  function getNonPlayerMembers(groupID: number, gameID: number) {
    const members = getMembers(groupID);
    const players = getPlayers(groupID, gameID);
    const playerMembers = members.filter((m) => {
      for (const player of players) {
        if (m.personID === player.personID) {
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

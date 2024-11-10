import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Game from "../../classes/Game";

export const GamesContext = createContext({
  refresh: (groupID: number) => {},
  getGames: (groupID: number) => { return [] as Game[] },
  getGame: (groupID: number, gameID: number) => { return new Game() },
});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function GamesContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [games, setGames] = useState<{[groupID: number] : Game[]}>({});
  const [refreshDates, setRefreshDates] = useState<{[groupID: number] : Date}>({})

  function refresh(groupID: number) {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/games`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      let json: Game[];
      if (response.status == 204) {
        json = []
      } else {
        json = await response.json();
      }

      setGames(d => { return {
        ...d,
        [groupID]: json
      }});

      setRefreshDates(d => { return {
        ...d,
        [groupID]: new Date()
      }})
    });
  }

  function getGames(groupID: number) {
    if (!refreshDates[groupID] || refreshDates[groupID].getTime() + REFRESH_RATE < Date.now()) {
      refresh(groupID);
    }

    const gs = games[groupID];
    if (!gs) {
      return [] as Game[];
    }

    return gs;
  }

  function getGame(groupID: number, gameID: number) {
    const gs = getGames(groupID);

    const game = gs.find((g:Game) => { return g.id === gameID; })
    if (!game) {
      return new Game()
    }

    return game;
  }

  const provider = useMemo(() => ({refresh: refresh, getGames: getGames, getGame: getGame}), [games])

  return (
    <GamesContext.Provider value={provider}>
    {children}
    </GamesContext.Provider>
  )
}

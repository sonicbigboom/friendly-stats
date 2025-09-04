import { createContext, ReactNode, useContext, useMemo, useState } from "react";
import { TokenContext } from "../Token/TokenContext";
import Game from "../../classes/Game";

export const GamesContext = createContext({
  refresh: (groupId: number) => {},
  getGames: (groupId: number) => { return [] as Game[] },
  getGame: (groupId: number, gameId: number) => { return new Game() },
});

type Props = { children: ReactNode }

const REFRESH_RATE = 1 * 60 * 1000;

export default function GamesContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [games, setGames] = useState<{[groupId: number] : Game[]}>({});
  const [refreshDates, setRefreshDates] = useState<{[groupId: number] : Date}>({})

  function refresh(groupId: number) {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/groups/${groupId}/games`, {
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
        [groupId]: json
      }});

      setRefreshDates(d => { return {
        ...d,
        [groupId]: new Date()
      }})
    });
  }

  function getGames(groupId: number) {
    if (!refreshDates[groupId] || refreshDates[groupId].getTime() + REFRESH_RATE < Date.now()) {
      refresh(groupId);
    }

    const gs = games[groupId];
    if (!gs) {
      return [] as Game[];
    }

    return gs;
  }

  function getGame(groupId: number, gameId: number) {
    const gs = getGames(groupId);

    const game = gs.find((g:Game) => { return g.id === gameId; })
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

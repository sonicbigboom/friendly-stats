import { useContext, useEffect, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import Game from "../../classes/Game";
import { Link } from "react-router-dom";

type Props = {
  groupID: number;
  isGameAdmin: boolean;
};

export default function GamesPanel( { groupID, isGameAdmin }: Props) {
  const token = useContext(TokenContext);
  const [games, setGames] = useState<Game[]>([]);
  const [newGame, setNewGame] = useState<GameDto>(new GameDto());
  
  const listGames = games.map(game => {
    if (game.id >= 0) {

      let end = "On Going";
      if (game.endDate !== null) {
        end = new Date(game.endDate).toDateString();
      }

      return (   
        <li key={game.id}>
          <Link to={`/group/${groupID}/game/${game.id}`}>{String(game.name)} ({new Date(game.startDate).toDateString()} - {end})</Link>
        </li>
      );
    } else {
      return (   
        <li>Loading user {game.id}...</li>
      );
    }
  })

  useEffect(() => {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/games`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      if (response.status === 204) {
        setGames([]);
        return;
      }

      const json = await response.json();

      setGames(json);
    });
  }, [token, games.length]);

  async function addGame() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/games`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify(newGame)
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        setGames([...games, new Game()])
      }
    );
  }

  if (!isGameAdmin) {
    return (
      <div>
        <h3>Games:</h3>
        {listGames}
      </div>
    );
  }

  return (
    <div>
      <h3>Games:</h3>
      {listGames}
      <div>
        <h4>Add Game</h4>
        <label>Name</label>
        <input type="text" onChange={(e) => setNewGame(game => {
          const g = game.clone();
          g.name = e.target.value;
          return g;
        })} />
        <label>Game Type</label>
        <select onChange={(e) => setNewGame(game => {
          const g = game.clone();
          g.gameTypeID = Number(e.target.value);
          return g;
        })}>
          <option value={1}>Poker</option>
          <option value={4}>Fantasy Football</option>
        </select>
        <label>For Cash?</label>
        <input type="checkbox" onChange={(e) => setNewGame(game => {
          const g = game.clone();
          g.forCash = e.target.checked;
          return g;
        })} />
        <label>Season</label>
        <select onChange={(e) => setNewGame(game => {
          const g = game.clone();
          g.seasonID = Number(e.target.value);
          return g;
        })}>
          <option value={1}>Season 0</option>
        </select>
        <button onClick={addGame}>Add</button>
      </div>
    </div>
  )
}

class GameDto {
  name: string;
  gameTypeID: number;
  forCash: boolean;
  seasonID: number;

  public constructor();
  public constructor(
    name: string,
    gameTypeID: number,
    forCash: boolean,
    seasonID: number
  );
  public constructor(
    name?: string,
    gameTypeID?: number,
    forCash?: boolean,
    seasonID?: number
  ) {
    this.name = name ?? "";
    this.gameTypeID = gameTypeID ?? 1;
    this.forCash = forCash ?? true;
    this.seasonID = seasonID ?? 1;
  }

  clone() {
    return new GameDto(this.name, this.gameTypeID, this.forCash, this.seasonID);
  }
}
import { useContext, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { Link } from "react-router-dom";
import { GamesContext } from "../../data/Games/GamesContext";

type Props = {
  groupId: number;
  isGameAdmin: boolean;
};

export default function GamesPanel( { groupId, isGameAdmin }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const { getGames, refresh } = useContext(GamesContext)
  const [newGame, setNewGame] = useState<GameDto>(new GameDto());
  
  const listGames = getGames(groupId).map(game => {
    if (game.id >= 0) {

      let end = "On Going";
      if (game.endDate !== null) {
        end = new Date(game.endDate).toDateString();
      }

      return (   
        <li key={game.id}>
          <Link to={`/group/${groupId}/game/${game.id}`}>{String(game.name)} ({new Date(game.startDate).toDateString()} - {end})</Link>
        </li>
      );
    } else {
      return (   
        <li key={-1}>Loading user {game.id}...</li>
      );
    }
  })

  async function addGame() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupId}/games`,
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
        refresh(groupId);
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
        <label>
          Name{}
          <input type="text" onChange={(e) => setNewGame(game => {
            const g = game.clone();
            g.name = e.target.value;
            return g;
          })} />
        </label>
        <label>
          Game Type{}
          <select onChange={(e) => setNewGame(game => {
            const g = game.clone();
            g.gameTypeId = Number(e.target.value);
            return g;
          })}>
            <option value={1}>Poker</option>
            <option value={4}>Fantasy Football</option>
          </select>
        </label>
        <label>
          For Cash?{}
          <input type="checkbox" onChange={(e) => setNewGame(game => {
            const g = game.clone();
            g.forCash = e.target.checked;
            return g;
          })} />
        </label>
        <label>
          Season{}
          <select onChange={(e) => setNewGame(game => {
            const g = game.clone();
            g.seasonId = Number(e.target.value);
            return g;
          })}>
            <option value={1}>Season 0</option>
          </select>
        </label>
        <button onClick={addGame}>Add</button>
      </div>
    </div>
  )
}

class GameDto {
  name: string;
  gameTypeId: number;
  forCash: boolean;
  seasonId: number;

  public constructor();
  public constructor(
    name: string,
    gameTypeId: number,
    forCash: boolean,
    seasonId: number
  );
  public constructor(
    name?: string,
    gameTypeId?: number,
    forCash?: boolean,
    seasonId?: number
  ) {
    this.name = name ?? "";
    this.gameTypeId = gameTypeId ?? 1;
    this.forCash = forCash ?? true;
    this.seasonId = seasonId ?? 1;
  }

  clone() {
    return new GameDto(this.name, this.gameTypeId, this.forCash, this.seasonId);
  }
}
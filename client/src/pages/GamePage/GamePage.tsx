import { useParams } from "react-router-dom";
import "./GamePage.css";
import PlayerPanel from "../../components/PlayerPanel/PlayerPanel";
import { useContext } from "react";
import { GamesContext } from "../../data/Games/GamesContext";

export default function GroupPage() {
  const { groupId, gameId } = useParams()
  const { getGame } = useContext(GamesContext)

  return (
    <>
      <h2>{ getGame(Number(groupId), Number(gameId)).name }</h2>
      <div className="row">
        <div className="column">
          <PlayerPanel groupId={Number(groupId)} gameId={Number(gameId)} isGameAdmin={true} isCashAdmin={true} />
        </div>
        <div className="column">
        </div>
      </div>
      <br />
    </>
  );
}
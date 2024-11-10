import { useParams } from "react-router-dom";
import "./GamePage.css";
import PlayerPanel from "../../components/PlayerPanel/PlayerPanel";
import { useContext } from "react";
import { GamesContext } from "../../data/Games/GamesContext";

export default function GroupPage() {
  const { groupID, gameID } = useParams()
  const { getGame } = useContext(GamesContext)

  return (
    <>
      <h2>{ getGame(Number(groupID), Number(gameID)).name }</h2>
      <div className="row">
        <div className="column">
          <PlayerPanel groupID={Number(groupID)} gameID={Number(gameID)} isGameAdmin={true} isCashAdmin={true} />
        </div>
        <div className="column">
        </div>
      </div>
      <br />
    </>
  );
}
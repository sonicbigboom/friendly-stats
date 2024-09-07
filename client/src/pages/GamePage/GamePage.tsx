import { useContext } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { useParams } from "react-router-dom";
import MembersPanel from "../../components/MembersPanel/MembersPanel";
import "./GamePage.css";
import GamesPanel from "../../components/GamesPanel/GamesPanel";

export default function GroupPage() {
  const token = useContext(TokenContext);
  const { gameID } = useParams()

  return (
    <>
      <h2>Game</h2>
      <p>{gameID}</p>
      <div className="row">
        <div className="column">
        </div>
        <div className="column">
        </div>
      </div>
      <br />
    </>
  );
}
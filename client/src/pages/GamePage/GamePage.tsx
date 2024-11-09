import { useContext, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { useParams } from "react-router-dom";
import "./GamePage.css";
import PlayerPanel from "../../components/PlayerPanel/PlayerPanel";

export default function GroupPage() {
  const { token, setToken } = useContext(TokenContext);
  const { groupID, gameID } = useParams()

  return (
    <>
      <h2>Game</h2>
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
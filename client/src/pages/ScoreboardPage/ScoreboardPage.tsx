import { useParams } from "react-router-dom";
import "./ScoreboardPage.css";
import { useContext, useState } from "react";
import { GroupsContext } from "../../data/Groups/GroupsContext";
import ScoreboardPanel from "../../components/ScoreboardPanel/ScoreboardPanel";

export default function ScoreboardPage() {
  const { groupID } = useParams()
  const { getGroup } = useContext(GroupsContext);
  const [ gameTypeID, setGameTypeID ] = useState(1);
  const [ forCash, setForCash ] = useState(true);
  const [ seasonID, setSeasonID ] = useState(1);
  const [ userID, setUserID ] = useState(-1);

  return (
    <>
      <h2>{getGroup(Number(groupID)).name} Scoreboard</h2>
        <div className="column">
          <ScoreboardPanel groupID={Number(groupID)} gameTypeID={gameTypeID} forCash={forCash} seasonID={seasonID} userID={userID} />
        </div>
      <br />
    </>
  );
}
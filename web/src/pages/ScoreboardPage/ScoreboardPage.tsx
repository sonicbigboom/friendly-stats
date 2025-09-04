import { useParams } from "react-router-dom";
import "./ScoreboardPage.css";
import { useContext, useState } from "react";
import { GroupsContext } from "../../data/Groups/GroupsContext";
import ScoreboardPanel from "../../components/ScoreboardPanel/ScoreboardPanel";

export default function ScoreboardPage() {
  const { groupId } = useParams()
  const { getGroup } = useContext(GroupsContext);
  const [ gameTypeId, setGameTypeId ] = useState(1);
  const [ forCash, setForCash ] = useState(true);
  const [ seasonId, setSeasonId ] = useState(1);
  const [ userId, setUserId ] = useState(-1);

  return (
    <>
      <h2>{getGroup(Number(groupId)).name} Scoreboard</h2>
        <div className="column">
          <ScoreboardPanel groupId={Number(groupId)} gameTypeId={gameTypeId} forCash={forCash} seasonId={seasonId} userId={userId} />
        </div>
      <br />
    </>
  );
}
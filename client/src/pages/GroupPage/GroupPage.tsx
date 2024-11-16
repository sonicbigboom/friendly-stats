import { useParams } from "react-router-dom";
import MembersPanel from "../../components/MembersPanel/MembersPanel";
import "./GroupPage.css";
import GamesPanel from "../../components/GamesPanel/GamesPanel";
import { useContext } from "react";
import { Link } from "react-router-dom";
import { GroupsContext } from "../../data/Groups/GroupsContext";

export default function GroupPage() {
  const { groupID } = useParams()
  const { getGroup } = useContext(GroupsContext);

  return (
    <>
      <h2>{getGroup(Number(groupID)).name}</h2>
      <div className="row">
        <div className="column">
          <MembersPanel groupID={Number(groupID)} isCashAdmin={true}/>
        </div>
        <div className="column">
          <Link to={`/group/${Number(groupID)}/scoreboard`}>Scoreboard</Link>
          <GamesPanel groupID={Number(groupID)} isGameAdmin={true}/>
        </div>
      </div>
      <br />
    </>
  );
}
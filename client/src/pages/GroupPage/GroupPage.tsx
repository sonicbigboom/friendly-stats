import { useParams } from "react-router-dom";
import MembersPanel from "../../components/MembersPanel/MembersPanel";
import "./GroupPage.css";
import GamesPanel from "../../components/GamesPanel/GamesPanel";
import { useContext } from "react";
import { Link } from "react-router-dom";
import { GroupsContext } from "../../data/Groups/GroupsContext";

export default function GroupPage() {
  const { groupId } = useParams()
  const { getGroup } = useContext(GroupsContext);

  return (
    <>
      <h2>{getGroup(Number(groupId)).name}</h2>
      <div className="row">
        <div className="column">
          <MembersPanel groupId={Number(groupId)} isCashAdmin={true}/>
        </div>
        <div className="column">
          <Link to={`/group/${Number(groupId)}/scoreboard`}>Scoreboard</Link>
          <GamesPanel groupId={Number(groupId)} isGameAdmin={true}/>
        </div>
      </div>
      <br />
    </>
  );
}
import { useContext } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { useParams } from "react-router-dom";
import MembersPanel from "../../components/MembersPanel/MembersPanel";
import "./GroupPage.css";
import GamesPanel from "../../components/GamesPanel/GamesPanel";

export default function GroupPage() {
  const { token, setToken } = useContext(TokenContext);
  const { groupID } = useParams()

  return (
    <>
      <h2>Group</h2>
      <div className="row">
        <div className="column">
          <MembersPanel groupID={Number(groupID)} isCashAdmin={true}/>
        </div>
        <div className="column">
          <GamesPanel groupID={Number(groupID)} isGameAdmin={true}/>
        </div>
      </div>
      <br />
    </>
  );
}
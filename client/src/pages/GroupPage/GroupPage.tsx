import { useContext } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { useParams } from "react-router-dom";
import MembersPanel from "../../components/MembersPanel/MembersPanel";

export default function GroupPage() {
  const token = useContext(TokenContext);
  const { groupID } = useParams()

  return (
    <>
      <h2>Group</h2>
      <MembersPanel groupID={Number(groupID)} isCashAdmin={true}/>
      <br />
    </>
  );
}
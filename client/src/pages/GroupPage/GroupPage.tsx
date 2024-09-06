import { useContext } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { useParams } from "react-router-dom";

export default function GroupPage() {
  const token = useContext(TokenContext);
  const { groupID } = useParams()

  return (
    <>
      <h2>Group</h2>
      <p>{groupID}</p>
      <br />
    </>
  );
}
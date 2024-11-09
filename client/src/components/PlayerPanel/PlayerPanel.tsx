import { useContext, useEffect, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import Member, { PersonRole } from "../../classes/Member";
import { BankTransaction } from "../MembersPanel/MembersPanel";

type Props = {
  groupID: number;
  gameID: number;
  isGameAdmin: boolean;
  isCashAdmin: boolean;
};

export default function PlayerPanel( { groupID, gameID, isGameAdmin, isCashAdmin }: Props) {
  const { token, setToken } = useContext(TokenContext);
  const [members, setMembers] = useState<Member[]>([]);
  
  const listPlayers = members.map(member => {
    if (member.personID >= 0 && member.clubID >= 0) {

      return (   
        <li key={member.personID}>
          <p>{member.firstName} {member.lastName}{(member.nickname) ? <> ({member.nickname})</> : <></>}: {member.cashBalance}</p>
          <GameRecord userID={member.personID} groupID={groupID} gameID={gameID} isGameAdmin={isGameAdmin}/>
          <BankTransaction userID={member.personID} groupID={member.clubID} isCashAdmin={isCashAdmin}/>
        </li>
      );
    } else {
      return (   
        <li>Loading user {member.personID}...</li>
      );
    }
  })

  useEffect(() => {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/users`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      if (response.status === 204) {
        setMembers([]);
        return;
      }

      const json = await response.json();

      setMembers(json);
    });
  }, [token, members.length]);

  return (
    <div>
      <h3>Player:</h3>
      {listPlayers}
    </div>
  )
}

type GameRecordProps = {
  userID: number;
  groupID: number;
  gameID: number;
  isGameAdmin: boolean;
};

export function GameRecord({ userID, groupID, gameID, isGameAdmin }: GameRecordProps) {
  const { token, setToken } = useContext(TokenContext);
  const [scoreChange, setScoreChange] = useState(0);

  if (!isGameAdmin) { return <></> }

  function recordScoreChange() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/games/${gameID}/records`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify({userID: userID, scoreChange: scoreChange}),
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
      }
    );
  }

  return (
    <div>
      <input type="number" id={`deposit-${userID}`} onChange={(e) => setScoreChange(Number(e.target.value))}/>
      <button type="button" onClick={recordScoreChange}>Record Score Change</button>
    </div>
  );
}
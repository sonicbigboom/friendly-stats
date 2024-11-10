import { useContext, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { BankTransaction } from "../MembersPanel/MembersPanel";
import { PlayersContext } from "../../data/Players/PlayersContext";

type Props = {
  groupID: number;
  gameID: number;
  isGameAdmin: boolean;
  isCashAdmin: boolean;
};

export default function PlayerPanel( { groupID, gameID, isGameAdmin, isCashAdmin }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const { getPlayerMembers, getNonPlayerMembers, refresh } = useContext(PlayersContext);
  const [ newPlayerID, setNewPlayerID ] = useState((getNonPlayerMembers(groupID, gameID).length > 0) ? getNonPlayerMembers(groupID, gameID)[0].personID : -1);
  
  const players = getPlayerMembers(groupID, gameID);
  const nonPlayers = getNonPlayerMembers(groupID, gameID);

  const listPlayers = players.map(member => {
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
        <li key={-1}>Loading user {member.personID}...</li>
      );
    }
  })

  const listNewPlayerOptions = nonPlayers.map(member => {
    return  (
      <option key={member.personID} value={member.personID}>{member.firstName} {member.lastName}{(member.nickname) ? (" " + member.nickname) : ""}</option>
    )
  })

  function addNewPlayer() {
    let id = newPlayerID;
    if (id === -1) {
      id = nonPlayers[0].personID
    }
    setNewPlayerID(-1)
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/games/${gameID}/players`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify({userID: id, metadata: ""}),
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        refresh(groupID, gameID)
      }
    );
  }

  const addNewPlayerSelector = (
    <div>
      <label>
        Add Player{}
        <select onChange={(e) => setNewPlayerID(Number(e.target.value))}>
          {listNewPlayerOptions}
        </select>
      </label>
      <button onClick={addNewPlayer}>Add</button>
    </div>
  )

  return (
    <div>
      <h3>Players:</h3>
      {listPlayers}
      {(nonPlayers.length > 0) ? addNewPlayerSelector : <></>}
    </div>
  )
}

type GameRecordProps = {
  userID: number;
  groupID: number;
  gameID: number;
  isGameAdmin: boolean;
};

export function GameRecord({ userID, groupID, gameID, isGameAdmin }: Readonly<GameRecordProps>) {
  const { token } = useContext(TokenContext);
  const { refresh: refreshPlayers } = useContext(PlayersContext);
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
        refreshPlayers(groupID, gameID);
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
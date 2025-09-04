import { useContext, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { BankTransaction } from "../MembersPanel/MembersPanel";
import { PlayersContext } from "../../data/Players/PlayersContext";

type Props = {
  groupId: number;
  gameId: number;
  isGameAdmin: boolean;
  isCashAdmin: boolean;
};

export default function PlayerPanel( { groupId, gameId, isGameAdmin, isCashAdmin }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const { getPlayerMembers, getNonPlayerMembers, refresh } = useContext(PlayersContext);
  const [ newPlayerId, setNewPlayerId ] = useState((getNonPlayerMembers(groupId, gameId).length > 0) ? getNonPlayerMembers(groupId, gameId)[0].userId : -1);
  
  const players = getPlayerMembers(groupId, gameId);
  const nonPlayers = getNonPlayerMembers(groupId, gameId);

  const listPlayers = players.map(member => {
    if (member.userId >= 0 && member.groupId >= 0) {

      return (   
        <li key={member.userId}>
          <p>{member.firstName} {member.lastName}{(member.nickname) ? <> ({member.nickname})</> : <></>}: {member.cashBalance}</p>
          <GameRecord userId={member.userId} groupId={groupId} gameId={gameId} isGameAdmin={isGameAdmin}/>
          <BankTransaction userId={member.userId} groupId={member.groupId} isCashAdmin={isCashAdmin}/>
        </li>
      );
    } else {
      return (   
        <li key={-1}>Loading user {member.userId}...</li>
      );
    }
  })

  const listNewPlayerOptions = nonPlayers.map(member => {
    return  (
      <option key={member.userId} value={member.userId}>{member.firstName} {member.lastName}{(member.nickname) ? (" " + member.nickname) : ""}</option>
    )
  })

  function addNewPlayer() {
    let id = newPlayerId;
    if (id === -1) {
      id = nonPlayers[0].userId
    }
    setNewPlayerId(-1)
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/games/${gameId}/players`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify({userId: id, metadata: ""}),
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        refresh(groupId, gameId)
      }
    );
  }

  const addNewPlayerSelector = (
    <div>
      <label>
        Add Player{}
        <select onChange={(e) => setNewPlayerId(Number(e.target.value))}>
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
  userId: number;
  groupId: number;
  gameId: number;
  isGameAdmin: boolean;
};

export function GameRecord({ userId, groupId, gameId, isGameAdmin }: Readonly<GameRecordProps>) {
  const { token } = useContext(TokenContext);
  const { refresh: refreshPlayers } = useContext(PlayersContext);
  const [scoreChange, setScoreChange] = useState(0);

  if (!isGameAdmin) { return <></> }

  function recordScoreChange() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/games/${gameId}/records`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify({userId: userId, scoreChange: scoreChange}),
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        refreshPlayers(groupId, gameId);
      }
    );
  }

  return (
    <div>
      <input type="number" id={`deposit-${userId}`} onChange={(e) => setScoreChange(Number(e.target.value))}/>
      <button type="button" onClick={recordScoreChange}>Record Score Change</button>
    </div>
  );
}
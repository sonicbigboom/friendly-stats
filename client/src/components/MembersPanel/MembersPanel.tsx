import { useContext, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { UserRole } from "../../classes/Member";
import { MembersContext } from "../../data/Members/MembersContext";

type Props = {
  groupId: number;
  isCashAdmin: boolean;
};

export default function MembersPanel( { groupId, isCashAdmin }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const { getMembers, refresh } = useContext(MembersContext)
  const [newMember, setNewMember] = useState<MemberDto>(new MemberDto("", UserRole.Player, "", "", ""));
  
  const listMembers = getMembers(groupId).map(member => {
    if (member.userId >= 0 && member.groupId >= 0) {

      return (   
        <li key={member.userId}>
          <p>{member.firstName} {member.lastName}{(member.nickname) ? <> ({member.nickname})</> : <></>}: {member.cashBalance}</p>
          <BankTransaction userId={member.userId} groupId={member.groupId} isCashAdmin={isCashAdmin}/>
        </li>
      );
    } else {
      return (   
        <li key={-1}>Loading user {member.userId}...</li>
      );
    }
  })

  async function addMember() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/groups/${groupId}/users`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify(newMember)
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        refresh(groupId)
      }
    );
  }

  return (
    <div>
      <h3>Members:</h3>
      {listMembers}
      <div>
        <h4>Add Member</h4>
        <label>
          Email{}
          <input type="text" onChange={(e) => setNewMember(member => {
            const m = member.clone();
            m.identifier = e.target.value;
            return m;
          })} />
        </label>
        <label>
          First Name{}
          <input type="text" onChange={(e) => setNewMember(member => {
            const m = member.clone();
            m.firstName = e.target.value;
            return m;
          })} />
        </label>
        <label>
          Last Name{}
          <input type="text" onChange={(e) => setNewMember(member => {
            const m = member.clone();
            m.lastName = e.target.value;
            return m;
          })} />
        </label>
        <label>
          Nickname{}
          <input type="text" onChange={(e) => setNewMember(member => {
            const m = member.clone();
            m.nickname = e.target.value;
            return m;
          })} />
        </label>
        <button onClick={addMember}>Add</button>
      </div>
    </div>
  )
}

type BankTransactionProps = {
  groupId: number;
  userId: number;
  isCashAdmin: boolean;
};

export function BankTransaction({ groupId, userId, isCashAdmin }: Readonly<BankTransactionProps>) {
  const { token } = useContext(TokenContext);
  const [deposit, setDeposit] = useState(0);
  const { refresh: refreshMembers } = useContext(MembersContext);

  if (!isCashAdmin) { return <></> }

  function depositTransaction() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_API_HOST}/groups/${groupId}/bank`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify({userId: userId, deposit: deposit}),
      }
    ).then(
      async (response) => {
        if (!response.ok) {
          throw response.status;
        }
        refreshMembers(groupId);
      }
    );
  }

  return (
    <div>
      <input type="number" id={`deposit-${userId}`} onChange={(e) => setDeposit(Number(e.target.value))}/>
      <button type="button" onClick={depositTransaction}>Deposit</button>
    </div>
  );
}

class MemberDto {
  identifier: string;
  personRole: UserRole | null;
  firstName: string | null;
  lastName: string | null;
  nickname: string | null;

  public constructor(
    identifier: string,
    personRole: UserRole | null,
    firstName: string | null,
    lastName: string | null,
    nickname: string | null,
  ) {
    this.identifier = identifier;
    this.personRole = personRole;
    this.firstName = firstName;
    this.lastName = lastName;
    this.nickname = nickname;
  }

  clone() {
    return new MemberDto(this.identifier, this.personRole, this.firstName, this.lastName, this.nickname);
  }
}
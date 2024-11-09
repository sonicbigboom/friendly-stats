import { useContext, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { PersonRole } from "../../classes/Member";
import { MembersContext } from "../../data/Members/MembersContext";

type Props = {
  groupID: number;
  isCashAdmin: boolean;
};

export default function MembersPanel( { groupID, isCashAdmin }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const { getMembers, refresh } = useContext(MembersContext)
  const [newMember, setNewMember] = useState<MemberDto>(new MemberDto("", PersonRole.Player, "", "", ""));
  
  const listMembers = getMembers(groupID).map(member => {
    if (member.personID >= 0 && member.clubID >= 0) {

      return (   
        <li key={member.personID}>
          <p>{member.firstName} {member.lastName}{(member.nickname) ? <> ({member.nickname})</> : <></>}: {member.cashBalance}</p>
          <BankTransaction userID={member.personID} groupID={member.clubID} isCashAdmin={isCashAdmin}/>
        </li>
      );
    } else {
      return (   
        <li key={-1}>Loading user {member.personID}...</li>
      );
    }
  })

  async function addMember() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/users`,
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
        refresh(groupID)
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
  groupID: number;
  userID: number;
  isCashAdmin: boolean;
};

export function BankTransaction({ groupID, userID, isCashAdmin }: Readonly<BankTransactionProps>) {
  const { token } = useContext(TokenContext);
  const [deposit, setDeposit] = useState(0);

  if (!isCashAdmin) { return <></> }

  function depositTransaction() {
    fetch(
      `${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/bank`,
      {
        method: "POST",
        headers: new Headers({ Authorization: token, "content-type": "application/json" }),
        body: JSON.stringify({userID: userID, deposit: deposit}),
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
      <input type="number" id={`deposit-${userID}`} onChange={(e) => setDeposit(Number(e.target.value))}/>
      <button type="button" onClick={depositTransaction}>Deposit</button>
    </div>
  );
}

class MemberDto {
  identifier: string;
  personRole: PersonRole | null;
  firstName: string | null;
  lastName: string | null;
  nickname: string | null;

  public constructor(
    identifier: string,
    personRole: PersonRole | null,
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
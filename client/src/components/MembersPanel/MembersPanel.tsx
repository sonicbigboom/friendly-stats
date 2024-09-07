import { useContext, useEffect, useState } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import Member, { PersonRole } from "../../classes/Member";

type Props = {
  groupID: number;
  isCashAdmin: boolean;
};

export default function MembersPanel( { groupID, isCashAdmin }: Props) {
  const token = useContext(TokenContext);
  const [members, setMembers] = useState<Member[]>([]);
  const [newMember, setNewMember] = useState<MemberDto>(new MemberDto("", PersonRole.Player, "", "", ""));
  
  const listMembers = members.map(member => {
    if (member.personID >= 0 && member.clubID >= 0) {

      return (   
        <li key={member.personID}>
          <p>{member.firstName} {member.lastName} ({member.nickname}) cashBalance: {member.cashBalance}</p>
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
    fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/users`, {
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
  }, [token, members]);

  async function addMember() {
    fetch(
      `http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/users`,
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
        setMembers([...members, new Member(-1, -1, PersonRole.Player, 0, newMember.firstName, newMember.lastName, newMember.nickname)])
      }
    );
  }

  return (
    <div>
      <h3>Members:</h3>
      {listMembers}
      <div>
        <h4>Add Member</h4>
        <label>Email</label>
        <input type="text" onChange={(e) => setNewMember(member => {
          const m = member.clone();
          m.identifier = e.target.value;
          return m;
        })} />
        <label>First Name</label>
        <input type="text" onChange={(e) => setNewMember(member => {
          const m = member.clone();
          m.firstName = e.target.value;
          return m;
        })} />
        <label>Last Name</label>
        <input type="text" onChange={(e) => setNewMember(member => {
          const m = member.clone();
          m.lastName = e.target.value;
          return m;
        })} />
        <label>Nickname</label>
        <input type="text" onChange={(e) => setNewMember(member => {
          const m = member.clone();
          m.nickname = e.target.value;
          return m;
        })} />
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

export function BankTransaction({ groupID, userID, isCashAdmin }: BankTransactionProps) {
  const token = useContext(TokenContext);
  const [deposit, setDeposit] = useState(0);

  if (!isCashAdmin) { return <></> }

  function depositTransaction() {
    fetch(
      `http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/groups/${groupID}/bank`,
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
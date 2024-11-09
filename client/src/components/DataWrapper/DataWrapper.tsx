import { ReactNode } from "react";
import GroupsContextWrapper from "../../data/Groups/GroupsContext";
import UserContextWrapper from "../../data/User/UserContext";
import MembersContextWrapper from "../../data/Members/MembersContext";

type Props = {
  children: ReactNode;
};

export default function DataWrapper({ children }: Readonly<Props>) {
  return (
    <UserContextWrapper>
      <GroupsContextWrapper>
        <MembersContextWrapper>
          {children}
        </MembersContextWrapper>
      </GroupsContextWrapper>
    </UserContextWrapper>
  );
}

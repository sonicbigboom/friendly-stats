import { ReactNode } from "react";
import GroupsContextWrapper from "../../data/Groups/GroupsContext";
import UserContextWrapper from "../../data/User/UserContext";

type Props = {
  children: ReactNode;
};

export default function DataWrapper({ children }: Readonly<Props>) {
  return (
    <UserContextWrapper>
      <GroupsContextWrapper>
        {children}
      </GroupsContextWrapper>
    </UserContextWrapper>
  );
}

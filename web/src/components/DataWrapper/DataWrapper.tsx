import { ReactNode } from "react";
import GroupsContextWrapper from "../../data/Groups/GroupsContext";
import UserContextWrapper from "../../data/User/UserContext";
import MembersContextWrapper from "../../data/Members/MembersContext";
import GamesContextWrapper from "../../data/Games/GamesContext";
import PlayersContextWrapper from "../../data/Players/PlayersContext";

type Props = {
  children: ReactNode;
};

export default function DataWrapper({ children }: Readonly<Props>) {
  return (
    <UserContextWrapper>
      <GroupsContextWrapper>
        <MembersContextWrapper>
          <GamesContextWrapper>
            <PlayersContextWrapper>
            {children}
            </PlayersContextWrapper>
          </GamesContextWrapper>
        </MembersContextWrapper>
      </GroupsContextWrapper>
    </UserContextWrapper>
  );
}

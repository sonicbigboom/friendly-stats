import { ReactNode, useContext, useMemo, useState } from "react";
import { UserContext } from "./UserContext";
import User from "../../classes/User";
import { TokenContext } from "../Token/TokenContext";

type Props = { children: ReactNode }

export default function UserContextWrapper({ children }: Readonly<Props>) {
  const { token } = useContext(TokenContext);
  const [user, setUser] = useState(new User());

  function refresh() {
    fetch(`${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/me`, {
      method: "GET",
      headers: new Headers({ Authorization: token }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response.status;
      }

      const json = await response.json();

      setUser(json);
    });
  }

  if (user.id === -1) {
    refresh();
  }

  const provider = useMemo(() => ({user: user, refresh: refresh}), [user])

  return (
    <UserContext.Provider value={provider}>
    {children}
    </UserContext.Provider>
  )
}
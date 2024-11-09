import { ReactNode } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { BrowserRouter } from "react-router-dom";

type Props = {
  children: ReactNode;
  token: string;
  setToken: (token: null | string) => void;
};

export default function Wrapper({ children, token, setToken }: Props) {
  const clientId = process.env.REACT_APP_FRIENDLY_STATS_GOOGLE_CLIENT_ID;
  if (clientId === undefined) {
    throw clientId;
  }

  return (
    <TokenContext.Provider value={{token: token, setToken: setToken}}>
      <GoogleOAuthProvider clientId={clientId}>
        <BrowserRouter>{children}</BrowserRouter>
      </GoogleOAuthProvider>
    </TokenContext.Provider>
  );
}

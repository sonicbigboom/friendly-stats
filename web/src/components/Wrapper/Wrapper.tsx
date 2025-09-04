import { ReactNode, useMemo } from "react";
import { TokenContext } from "../../data/Token/TokenContext";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { BrowserRouter } from "react-router-dom";

type Props = {
  children: ReactNode;
  token: string;
  setToken: (token: null | string) => void;
};

export default function Wrapper({ children, token, setToken }: Readonly<Props>) {
  const clientId = process.env.REACT_APP_FRIENDLY_STATS_GOOGLE_CLIENT_ID;
  if (clientId === undefined) {
    throw clientId;
  }

  const tokenProvider = useMemo(() => ({token: token, setToken: setToken}), [token])

  return (
    <TokenContext.Provider value={tokenProvider}>
      <GoogleOAuthProvider clientId={clientId}>
        <BrowserRouter>{children}</BrowserRouter>
      </GoogleOAuthProvider>
    </TokenContext.Provider>
  );
}

import { ReactNode, useContext } from 'react';
import { TokenContext } from '../Token/TokenContext';
import { GoogleOAuthProvider } from '@react-oauth/google';

type Props = {
  children: ReactNode
  token: string
}

export default function Wrapper({children, token}: Props) {
  const clientId = process.env.REACT_APP_FRIENDLY_STATS_GOOGLE_CLIENT_ID;
  if (clientId === undefined) {
    throw clientId
  }

  return (
    <TokenContext.Provider value={token}>
      <GoogleOAuthProvider clientId={clientId}>
        {children}
      </GoogleOAuthProvider>
    </TokenContext.Provider>
  )
}
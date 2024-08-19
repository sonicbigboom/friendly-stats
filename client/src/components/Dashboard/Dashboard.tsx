import React, { useContext } from 'react';
import { TokenContext } from '../Token/TokenContext';

export default function Dashboard() {
  const token = useContext(TokenContext);

  return(
    <h2>
        Dashboard, Token: {token}
    </h2>
  );
}
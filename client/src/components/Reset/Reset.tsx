import React, { ChangeEvent, useState } from 'react';
import { GoogleLogin } from '@react-oauth/google';
import './Reset.css';
import Dropdown, { Option } from 'react-dropdown';
import 'react-dropdown/style.css';

async function resetUser(email:string, token: string, authType: string, code: string) {
  return fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/reset`, {
    method: 'POST',
    headers: new Headers({ 'content-type': 'application/json' }),
    body: JSON.stringify({email, token, authType, code })
  })
    .then(response => {
      if (!response.ok) { throw response }
    });
}

async function sendToken(email: string) {
  return fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/reset?email=` + email, {
    method: 'GET'
  })
    .then(response => {
      if (!response.ok) { throw response }
    });
}

const authTypes = [
  'basic', 'google'
];

export default function Register() {
  const [email, setEmail] = useState<string>("");
  const [token, setToken] = useState<string>("");
  const [authType, setAuthType] = useState<string>(authTypes[0]);
  const [code, setCode] = useState<string>("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    resetUser(email, token, authType, code);
  }

  const handleSendToken = async (e: React.FormEvent) => {
    sendToken(email);
  }

  function handleEmailChange(event: ChangeEvent) {
    const element = event.target as HTMLInputElement;
    setEmail(element.value);
  };

  function handleTokenChange(event: ChangeEvent) {
    const element = event.target as HTMLInputElement;
    setToken(element.value);
  }

  function handleAuthTypeChange(option: Option) {
    setAuthType(option.value);
  }

  return (
    <div className="reset-wrapper">
      <h1>Create an account!</h1>
      <form onSubmit={handleSubmit}>
        <label>
          <p>Email</p>
          <input type="text" name="email" value={email} onChange={handleEmailChange} />
          <button type="button" onClick={handleSendToken}>Send Token</button>
        </label>
        <label>
          <p>Token</p>
          <input type="text" name="token" value={token} onChange={handleTokenChange} />
        </label>
        <label>
          <h3>Credentials Method</h3>
          <Dropdown options={authTypes} onChange={handleAuthTypeChange} value={authType} placeholder="Select a authentication type." />
          <CredentialMechanism authType={authType} setCode={setCode} />
        </label>
        <div>
          <button type="submit">Submit</button>
        </div>
      </form>
    </div>
  )
}

interface CredentialMechanismProps {
  authType: string,
  setCode: ((value: string) => void)
}

function CredentialMechanism({ authType, setCode }: CredentialMechanismProps) {
  function handleCodeChange(event: ChangeEvent) {
    const { name, value }: any = event.target;
    setCode(value);
  };

  if (authType === "basic") {
    return (
      <label>
        <p>Password</p>
        <input type="password" name="code" onChange={handleCodeChange} />
      </label>
    );
  } else if (authType === "google") {
    return <GoogleLogin
      onSuccess={async credentialResponse => {
        if (credentialResponse.credential === undefined) {
          throw credentialResponse;
        }

        setCode(credentialResponse.credential);
      }}
      onError={() => {
        console.log('Login Failed');
      }} />
  }
  return <></>
}
import React, { ChangeEvent, useState } from 'react';
import { GoogleLogin } from '@react-oauth/google';
import './Register.css';
import Dropdown, { Option } from 'react-dropdown';
import 'react-dropdown/style.css';

async function registerUser(userInfo: UserInfo, authType: string, code: string) {
  return fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/register`, {
    method: 'POST',
    headers: new Headers({ 'content-type': 'application/json' }),
    body: JSON.stringify({ ...userInfo, authType, code })
  })
    .then(response => {
      if (!response.ok) { throw response }
    });
}

interface UserInfo {
  username: string,
  email: string,
  firstName: string,
  lastName: string,
  nickname: string
}

const authTypes = [
  'basic', 'google'
];

export default function Register() {
  const [userInfo, setUserInfo] = useState<UserInfo>({ username: "", email: "", firstName: "", lastName: "", nickname: "" });
  const [authType, setAuthType] = useState<string>(authTypes[0])
  const [code, setCode] = useState<string>("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    /* const token: { accessToken: string, tokenType: string } = await */ registerUser(userInfo, authType, code);
    // setToken(`${token.tokenType} ${token.accessToken}`);
  }

  function handleUserInfoChange(event: ChangeEvent) {
    const { name, value }: any = event.target;
    setUserInfo((userInfo) => ({ ...userInfo, [name]: value }));
  };

  function handleAuthTypeChange(option: Option) {
    setAuthType(option.value);
  }

  return (
    <div className="login-wrapper">
      <h1>Create an account!</h1>
      <form onSubmit={handleSubmit}>
        <label>
          <p>Username</p>
          <input type="text" name="username" value={userInfo.username} onChange={handleUserInfoChange} />
        </label>
        <label>
          <p>Email</p>
          <input type="text" name="email" value={userInfo.email} onChange={handleUserInfoChange} />
        </label>
        <label>
          <p>First Name</p>
          <input type="text" name="firstName" value={userInfo.firstName} onChange={handleUserInfoChange} />
        </label>
        <label>
          <p>Last Name</p>
          <input type="text" name="lastName" value={userInfo.lastName} onChange={handleUserInfoChange} />
        </label>
        <label>
          <p>Nickname</p>
          <input type="text" name="nickname" value={userInfo.nickname} onChange={handleUserInfoChange} />
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
import React, { useState } from 'react';
import './Login.css';
import PropTypes from 'prop-types';
import { GoogleLogin } from '@react-oauth/google';
import Register from '../Register/Register';

async function loginUser(credentials: { loginName: string; code: string; authType: string; }) {
  return fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/login`, {
    method: 'POST',
    headers: new Headers({ 'content-type': 'application/json' }),
    body: JSON.stringify(credentials)
  })
    .then(response => {
      if (!response.ok) { throw response }
      return response.json()
    });
}

interface LoginProps {
  setToken: (token: string) => void;
}

const Login: React.FC<LoginProps> = ({ setToken }) => {
  const [isRegistering, setIsRegistering] = useState(false)
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");

  function flipIsRegistering() {
    setIsRegistering(isRegistering => !isRegistering)
  }

  if (isRegistering) {
    return (
      <div className="login-wrapper">
        <Register/>
        <br/>
        <button onClick={flipIsRegistering}> Login </button>
      </div>
    );
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const token: { accessToken: string, tokenType: string } = await loginUser({
      "loginName": username,
      "code": password,
      "authType": "basic"
    });
    setToken(`${token.tokenType} ${token.accessToken}`);
  }

  return (
    <div className="login-wrapper">
      <h1>Please Log In</h1>
      <form onSubmit={handleSubmit}>
        <label>
          <p>Username</p>
          <input type="text" onChange={e => setUserName(e.target.value)} />
        </label>
        <label>
          <p>Password</p>
          <input type="password" onChange={e => setPassword(e.target.value)} />
        </label>
        <div>
          <button type="submit">Submit</button>
        </div>
      </form>
      <GoogleLogin
        onSuccess={async credentialResponse => {
          if (credentialResponse.credential === undefined) {
            throw credentialResponse;
          }

          const token: { accessToken: string, tokenType: string } = await loginUser({
            loginName: "",
            code: credentialResponse.credential,
            authType: "google"
          })
          setToken(`${token.tokenType} ${token.accessToken}`);
        }}
        onError={() => {
          console.log('Login Failed');
        }}
      />
      <br/>
      <button onClick={flipIsRegistering}> Register </button>
    </div>
  )
}

Login.propTypes = {
  setToken: PropTypes.func.isRequired
}

export default Login;
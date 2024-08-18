import React, { useState } from 'react';
import './Login.css';
import PropTypes from 'prop-types';


async function loginUser(credentials: { username: string; password: string; }) {
  return fetch(`http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/login?` + new URLSearchParams({
      username: credentials.username,
      password: credentials.password
  }).toString(), {
    method: 'POST',
  })
  .then( response => {
    if (!response.ok) { throw response }
    return response.json()} );
}

interface LoginProps {
  setToken: React.Dispatch<React.SetStateAction<undefined>>;
}

const Login: React.FC<LoginProps> = ({ setToken }) => {
  const [username, setUserName] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = await loginUser({
      username,
      password
    });
    setToken(token);
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
    </div>
  )
}

Login.propTypes = {
  setToken: PropTypes.func.isRequired
}

export default Login;
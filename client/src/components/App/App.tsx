import React, { useState } from 'react';
import useToken from '../Token/useToken';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from '../Login/Login';
import Dashboard from '../Dashboard/Dashboard';
import Wrapper from '../Wrapper/Wrapper';


function App() {
  const { token, setToken } = useToken();

  if (!token) {
    return (
      <Wrapper token={token}>
        <Login setToken={setToken} />
      </Wrapper>
    )
  }

  function logout() {
    setToken(null);
  }

  return (
    <Wrapper token={token}>
      <div className="wrapper">
        <h1>Friendly Stats</h1>
        <BrowserRouter>
          <Routes>
            <Route path="/dashboard" element={<Dashboard />} />
          </Routes>
        </BrowserRouter>
        <button onClick={logout}>Logout</button>
      </div>
    </Wrapper>
  );
}

export default App;

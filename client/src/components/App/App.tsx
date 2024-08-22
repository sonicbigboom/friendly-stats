import React, { useState } from 'react';
import useToken from '../Token/useToken';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from '../Login/Login';
import Dashboard from '../Dashboard/Dashboard';
import Wrapper from '../Wrapper/Wrapper';
import Verify from '../Verify/Verify';
import Reset from '../Reset/Reset';
import Register from '../Register/Register';


function App() {
  const { token, setToken } = useToken();

  function logout() {
    setToken(null);
  }

  if (!token) {
    return (
      <Wrapper token={token}>
        <Routes>
          <Route path="/login" element={<Login setToken={setToken}/>} />
          <Route path="/register" element={<Register />} />
          <Route path="/reset" element={<Reset />} />
          <Route path="/verify" element={<Verify />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </Wrapper>
    )
  }

  return (
    <Wrapper token={token}>
      <div className="wrapper">
        <h1>Friendly Stats</h1>
        <Routes>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
        <button onClick={logout}>Logout</button>
      </div>
    </Wrapper>
  );
}

export default App;

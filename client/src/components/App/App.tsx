import React, { useState } from 'react';
import logo from './logo.svg';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from '../Login/Login';
import Dashboard from '../Dashboard/Dashboard';

function App() {
  const [token, setToken] = useState<string>();

  if(!token) {
    return <Login setToken={setToken} />
  }

  return (
    <div className="wrapper">
      <h1>Application</h1>
      <BrowserRouter>
        <Routes>
          <Route path="/dashboard" element={<Dashboard/>} />
        </Routes>
      </BrowserRouter>
      <h2>Token: {token}</h2>
    </div>
  );
}

export default App;

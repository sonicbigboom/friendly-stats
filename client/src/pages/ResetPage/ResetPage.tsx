import React, { useState } from "react";
import "./ResetPage.css";
import Dropdown from "react-dropdown";
import "react-dropdown/style.css";
import { useNavigate } from "react-router-dom";
import { CredentialMechanism } from "../RegisterPage/RegisterPage";

async function resetUser(
  email: string,
  token: string,
  authType: string,
  code: string
) {
  return fetch(
    `http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/reset`,
    {
      method: "POST",
      headers: new Headers({ "content-type": "application/json" }),
      body: JSON.stringify({ email, token, authType, code }),
    }
  ).then((response) => {
    if (!response.ok) {
      throw response;
    }
  });
}

async function sendToken(email: string) {
  return fetch(
    `http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/reset?email=` +
      email,
    {
      method: "GET",
    }
  ).then((response) => {
    if (!response.ok) {
      throw response;
    }
  });
}

const authTypes = ["basic", "google"];

export default function ResetPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState<string>("");
  const [token, setToken] = useState<string>("");
  const [authType, setAuthType] = useState<string>(authTypes[0]);
  const [code, setCode] = useState<string>("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    resetUser(email, token, authType, code);
  };

  return (
    <div className="reset-wrapper">
      <h1>Create an account!</h1>
      <form onSubmit={handleSubmit}>
        <label>
          <p>Email</p>
          <input
            type="text"
            name="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button type="button" onClick={() => sendToken(email)}>
            Send Token
          </button>
        </label>
        <label>
          <p>Token</p>
          <input
            type="text"
            name="token"
            value={token}
            onChange={(e) => setToken(e.target.value)}
          />
        </label>
        <label>
          <h3>Credentials Method</h3>
          <Dropdown
            options={authTypes}
            onChange={(option) => setAuthType(option.value)}
            value={authType}
            placeholder="Select a authentication type."
          />
          <CredentialMechanism authType={authType} setCode={setCode} />
        </label>
        <div>
          <button type="submit">Submit</button>
        </div>
      </form>
      <br />
      <button name="login" onClick={() => navigate("/login")}>
        {" "}
        Login{" "}
      </button>
    </div>
  );
}

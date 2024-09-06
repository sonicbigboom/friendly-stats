import React, { ChangeEvent, useState } from "react";
import { GoogleLogin } from "@react-oauth/google";
import "./RegisterPage.css";
import Dropdown from "react-dropdown";
import "react-dropdown/style.css";
import { useNavigate } from "react-router-dom";

async function registerUser(
  userInfo: UserInfo,
  authType: string,
  code: string
) {
  return fetch(
    `http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/register?verificationUrl=http://${process.env.REACT_APP_FRIENDLY_STATS_CLIENT_HOST}/verify?token=`,
    {
      method: "POST",
      headers: new Headers({ "content-type": "application/json" }),
      body: JSON.stringify({ ...userInfo, authType, code }),
    }
  ).then((response) => {
    if (!response.ok) {
      throw response.status;
    }
  });
}

interface UserInfo {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  nickname: string;
}

const authTypes = ["basic", "google"];

export default function RegisterPage() {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState<UserInfo>({
    username: "",
    email: "",
    firstName: "",
    lastName: "",
    nickname: "",
  });
  const [authType, setAuthType] = useState<string>(authTypes[0]);
  const [code, setCode] = useState<string>("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    /* const token: { accessToken: string, tokenType: string } = await */ registerUser(
      userInfo,
      authType,
      code
    );
    // setToken(`${token.tokenType} ${token.accessToken}`);
  };

  function handleUserInfoChange(event: ChangeEvent) {
    const { name, value }: any = event.target;
    setUserInfo((userInfo) => ({ ...userInfo, [name]: value }));
  }

  return (
    <div className="register-wrapper">
      <h1>Create an account!</h1>
      <form onSubmit={handleSubmit}>
        <label>
          <p>Username</p>
          <input
            type="text"
            name="username"
            value={userInfo.username}
            onChange={handleUserInfoChange}
          />
        </label>
        <label>
          <p>Email</p>
          <input
            type="text"
            name="email"
            value={userInfo.email}
            onChange={handleUserInfoChange}
          />
        </label>
        <label>
          <p>First Name</p>
          <input
            type="text"
            name="firstName"
            value={userInfo.firstName}
            onChange={handleUserInfoChange}
          />
        </label>
        <label>
          <p>Last Name</p>
          <input
            type="text"
            name="lastName"
            value={userInfo.lastName}
            onChange={handleUserInfoChange}
          />
        </label>
        <label>
          <p>Nickname</p>
          <input
            type="text"
            name="nickname"
            value={userInfo.nickname}
            onChange={handleUserInfoChange}
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

interface CredentialMechanismProps {
  authType: string;
  setCode: (value: string) => void;
}

export function CredentialMechanism({
  authType,
  setCode,
}: CredentialMechanismProps) {
  if (authType === "basic") {
    return (
      <label>
        <p>Password</p>
        <input
          type="password"
          name="code"
          onChange={(e) => setCode(e.target.value)}
        />
      </label>
    );
  } else if (authType === "google") {
    return (
      <GoogleLogin
        onSuccess={async (credentialResponse) => {
          if (credentialResponse.credential === undefined) {
            throw credentialResponse;
          }

          setCode(credentialResponse.credential);
        }}
        onError={() => {
          console.log("Login Failed");
        }}
      />
    );
  }
  return <></>;
}

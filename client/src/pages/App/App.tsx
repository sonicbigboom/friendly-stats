import useToken from "../../data/Token/useToken";
import { Link, Navigate, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "../LoginPage/LoginPage";
import DashboardPage from "../DashboardPage/DashboardPage";
import Wrapper from "../../components/Wrapper/Wrapper";
import VerifyPage from "../VerifyPage/VerifyPage";
import RegisterPage from "../RegisterPage/RegisterPage";
import ResetPage from "../ResetPage/ResetPage";
import GroupPage from "../GroupPage/GroupPage";
import GamePage from "../GamePage/GamePage";

function App() {
  const { token, setToken } = useToken();

  function logout() {
    setToken(null);
  }

  if (!token) {
    return (
      <Wrapper token={token}>
        <Routes>
          <Route path="/login" element={<LoginPage setToken={setToken} />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/reset" element={<ResetPage />} />
          <Route path="/verify" element={<VerifyPage />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </Wrapper>
    );
  }

  return (
    <Wrapper token={token}>
      <div className="wrapper">
        <Link to="/dashboard"><h1>Friendly Stats</h1></Link>
        <Routes>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/group/:groupID" element={<GroupPage />} />
          <Route path="/game/:gameID" element={<GamePage />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
        <button onClick={logout}>Logout</button>
      </div>
    </Wrapper>
  );
}

export default App;

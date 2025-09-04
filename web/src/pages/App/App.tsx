import useToken from "../../data/Token/useToken";
import { Navigate, Route, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "../LoginPage/LoginPage";
import DashboardPage from "../DashboardPage/DashboardPage";
import Wrapper from "../../components/Wrapper/Wrapper";
import VerifyPage from "../VerifyPage/VerifyPage";
import RegisterPage from "../RegisterPage/RegisterPage";
import ResetPage from "../ResetPage/ResetPage";
import GroupPage from "../GroupPage/GroupPage";
import GamePage from "../GamePage/GamePage";
import FSNavbar from "../../components/FSNavbar/FSNavbar";
import 'bootstrap/dist/css/bootstrap.min.css';
import DataWrapper from "../../components/DataWrapper/DataWrapper";
import ScoreboardPage from "../ScoreboardPage/ScoreboardPage";

function App() {
  const { token, setToken } = useToken();

  if (!token) {
    return (
      <Wrapper token={token} setToken={setToken}>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/reset" element={<ResetPage />} />
          <Route path="/verify" element={<VerifyPage />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </Wrapper>
    );
  }

  return (
    <Wrapper token={token} setToken={setToken}>
      <DataWrapper>
        <FSNavbar />
        <Routes>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/group/:groupId" element={<GroupPage />} />
          <Route path="/group/:groupId/scoreboard" element={<ScoreboardPage />} />
          <Route path="/group/:groupId/game/:gameId" element={<GamePage />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </DataWrapper>
    </Wrapper>
  );
}

export default App;

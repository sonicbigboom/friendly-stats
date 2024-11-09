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
import FSNavbar from "../../components/FSNavbar/FSNavbar";
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  const { token, setToken } = useToken();

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
      <FSNavbar setToken={setToken}/>
      <div className="wrapper">
        <Routes>
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/group/:groupID" element={<GroupPage />} />
          <Route path="/group/:groupID/game/:gameID" element={<GamePage />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </div>
    </Wrapper>
  );
}

export default App;

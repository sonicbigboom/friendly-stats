import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";

export default function VerifyPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [status, setStatus] = useState("Verifying...");

  async function verifyUser(token: string) {
    return fetch(
      `http://${process.env.REACT_APP_FRIENDLY_STATS_SERVER_HOST}/auth/verify?token=${token}`,
      {
        method: "GET",
      }
    ).then((response) => {
      if (!response.ok) {
        setStatus("Verification failed :(");
        throw response.status;
      }
      setStatus("User was verified!");
    });
  }

  useEffect(() => {
    const token = searchParams.get("token");
    if (token === null) {
      throw token;
    }
    verifyUser(token);
  }, []);

  return <p>{status}</p>;
}

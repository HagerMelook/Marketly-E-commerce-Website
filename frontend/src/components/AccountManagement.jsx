import { useState } from "react";
import AccountCard from "./AccountCard";
import "./AccountManagement.css";

function AccountManagement() {
  const [username, setUsername] = useState("");
  const [account, setAccount] = useState(null);
  const [msg, setMsg] = useState("");
  const [resetCardMsg, setResetCardMsg] = useState(false);

  const resetMsg = () => setMsg("");

  const handleSearch = async () => {
    resetMsg();
    setResetCardMsg(true); // Trigger AccountCard to reset msg
    if (!username.trim()) {
      setMsg("Please enter a username to search.");
      setAccount(null);
      return;
    }

    try {
      const result = await fetch(
        `http://localhost:8080/admin/info/${username}`,
        {
          method: "GET",
        }
      );

      if (!result.ok) {
        setMsg("Account not found.");
        setAccount(null);
        return;
      }

      const data = await result.json();
      setAccount({
        username: data.username,
        status: data.active ? "active" : "inactive",
        role: data.type,
        id: data.id,
      });
    } catch (error) {
      console.error("Error fetching data:", error);
      setMsg("Failed to fetch data. Please try again later.");
    } finally {
      setResetCardMsg(false); // Reset back after action
    }
  };

  return (
    <div className="account-management">
      <div className="search-section">
        <h2>Search User Account</h2>
        <div>
          <input
            type="text"
            placeholder="Search by Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            onFocus={resetMsg}
          />
        </div>
        {msg && <span className="message">{msg}</span>}
        <button onClick={handleSearch}>Search</button>
      </div>
      {account && <AccountCard account={account} resetMsg={resetCardMsg} />}
    </div>
  );
}

export default AccountManagement;

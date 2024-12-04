import { useState } from "react";
import AccountCard from "./AccountCard";
import "./AccountManagement.css";

function AccountManagement() {
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [account, setAccount] = useState(null);
  const [msg, setMsg] = useState("");

  const handleSearch = async () => {
    setMsg("");
    if (!email && !username) {
      setMsg("Please enter email or username to search.");
      setAccount(null);
      return;
    }

    try {
      let result;
      if (email) {
        result = await fetch(
          `http://localhost:8080/admin/info?email=${email}`,
          {
            method: "GET",
          }
        );
      } else {
        result = await fetch(`http://localhost:8080/admin/info/${username}`, {
          method: "GET",
        });
      }

      if (!result.ok) {
        setMsg("Account not found.");
        setAccount(null);
        return;
      }

      const data = await result.json();
      console.log(data);
      setAccount({
        email: data.email,
        username: data.username,
        status: data.active ? "active" : "inactive",
        role: data.type,
        id: data.id,
      });
    } catch (error) {
      console.error("Error fetching data:", error);
      setMsg("Failed to fetch data. Please try again later.");
    }
  };

  return (
    <div className="account-management">
      <div className="search-section">
        <h2>Search User Account</h2>
        <div>
          <input
            type="text"
            placeholder="Search by Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div>
          <input
            type="text"
            placeholder="Search by Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        {msg && <span className="message">{msg}</span>}
        <button onClick={handleSearch}>Search</button>
      </div>
      {account && <AccountCard account={account} />}
    </div>
  );
}

export default AccountManagement;

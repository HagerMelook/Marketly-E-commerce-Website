import PropTypes from "prop-types";
import { useState } from "react";

function AccountCard({ account }) {
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);

  const handleAction = async (action) => {
    setMsg("");
    setLoading(true);

    try {
      let result;
      const urlMap = {
        activate: `http://localhost:8080/admin/activate/${account.id}`,
        deactivate: `http://localhost:8080/admin/deactivate/${account.id}`,
        remove: `http://localhost:8080/admin/delete/${account.id}`,
        promote: `http://localhost:8080/admin/promote/${account.id}`,
        demote: `http://localhost:8080/admin/demote/${account.id}`,
      };

      if (!urlMap[action]) {
        setMsg("Invalid action!");
        return;
      }

      const method = action === "remove" ? "DELETE" : "PUT";
      result = await fetch(urlMap[action], { method });

      if (!result.ok) {
        setMsg(`Failed to ${action} account: ${result.statusText || "Error"}`);
        return;
      }

      setMsg(`Account ${action}d successfully!`);
    } catch (error) {
      setMsg(`Error: ${error.message || "Unexpected error occurred"}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="account-card">
      <h3>Account Info</h3>
      <p>
        <strong>Email:</strong> {account.email}
      </p>
      <p>
        <strong>Username:</strong> {account.username}
      </p>
      <p>
        <strong>Status:</strong> {account.status}
      </p>
      <p>
        <strong>Role:</strong> {account.role}
      </p>
      {msg && (
        <p
          className={`action-msg ${
            msg.includes("Failed") ? "error" : "success"
          }`}
        >
          {msg}
        </p>
      )}
      <div className="action-buttons">
        {["activate", "deactivate", "remove", "promote", "demote"].map(
          (action) => (
            <button
              key={action}
              onClick={() => handleAction(action)}
              disabled={loading}
            >
              {action.charAt(0).toUpperCase() + action.slice(1)}
            </button>
          )
        )}
      </div>
    </div>
  );
}

AccountCard.propTypes = {
  account: PropTypes.shape({
    email: PropTypes.string,
    username: PropTypes.string,
    status: PropTypes.string,
    role: PropTypes.string,
    id: PropTypes.number.isRequired,
  }).isRequired,
};

export default AccountCard;

import PropTypes from "prop-types";
import { useState, useEffect } from "react";

function AccountCard({ account, resetMsg }) {
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);

  const resetLocalMsg = () => setMsg("");

  // Reset the message when the `resetMsg` prop changes
  useEffect(() => {
    if (resetMsg) {
      resetLocalMsg();
    }
  }, [resetMsg]);

  const handleAction = async (action) => {
    resetLocalMsg();
    setLoading(true);

    try {
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
      const result = await fetch(urlMap[action], { method });

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
              {loading && <span className="spinner"></span>}
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
    username: PropTypes.string,
    status: PropTypes.string,
    role: PropTypes.string,
    id: PropTypes.number.isRequired,
  }).isRequired,
  resetMsg: PropTypes.bool.isRequired,
};

export default AccountCard;

import { useState } from "react";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import AccountManagement from "../components/AccountManagement";
import "./AdminDashboard.css";

function AdminDashboard() {
  const [index, setIndex] = useState(1);

  const renderContent = () => {
    switch (index) {
      case 1:
        return <AccountManagement />;
      case 2:
        return <h1>Preview Users Feedback</h1>;
      case 3:
        return <h1>Vendor Requests</h1>;
      default:
        return <h1>Welcome to the Admin Dashboard</h1>;
    }
  };

  return (
    <div className="admin-dashboard">
      <Header isAdmin={true} />
      <div className="dashboard-body">
        <Sidebar handleOnClick={setIndex} activeIndex={index} />
        <div className="content">{renderContent()}</div>
      </div>
    </div>
  );
}

export default AdminDashboard;

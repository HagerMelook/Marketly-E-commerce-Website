import { useState } from "react";
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import AccountManagement from "../components/AccountManagement";
import FeedbackPage from "../components/FeedbackPage";
import VendorRequestsPage from "../components/VendorRequestsPage";
import "./AdminDashboard.css";

function AdminDashboard() {
  const [index, setIndex] = useState(1);

  const renderContent = () => {
    switch (index) {
      case 1:
        return <AccountManagement />;
      case 2:
        return <FeedbackPage />;
      case 3:
        return <VendorRequestsPage />;
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

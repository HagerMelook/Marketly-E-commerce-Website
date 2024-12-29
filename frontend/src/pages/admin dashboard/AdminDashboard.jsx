import { useState } from "react";
import Header from "../../components/header/Header";
import Sidebar from "../../components/sidebar/Sidebar";
import AccountManagement from "../../components/account management/AccountManagement";
import FeedbackPage from "../../components/feedback/FeedbackPage";
import VendorRequestsPage from "../../components/vendor requests/VendorRequestsPage";
import CategoryManagement from "../../components/category management/CategoryManagement";
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
      case 4:
        return <CategoryManagement />;
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
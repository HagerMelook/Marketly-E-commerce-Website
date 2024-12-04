import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AdminDashboard from "./pages/AdminDashboard";
import Home from "./pages/Home";

// import VendorRequestsPage from './components/VendorRequestsPage'

function App() {

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home isAdmin={true} />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
      </Routes>
    </Router>
  );
}

export default App;

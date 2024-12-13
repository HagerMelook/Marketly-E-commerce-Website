import RadialChoice from "./pages/RadialChoice/RadialChoice";
import Home from "./pages/Home/Home";
import ClientReg from "./pages/ClientReg/ClientReg";
import VendorReg from "./pages/VendorReg/VendorReg";
import AdminDashboard from "./pages/AdminDashboard/AdminDashboard";
import { Routes, Route, Navigate, BrowserRouter } from "react-router-dom";

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index element={<RadialChoice />} />
        <Route path="home" element={<Home isAdmin={false} />} />
        <Route path="clientSignUp" element={<ClientReg />} />
        <Route path="vendorSignUp" element={<VendorReg />} />
        <Route path="admin-dashboard" element={<AdminDashboard />} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;

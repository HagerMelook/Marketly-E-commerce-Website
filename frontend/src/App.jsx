import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminDashboard from "./pages/admin dashboard/AdminDashboard";
import ClientReg from "./pages/registeration/ClientReg";
import Home from "./pages/home/Home";
import RadialChoice from "./pages/registeration/RadialChoice";
import VendorReg from "./pages/registeration/VendorReg";
import Login from "./pages/logIn/LogIn";

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index element={<RadialChoice />} />
        <Route path="/login" element={<Login />} />
        <Route path="clientSignUp" element={<ClientReg />} />
        <Route path="vendorSignUp" element={<VendorReg />} />
        <Route path="home" element={<Home isAdmin={false} />} />
        <Route path="admin-dashboard" element={<AdminDashboard />} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;

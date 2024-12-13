import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminDashboard from "./pages/AdminDashboard";
import ClientReg from "./pages/ClientReg/ClientReg";
import Home from "./pages/Home";
import RadialChoice from "./pages/RadialChoice/RadialChoice";
import VendorReg from "./pages/VendorReg/VendorReg";

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index element={<RadialChoice />}/>
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

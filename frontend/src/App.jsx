import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminDashboard from "./pages/admin dashboard/AdminDashboard";
import ClientReg from "./pages/registeration/ClientReg";
import Home from "./pages/home/Home";
import RadialChoice from "./pages/registeration/RadialChoice";
import VendorReg from "./pages/registeration/VendorReg";
import ProductPage from "./pages/ProductPage/ProductPage";
import EditProfile from "./components/EditProfile/EditProfile";

function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route index element={<RadialChoice />} />
        <Route path="clientSignUp" element={<ClientReg />} />
        <Route path="productPage/:productId" element={<ProductPage />} />
        <Route path="editProfile" element={<EditProfile />} />
        <Route path="vendorSignUp" element={<VendorReg />} />
        <Route path="home" element={<Home isAdmin={false} />} />
        <Route path="admin-dashboard" element={<AdminDashboard />} />
        <Route path="*" element={<Navigate to="/home" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;

import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminDashboard from "./pages/admin dashboard/AdminDashboard";
import ClientReg from "./pages/registeration/ClientReg";
import Home from "./pages/home/Home";
import RadialChoice from "./pages/registeration/RadialChoice";
import VendorReg from "./pages/registeration/VendorReg";

import Login from "./pages/logIn/LogIn";

import SearchPage from "./pages/search page/SearchPage.jsx";
import CategoryPage from "./pages/category page/CategoryPage.jsx";


function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/login" element={<Login />} />

        {/* <Route index element={<Home isVendor={true} isAdmin={false} />} /> */}
        <Route index element={<RadialChoice />} />

        <Route path="clientSignUp" element={<ClientReg />} />
        <Route path="vendorSignUp" element={<VendorReg />} />
        <Route path="home" element={<Home isAdmin={false} />} />
        <Route path="admin-dashboard" element={<AdminDashboard />} />
        <Route path="*" element={<Navigate to="/home" />} />

        <Route path="/search" element={<SearchPage />} />
        <Route path="/category" element={<CategoryPage />} />

      </Routes>
    </BrowserRouter>
  );
}

export default AppRouter;

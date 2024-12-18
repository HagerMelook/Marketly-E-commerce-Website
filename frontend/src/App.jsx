import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminDashboard from "./pages/admin dashboard/AdminDashboard";
import ClientReg from "./pages/registeration/ClientReg";
import Home from "./pages/home/Home";
import RadialChoice from "./pages/registeration/RadialChoice";
import VendorReg from "./pages/registeration/VendorReg";
import VendorInventory from "./pages/Inventory/VendorInventory";
import ProductManagementPage from "./pages/product management/ProductManagementPage";
import VendorInventory from "./pages/vendor inventory/VendorInventory";
import { ProductProvider } from "./contexts/ProductProvider";
import { AccountInfoProvider } from "./contexts/AccountInfoProvider";


function App() {
  return (
    <AccountInfoProvider>
      <BrowserRouter>
        <Routes>
          <Route index element={<RadialChoice />} />
          <Route path="clientSignUp" element={<ClientReg />} />
          <Route path="vendorSignUp" element={<VendorReg />} />
          <Route path="home" element={<Home isAdmin={false} />} />
          <Route path="admin-dashboard" element={<AdminDashboard />} />
          <Route
            path="inventory/*"
            element={
              <ProductProvider>
                <Routes>
                  <Route index element={<VendorInventory />} />
                  <Route
                    path="product-form"
                    element={<ProductManagementPage />}
                  />
                </Routes>
              </ProductProvider>
            }
          />
          <Route path="*" element={<Navigate to="/home" />} />
        </Routes>
      </BrowserRouter>
    </AccountInfoProvider>
  );
}

export default App;

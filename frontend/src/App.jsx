import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import AdminDashboard from "./pages/admin dashboard/AdminDashboard";
import ClientReg from "./pages/registeration/ClientReg";
import Home from "./pages/home/Home";
import RadialChoice from "./pages/registeration/RadialChoice";
import VendorReg from "./pages/registeration/VendorReg";
import VendorInventory from "./pages/Inventory/VendorInventory";
import ProductManagementPage from "./pages/product management/ProductManagementPage";
import { ProductProvider } from "./contexts/ProductProvider";
import SearchPage from "./pages/search page/SearchPage.jsx";
import CategoryPage from "./pages/category page/CategoryPage.jsx";



function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route index element={<RadialChoice />} />
          <Route path="clientSignUp" element={<ClientReg />} />
          <Route path="vendorSignUp" element={<VendorReg />} />
          <Route path="home" element={<Home isAdmin={false} />} />
          <Route path="admin-dashboard" element={<AdminDashboard />} />
          <Route path="*" element={<Navigate to="/home" />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/category" element={<CategoryPage />} />
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
  );
}

export default App;

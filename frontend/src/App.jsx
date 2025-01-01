import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { ProductProvider } from "./contexts/ProductProvider";
import AdminDashboard from "./pages/admin dashboard/AdminDashboard";
import Home from "./pages/home/Home";
import VendorInventory from "./pages/Inventory/VendorInventory";
import ProductManagementPage from "./pages/product management/ProductManagementPage";
import ClientReg from "./pages/registeration/ClientReg";
import RadialChoice from "./pages/registeration/RadialChoice";
import VendorReg from "./pages/registeration/VendorReg";

import Login from "./pages/logIn/LogIn";

import CategoryPage from "./pages/category page/CategoryPage.jsx";
import SearchPage from "./pages/search page/SearchPage.jsx";

import EditProfile from "./pages/EditProfile/EditProfile.jsx";
import ProductPage from "./pages/ProductPage/ProductPage";

import CheckoutComplete from "./pages/checkout complete/CheckoutComplete.jsx";
import CheckoutPage from "./pages/checkout/Checkout.jsx";

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route index element={<Login />} />
          <Route path="radialChoice" element={<RadialChoice />} />
          <Route path="clientSignUp" element={<ClientReg />} />
          <Route path="vendorSignUp" element={<VendorReg />} />
          <Route path="productPage/:productId" element={<ProductPage />} />
          <Route path="editProfile" element={<EditProfile />} />
          <Route path="home" element={<Home isAdmin={false} />} />
          <Route path="admin-dashboard" element={<AdminDashboard />} />
          <Route path="*" element={<Navigate to="/home" />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/category" element={<CategoryPage />} />
          <Route path="/checkout" element={<CheckoutPage/>}/>
          <Route path="/checkout/complete" element={<CheckoutComplete/>}/>
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

import ProductForm from "../components/ProductForm";
import Header from "../components/Header";
import "./ProductManagementPage.css";

function ProductManagementPage() {
  return (
    <div className="product-management-page">
      <Header />
      <div className="page-body">
        <ProductForm />
      </div>
    </div>
  );
}

export default ProductManagementPage;

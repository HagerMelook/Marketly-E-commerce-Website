import ProductForm from "../../components/product form/ProductForm";
import Header from "../../components/header/Header";
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

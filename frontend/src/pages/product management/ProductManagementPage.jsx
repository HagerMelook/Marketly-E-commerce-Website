import propTypes from "prop-types";
import ProductForm from "../components/ProductForm";
import Header from "../components/Header";
import "./ProductManagementPage.css";

function ProductManagementPage({ product }) {
  return (
    <div className="product-management-page">
      <Header />
      <div className="page-body">
        <ProductForm
          id={product.id}
          vendorId={product.vendorId}
          name={product.name}
          description={product.description}
          images={product.images}
          quantity={product.quantity}
          price={product.price}
          category={product.category}
        />
      </div>
    </div>
  );
}

ProductManagementPage.propTypes = {
  product: propTypes.shape({
    id: propTypes.number.isRequired,
    vendorId: propTypes.number.isRequired,
    name: propTypes.string.isRequired,
    description: propTypes.string.isRequired,
    images: propTypes.array.isRequired,
    quantity: propTypes.number.isRequired,
    price: propTypes.number.isRequired,
    category: propTypes.string.isRequired,
  }),
};

export default ProductManagementPage;

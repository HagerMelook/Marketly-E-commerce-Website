import PropTypes from "prop-types";
import Rating from "@mui/material/Rating";
import "./ProductInfo.css";

function ProductInfo({ product }) {
  return (
    <div>
      <div className="product-info-container">
        <h1 className="product-name">{product.name}</h1>
        <p className="product-vendor">
          <strong>Vendor: </strong>
          {product.organizationName}
        </p>
        <h3 className="product-price">Price: EGP {product.price}</h3>
        <div className="rating-container">
          <Rating precision={0.25} value={product.rating} readOnly />
          {product.rating.toFixed(1)}/5
        </div>
      </div>
      <div>
        {product.quantity > 0 ? (
          <h2 className="in-stock">In Stock</h2>
        ) : (
          <h2 className="out-of-stock">Out of Stock</h2>
        )}
        <button className="blue-button" disabled={product.quantity === 0}>
          Add to Cart
        </button>
      </div>
    </div>
  );
}

ProductInfo.propTypes = {
  product: PropTypes.object,
};

export default ProductInfo;

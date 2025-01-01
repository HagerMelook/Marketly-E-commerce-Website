import PropTypes from "prop-types";
import "./OrderProduct.css";

function OrderProduct({ product }) {
  return (
    <div className="order-product">
      <h3>{product.name}</h3>
      <p>Descriptio: {product.description}</p>
      <p>From: {product.vendor.organizationName}</p>
      <p>Price: {product.price}$</p>
      <p>Quentity: {product.quantity}</p>
    </div>
  );
}

OrderProduct.propTypes = {
  product: PropTypes.object,
};

export default OrderProduct;

import PropTypes from "prop-types";
import "./SmallProductCard.css";

function SmallProductCard({ product }) {
  return (
    <ul
      className="small-product-card"
      onClick={() => (window.location.href = `/product/${product.id}`)}
    >
      <h3>{product.name}</h3>
      <p>${product.price}</p>
    </ul>
  );
}

SmallProductCard.propTypes = {
  product: PropTypes.object,
};

export default SmallProductCard;

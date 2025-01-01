import PropTypes from "prop-types";
import "./OrderCard.css";
import OrderProduct from "../order product/OrderProduct";

const OrderCard = ({ order }) => {
  return (
    <div className="order-card">
      <div className="order-info">
        <div className="order-date">
          Order Placed<p className="text-info">{order.date}</p>
        </div>
        <div className="total-price">
          Total Price<p className="text-info">{order.checkoutPrice}</p>
        </div>
        <div className="status">
          Status<p className="text-info">{order.status}</p>
        </div>
        {order.status !== "Shipped" && order.status !== "Delivered" && (
          <button className="cancel-order">Cancel</button>
        )}
      </div>
      <div className="order-products-list">
        {order.products.map((product) => (
          <OrderProduct key={product.id} product={product} />
        ))}
      </div>
    </div>
  );
};

OrderCard.propTypes = {
  order: PropTypes.object.isRequired,
};

export default OrderCard;

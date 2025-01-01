import { useState, useEffect } from "react";
import Header from "../../components/header/Header";
import OrderCard from "../../components/order card/OrderCard";
import "./orders.css";

function OrdersList() {
  const [orders, setOrders] = useState([]);

  // const id = sessionStorage.getItem("id");

  useEffect(() => {
    // fetch(`http://localhost:8080/orders/${id}`, {
    fetch(`http://localhost:8080/orders/${3}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem("token")}`
      },
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setOrders(data);
      })
      .catch((error) => {
        console.error(error);
        throw error;
      });
  }, [orders]);

  return (
    <div className="orders-list">
      <Header isVendor={true} />
      <div className="cards-list">
        {orders.map((order) => (
          <OrderCard order={order} key={order.id} />
        ))}
      </div>
    </div>
  );
}

export default OrdersList;

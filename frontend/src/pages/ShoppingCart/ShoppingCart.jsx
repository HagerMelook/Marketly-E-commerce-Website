import { useEffect, useState } from "react";
import Header from "../../components/header/Header";
import { CartItem } from "./CartItem";
import Pagination from './Pagination';
import { useNavigate } from "react-router-dom";


const ITEMS_PER_PAGE = 5;

const DUMMY_CART_ITEMS = [
  {
    id: 1,
    name: "Wireless Headphones",
    price: 129.99,
    quantity: 1,
    image: "/api/placeholder/100/100",
    description: "Premium noise-canceling wireless headphones"
  },
  {
    id: 2,
    name: "Smart Watch",
    price: 249.99,
    quantity: 1,
    image: "/api/placeholder/100/100",
    description: "Fitness tracking smartwatch with heart rate monitor"
  },
  {
    id: 3,
    name: "Laptop Backpack",
    price: 79.99,
    quantity: 2,
    image: "/api/placeholder/100/100",
    description: "Water-resistant laptop backpack with USB charging port"
  },
  {
    id: 4,
    name: "Mechanical Keyboard",
    price: 159.99,
    quantity: 1,
    image: "/api/placeholder/100/100",
    description: "RGB mechanical gaming keyboard with Cherry MX switches"
  },
  {
    id: 5,
    name: "Wireless Mouse",
    price: 49.99,
    quantity: 1,
    image: "/api/placeholder/100/100",
    description: "Ergonomic wireless mouse with adjustable DPI"
  },
  {
    id: 6,
    name: "USB-C Hub",
    price: 39.99,
    quantity: 1,
    image: "/api/placeholder/100/100",
    description: "7-in-1 USB-C hub with HDMI and power delivery"
  }
];

function ShoppingCart() {
  const [currentPage, setCurrentPage] = useState(1);
  const [cartItems, setCartItems] = useState([]);
  const [cost, setCost] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCart = async () => {
      try {
        const accountId = sessionStorage.getItem("id");
        const response = await fetch(`http://localhost:8080/ShoppingCart/${accountId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          setCartItems(data.products);
          setCost(data.totalPrice);
          sessionStorage.setItem("products", JSON.stringify(data.products));
        } else {
          setCartItems(DUMMY_CART_ITEMS);
          setCost(DUMMY_CART_ITEMS.reduce((total, item) => total + item.price * item.quantity, 0));
          sessionStorage.setItem("products", JSON.stringify(DUMMY_CART_ITEMS));
        }
      } catch (error) {
        console.error("Error fetching cart items:", error);
      }
    };

    fetchCart();
  }, []);

  const handleUpdateQuantity = (id, newQuantity) => {
    setCartItems(prevItems =>
      prevItems.map(item =>
        item.id === id ? { ...item, quantity: newQuantity } : item
      )
    );
  };

  const handleRemove = (id) => {
    setCartItems(prevItems => prevItems.filter(item => item.id !== id));
  };

  const totalPages = Math.ceil(cartItems.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const endIndex = startIndex + ITEMS_PER_PAGE;
  const currentItems = cartItems.slice(startIndex, endIndex);

  const shipping = 0.1 * cost;
  const tax = cost * 0.08;
  const total = cost + shipping + tax;

  return (
    <div className="min-h-screen bg-gray-50">
      <Header />
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <h1 className="text-2xl font-bold text-gray-900 mb-8">Shopping Cart</h1>

        <div className="lg:grid lg:grid-cols-12 lg:gap-8">
          <div className="lg:col-span-8">
            <div className="bg-white rounded-lg shadow">
              <div className="p-6">
                {currentItems.length === 0 ? (
                  <p className="text-center text-gray-500 py-8">Your cart is empty</p>
                ) : (
                  currentItems.map((item) => (
                    <CartItem
                      key={item.id}
                      {...item}
                      onUpdateQuantity={handleUpdateQuantity}
                      onRemove={handleRemove}
                    />
                  ))
                )}

                {cartItems.length > ITEMS_PER_PAGE && (
                  <Pagination
                    currentPage={currentPage}
                    totalPages={totalPages}
                    onPageChange={setCurrentPage}
                  />
                )}
              </div>
            </div>
          </div>

          <div className="lg:col-span-4 mt-8 lg:mt-0">
            <div className="bg-white rounded-lg shadow p-6">
              <h2 className="text-lg font-medium text-gray-900 mb-4">Order Summary</h2>
              <div className="space-y-3">
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Subtotal</span>
                  <span className="text-gray-900">${cost.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Shipping</span>
                  <span className="text-gray-900">${shipping.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span className="text-gray-600">Tax</span>
                  <span className="text-gray-900">${tax.toFixed(2)}</span>
                </div>
                <div className="pt-3 border-t">
                  <div className="flex justify-between">
                    <span className="text-base font-medium text-gray-900">Total</span>
                    <span className="text-base font-medium text-gray-900">${total.toFixed(2)}</span>
                  </div>
                </div>
              </div>
              <button
                className="mt-6 w-full bg-indigo-600 text-white py-3 px-4 rounded-md hover:bg-indigo-700 transition-colors"
                onClick={() => navigate("checkout")}
                disabled={cartItems.length === 0}
              >
                Proceed to Checkout
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
}

export default ShoppingCart;
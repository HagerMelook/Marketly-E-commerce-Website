import { Minus, Plus, Trash2 } from 'lucide-react';
import PropTypes from 'prop-types';

export function CartItem({ id, name, price, quantity, image, onUpdateQuantity, onRemove }) {
  const handleQuantityChange = async (newQuantity) => {
    if (newQuantity < 1) return;

    try {
      const accountId = sessionStorage.getItem("id");
      const response = await fetch(`http://localhost:8080/ShoppingCart/Update/${id}/${accountId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${sessionStorage.getItem("token")}`,
        },
        body: JSON.stringify(newQuantity)
      });

      if (response.ok) {
        onUpdateQuantity(id, newQuantity);
      } else {
        console.error("Failed to update quantity");
        onUpdateQuantity(id, newQuantity);
      }
    } catch (error) {
      console.error("Error updating quantity:", error);
    }
  };

  const handleRemove = async () => {
    try {
      const accountId = sessionStorage.getItem("id");
      const response = await fetch(`http://localhost:8080/ShoppingCart/Delete/${id}/${accountId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("token")}`,
        },
      });

      if (response.ok) {
        onRemove(id);
      } else {
        console.error("Failed to remove item");
        onRemove(id);
      }
    } catch (error) {
      console.error("Error removing item:", error);
    }
  };

  return (
    <div className="flex items-center py-6 border-b">
      <img
        src={image || "/api/placeholder/96/96"}
        alt={name}
        className="h-24 w-24 object-cover rounded-md"
      />
      <div className="ml-6 flex-1">
        <h3 className="text-lg font-medium text-gray-900">{name}</h3>
        <p className="mt-1 text-sm text-gray-500">${price.toFixed(2)}</p>
        <div className="mt-2 flex items-center">
          <button
            className="p-1 rounded-md hover:bg-gray-100"
            onClick={() => handleQuantityChange(quantity - 1)}
          >
            <Minus className="h-4 w-4 text-gray-600" />
          </button>
          <span className="mx-3 text-gray-700">{quantity}</span>
          <button
            className="p-1 rounded-md hover:bg-gray-100"
            onClick={() => handleQuantityChange(quantity + 1)}
          >
            <Plus className="h-4 w-4 text-gray-600" />
          </button>
        </div>
      </div>
      <div className="ml-6">
        <p className="text-lg font-medium text-gray-900">
          ${(price * quantity).toFixed(2)}
        </p>
        <button
          onClick={handleRemove}
          className="mt-2 text-red-600 hover:text-red-500 flex items-center"
        >
          <Trash2 className="h-4 w-4 mr-1" />
          <span className="text-sm">Remove</span>
        </button>
      </div>
    </div>
  );
}

CartItem.propTypes = {
  id: PropTypes.number.isRequired,
  name: PropTypes.string.isRequired,
  price: PropTypes.number.isRequired,
  quantity: PropTypes.number.isRequired,
  image: PropTypes.string,
  onUpdateQuantity: PropTypes.func.isRequired,
  onRemove: PropTypes.func.isRequired
};
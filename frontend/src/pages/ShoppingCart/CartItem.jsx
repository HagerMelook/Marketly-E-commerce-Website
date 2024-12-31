import { Minus, Plus, Trash2 } from 'lucide-react';
import proptypes from 'prop-types';

export function CartItem({ name, price, quantity, image }) {
  return (
    <div className="flex items-center py-6 border-b">
      <img
        src={image}
        alt={name}
        className="h-24 w-24 object-cover rounded-md"
      />
      <div className="ml-6 flex-1">
        <h3 className="text-lg font-medium text-gray-900">{name}</h3>
        <p className="mt-1 text-sm text-gray-500">${price.toFixed(2)}</p>
        <div className="mt-2 flex items-center">
          <button className="p-1 rounded-md hover:bg-gray-100">
            <Minus className="h-4 w-4 text-gray-600" />
          </button>
          <span className="mx-3 text-gray-700">{quantity}</span>
          <button className="p-1 rounded-md hover:bg-gray-100">
            <Plus className="h-4 w-4 text-gray-600" />
          </button>
        </div>
      </div>
      <div className="ml-6">
        <p className="text-lg font-medium text-gray-900">
          ${(price * quantity).toFixed(2)}
        </p>
        <button className="mt-2 text-red-600 hover:text-red-500 flex items-center">
          <Trash2 className="h-4 w-4 mr-1" />
          <span className="text-sm">Remove</span>
        </button>
      </div>
    </div>
  );
}

CartItem.propTypes = {
  name: proptypes.string.isRequired,
  price: proptypes.number.isRequired,
  quantity: proptypes.number.isRequired,
  image: proptypes.string.isRequired,
};

import PropTypes from 'prop-types';
import editIcon from "../../assets/edit-icon.svg";
import { useProductContext} from "../../contexts/ProductProvider"
import trashIcon from "../../assets/trash-icon.svg";
import './InventoryProductListing.css'
import { useNavigate } from 'react-router-dom';

function InventoryProduct({product, onRemove}){
    const {setSelectedProduct} = useProductContext();
    const navigate = useNavigate();

    const handleEdit = () => {
        setSelectedProduct(product)
        navigate('product-form')
    };

    const handleRemoveProduct = () => {
        fetch(`http://localhost:8080/vendor/delete/${product.id}`, {
            method: "DELETE", 
            })
            .then(res => {
                if (!res.ok) {
                    throw Error('Could not delete product');
                }
                return res.json();
            })
            .then(() => {
                onRemove(product.id)
            })
    }

    return(
        <tr>
            <td>{product.id}</td>
            <td>
                <img src={product.imagePaths[0]} alt="Product" style={{height: "120px" }} className ="product-image" />
            </td>
            <td>{product.name}</td>
            <td>{product.quantity}</td>
            <td>{product.price}</td>
            <td>{product.category}</td>
            <td>
                <div className="action">
                <button onClick={handleEdit}>
                    <img src={editIcon} className="inventory-icon" alt="Edit" />
                </button>
                <button onClick={handleRemoveProduct}>
                    <img src=  {trashIcon} className="inventory-icon" alt="Delete"/>
                </button>
                </div>
            </td>
        </tr>
          
    );
}

InventoryProduct.propTypes = {
    product: PropTypes.shape({
                id: PropTypes.number.isRequired,
                vendorId: PropTypes.number.isRequired,
                name: PropTypes.string.isRequired,
                description: PropTypes.string.isRequired,
                quantity: PropTypes.number,
                price: PropTypes.number,
                category: PropTypes.string,
                imagePaths: PropTypes.arrayOf(PropTypes.string),
            }).isRequired,
    onRemove: PropTypes.func.isRequired
};
export default InventoryProduct;


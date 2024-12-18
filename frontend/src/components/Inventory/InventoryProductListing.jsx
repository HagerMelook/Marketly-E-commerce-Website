import InventoryProduct from './InventoryProduct';
import PropTypes from 'prop-types';
import './InventoryProductListing.css'

function InventoryProductListing({products, onRemove}) {
   
    
    return(
        <div className="product-list">
            <table>
            `   <thead>
                    <tr>
                        <th>ID</th>
                        <th>Product Image</th>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Category</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {products.length > 0 && products.map((product) => (<InventoryProduct key={product.id} product={product}  onRemove={onRemove}/>))}
                </tbody>         
            </table>`
        </div>
    );
}

InventoryProductListing.propTypes = {
    products: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,             
            vendorId: PropTypes.number.isRequired,        
            name: PropTypes.string.isRequired,            
            description: PropTypes.string.isRequired,    
            quantity: PropTypes.number,                   
            price: PropTypes.number,                      
            category: PropTypes.string,                  
            images: PropTypes.arrayOf(PropTypes.string), 
        })
    ).isRequired,
    onRemove: PropTypes.func.isRequired
};

export default InventoryProductListing;
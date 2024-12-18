import PropTypes from "prop-types";
import Rating from "@mui/material/Rating";
import "./ProductInfo.css"

function ProductInfo({product}) {
    return(
        <div>
            <div style={{borderBottom: "1px solid rgb(141, 125, 125)", paddingBottom: "15px", borderStyle: ""}}>
                <h1 >{product.name}</h1>
                    <p><strong>Vendor: </strong>{product.organizationName}</p>
                    <h3>Price: ${product.price}</h3>
                    <div style={{display: "flex", gap: "5px"}}>
                    <Rating precision={0.25} value={product.rating} readOnly /> {product.rating.toFixed(1)}/5
                    </div>
            </div>
            <div>
                {product.quantity>0 ? (
                    <h2 style={{color: "#007600"}}>In Stock</h2>
                )
                :(
                    <h2 style={{color: "red"}}>Out of Stock</h2>
                )}
                <button className="blue-button" disabled={product.quantity==0}>Add to Cart</button>
            </div>
        </div>
    );
}

ProductInfo.propTypes = {
    product: PropTypes.object
};

export default ProductInfo;
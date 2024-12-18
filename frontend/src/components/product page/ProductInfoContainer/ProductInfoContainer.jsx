import PropTypes from "prop-types";
import ImagesContainer from "./ImagesContainer/ImagesContainer";
import "./ProductInfoContainer.css"
import ProductInfo from "./ProductInfo/ProductInfo"

function ProductInfoContainer({ images, product }) {

    return (
        <div
          className="product-info-container"
          style={{
            display: "flex",
            gap: "20px",
            alignItems: "flex-start",
            justifyContent: "center",
            maxWidth: "1200px"
          }}
        >
          <ImagesContainer images={images} />

          <div
            className="info-container"
            style={{
              flex: "1", // Takes up 50% of available space
              maxWidth: "50%", // Ensures it doesn't exceed 50%
              padding: "10px",
              border: "1px solid #ddd",
              borderRadius: "8px",
              backgroundColor: "#f9f9f9",
            }}
            
          >
            {product ? (
                <ProductInfo product={product}/>
            ) : (
              <p>Loading product information...</p>
            )}
          </div>
        </div>
      );
}

ProductInfoContainer.propTypes = {
    images: PropTypes.arrayOf(PropTypes.string),
    product: PropTypes.object
};

export default ProductInfoContainer;
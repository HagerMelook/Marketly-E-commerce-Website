import PropTypes from "prop-types";
import ImagesContainer from "./ImagesContainer/ImagesContainer";
import "./ProductInfoContainer.css"
import ProductInfo from "./ProductInfo/ProductInfo"

function ProductInfoContainer({ images, product }) {

    return (
        <div
          className="product-all-info-container"
        >
          <ImagesContainer images={images} />

          <div
            className="info-container"            
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
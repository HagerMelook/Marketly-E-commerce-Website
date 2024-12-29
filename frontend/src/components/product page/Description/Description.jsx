import PropTypes from "prop-types";
import "./Description.css"

function Description ({ product }) {
  return (
    <div className="product-info">
        {product && (<>
        <h1 className="section-title">Description</h1>
        <p className="product-description">{product && product.description}</p>

        <h1 className="section-title">Vendor Information</h1>
        <p className="vendor-info">
            <strong className="vendor-label">Vendor name: </strong> {product.organizationName}<br/>
            {product.phoneNumber && (
            <>
                <strong className="vendor-label">Phone number: </strong>
                {product.phoneNumber}
            </>
            )}<br/>
            {product.address && (
            <>
                <strong className="vendor-label">Address: </strong>
                {product.address}
            </>
            )}<br/>
            {product.postalCode && (
            <>
                <strong className="vendor-label">Postal code: </strong>
                {product.postalCode}
            </>
            )}
        </p>
        </>)}
    </div>
    );
};

Description.propTypes = {
    product: PropTypes.object
};

export default Description;
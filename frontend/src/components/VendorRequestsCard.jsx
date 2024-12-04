import PropTypes from "prop-types";

const VendorRequestsCard = (props) => {
    const { request, onRemove } = props;

    const handleReject = () => {
        fetch('http://localhost:8080/admin/request/delete/' + request.id, {
            method: "DELETE", 
        })
        .then((res) => {
            console.log(res)
            if (!res.ok) {
                throw new Error("Failed to reject vendor");
            }
            return res.json();
        })
        .then(() => {
            onRemove(request.id); 
        })
        .catch((error) => {
            console.error("Error deleting vendor:", error);
            alert("Failed to delete vendor");
        });
        
    };

    const handleAccept = () => {
        fetch('http://localhost:8080/admin/request/accept/' + request.id, {
            method: "POST", 
        })
        .then((res) => {
            console.log(res)
            if (!res.ok) {
                throw new Error("Failed to add vendor");
            }
            return res.json();
        })
        .then(() => {
            onRemove(request.id); 
        })
        .catch((error) => {
            console.error("Error adding vendor:", error);
            alert("Failed to add vendor");
        });
    };

    return (  
        <div className="vendor-request-card">
            <div className="vendor-info">
                    <p><strong>Name:</strong> {request.username}</p>
                    <p><strong>Organisation Name:</strong> {request.organisationName}</p>
                    <p><strong>Tax Number:</strong> {request.taxNumber}</p>
            </div>
            <div className="vendor-actions">
                <button className="accept-btn" onClick={handleAccept}>accept</button>
                <button className="reject-btn" onClick={handleReject}>reject</button>
            </div>
            
        </div>
    ); 
};

VendorRequestsCard.propTypes = {
    request: PropTypes.shape({
        id: PropTypes.number.isRequired,
        email: PropTypes.string.isRequired,
        username: PropTypes.string.isRequired,
        organisationName: PropTypes.string.isRequired,
        taxNumber:PropTypes.string.isRequired,
    }).isRequired,
    onRemove: PropTypes.func.isRequired,
};

export default VendorRequestsCard;
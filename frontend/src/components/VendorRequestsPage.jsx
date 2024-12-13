import { useEffect, useState } from "react";
import VendorRequestsCard from './VendorRequestsCard';
import './VendorRequest.css'
 
const VendorRequestsPage = () => {
    const [requests, setRequests] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/admin/request')
            .then(res => res.json())
            .then(data => {
                setRequests(data);
            })
            .catch(error => {
                console.error("Error fetching vendor requests:", error);
            });
    }, []);

    const handleRemoveRequest = (id) => {
        setRequests((prevRequests) => prevRequests.filter(request => request.id !== id));
    };

    if (requests === null) {
        return <div>Loading...</div>;  
    }

    return (  
        <>
            <h1>Vendor Requests</h1>
            <div className="vendor-request-page">
                {requests.length > 0 ? (
                    requests.map((request) => (
                        <VendorRequestsCard key={request.id} request={request} onRemove={handleRemoveRequest}/>
                    ))
                ) : (
                    <div>No Vendor Requests available</div> 
                )}
            </div>
        </>
    );
};

export default VendorRequestsPage;

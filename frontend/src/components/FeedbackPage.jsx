import { useEffect, useState } from "react";
import FeedbackCard from "./FeedbackCard";
import './Feedback.css'
const FeedbackPage = () => {
    const [feedbacks, setFeedbacks] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/admin/feedback')
            .then(res => res.json())
            .then(data => {
                setFeedbacks(data);
            })
            .catch(error => {
                console.error("Error fetching feedbacks:", error);
            });
    }, []);

    const handleRemoveFeedback = (id) => {
        setFeedbacks((prevFeedbacks) => prevFeedbacks.filter(feedback => feedback.id !== id));
    };

    if (feedbacks === null) {
        return <div>Loading...</div>;  
    }

    return (  
        <>
            <h2>Feedback Review</h2>
            <div className="feedback-page">
                {feedbacks.length > 0 ? (
                    feedbacks.map((feedback) => (
                        <FeedbackCard key={feedback.id} feedback={feedback} onRemove={handleRemoveFeedback}/>
                    ))
                ) : (
                    <div>No feedbacks available</div> 
                )}
            </div>
        </>
    );
};

export default FeedbackPage;

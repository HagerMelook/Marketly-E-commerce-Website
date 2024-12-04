import PropTypes from "prop-types";

const FeedbackCard = (props) => {
    const { feedback, onRemove } = props;

    const handleRemove = () => {
    
        fetch('http://localhost:8080/admin/feedback/' + feedback.id, {
            method: "DELETE", 
        })
        .then((res) => {
            console.log(res)
            if (!res.ok) {
                throw new Error("Failed to delete feedback");
            }
            return res.json();
        })
        .then(() => {
            onRemove(feedback.id); 
        })
        .catch((error) => {
            console.error("Error deleting feedback:", error);
            alert("Failed to delete feedback");
        });
        
    };

    return (  
        <div className="feedback-card">
            <span>{feedback.body}</span>
            <button onClick={handleRemove}>Delete</button>
        </div>
    ); 
};

FeedbackCard.propTypes = {
    feedback: PropTypes.shape({
        id: PropTypes.number.isRequired,
        body: PropTypes.string.isRequired,
    }).isRequired,
    onRemove: PropTypes.func.isRequired,
};

export default FeedbackCard;

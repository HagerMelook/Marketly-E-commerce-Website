import PropTypes from "prop-types";
import Rating from "@mui/material/Rating";
import "./Comments.css";

const CommentsComponent = ({ comments }) => {
  return (
    <div className="commentsContainer">
      {comments.map((comment, index) => (
        <div key={index} className="commentCard">
          <div className="commentHeader">
            <Rating value={comment.rating} readOnly />
            <span className="commenterName">{comment.name}</span>
          </div>
          <p className="commentText">{comment.body}</p>
        </div>
      ))}
    </div>
  );
};

CommentsComponent.propTypes = {
  comments: PropTypes.arrayOf(
    PropTypes.shape({
      rating: PropTypes.number.isRequired,
      body: PropTypes.string.isRequired,
    })
  ).isRequired
};

export default CommentsComponent;
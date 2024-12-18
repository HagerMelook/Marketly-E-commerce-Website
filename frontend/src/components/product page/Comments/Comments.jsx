import { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { getComments } from "../../../components/API/ProductPageApi";
import { getCommentByProduct, deleteComment} from "../../API/CommentsApi";
import { getRateByProduct, deleteRate} from "../../API/RatesApi";
import Rating from "@mui/material/Rating";
import AddCommentPopup from "./AddCommentPopUP";

const TopComponent = ({ productRating, productId, accountId, handler, userComment, userRate, onDelete }) => {
  const [isPopupOpen, setIsPopupOpen] = useState(false);

  const openPopup = () => setIsPopupOpen(true);
  const closePopup = () => setIsPopupOpen(false);

  return (
    <div style={styles.topContainer}>
      <div style={styles.leftContent}>
        <span style={styles.ratingText}>Product Rating: </span>
        <Rating precision={0.25} value={productRating} readOnly />
      </div>

      {userComment || userRate ? (
        <div>
          <button style={styles.editButton} onClick={openPopup}>
            Edit Feedback
          </button>
          <button style={styles.deleteButton} onClick={onDelete}>
            Delete Feedback
          </button>
        </div>
      ) : (
        <button style={styles.addButton} onClick={openPopup}>
          Add Feedback
        </button>
      )}

      <AddCommentPopup
        open={isPopupOpen}
        onClose={closePopup}
        productId={productId}
        accountId={accountId}
        onCommentSubmitted={handler}
        existingComment={userComment}
        existingRate={userRate}
      />
    </div>
  );
};

TopComponent.propTypes = {
  productRating: PropTypes.number.isRequired,
  productId: PropTypes.number.isRequired,
  accountId: PropTypes.number.isRequired,
  handler: PropTypes.func.isRequired,
  userComment: PropTypes.object,
  userRate: PropTypes.object,
  onDelete: PropTypes.func.isRequired,
};

const CommentsComponent = ({ comments }) => {
  return (
    <div style={styles.commentsContainer}>
      {comments.map((comment, index) => (
        <div key={index} style={styles.commentCard}>
          <div style={styles.commentHeader}>
            <Rating value={comment.rating} readOnly />
            <span style={styles.commenterName}>{comment.name}</span>
          </div>
          <p style={styles.commentText}>{comment.body}</p>
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
  ).isRequired,
};

const PaginationComponent = ({ onPrevious, onNext, currentPage, hasNext }) => {
  return (
    <div style={styles.paginationContainer}>
      <button onClick={onPrevious} disabled={currentPage === 1}>
        Previous
      </button>
      <span style={styles.pageIndicator}>Page {currentPage}</span>
      <button onClick={onNext} disabled={!hasNext}>
        Next
      </button>
    </div>
  );
};

PaginationComponent.propTypes = {
  onPrevious: PropTypes.func.isRequired,
  onNext: PropTypes.func.isRequired,
  currentPage: PropTypes.number.isRequired,
  hasNext: PropTypes.bool.isRequired,
};

const ProductComments = ({ productId, productRating, accountId }) => {
  const [comments, setComments] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [userComment, setUserComment] = useState(null);
  const [userRate, setUserRate] = useState(null);

  const pageSize = 5;

  const fetchComments = async (page) => {
    try {
      const queryParams = `page=${page}&size=${pageSize}`;
      const data = await getComments(productId, queryParams);

      setComments(data.content);
      setHasNext(!data.last);
      setCurrentPage(page);
    } catch (error) {
      console.error("Failed to fetch comments:", error);
    }
  };

  const fetchUserFeedback = async () => {
    try {
      const comment = await getCommentByProduct(accountId, productId);
      const rate = await getRateByProduct(accountId, productId);
      setUserComment(comment);
      setUserRate(rate);
    } catch (error) {
      console.error("Failed to fetch user feedback:", error);
    }
  };

  useEffect(() => {
    fetchComments(0);
    fetchUserFeedback();
  }, [productId]);

  const handlePrevious = () => {
    if (currentPage > 0) fetchComments(currentPage - 1);
  };

  const handleNext = () => {
    if (hasNext) fetchComments(currentPage + 1);
  };

  const handleCommentSubmitted = () => {
    fetchComments(0);
    fetchUserFeedback();
  };

  const handleDeleteFeedback = async () => {
    try {
      if (userComment) {
        await deleteComment(userComment.id);
      } else if (userRate) {
        await deleteRate(userRate.id);
      }
      fetchComments(0);
      fetchUserFeedback();
    } catch (error) {
      console.error("Failed to delete feedback:", error);
    }
  };

  return (
    <div style={styles.container}>
      <TopComponent
        productRating={productRating}
        productId={productId}
        accountId={accountId}
        handler={handleCommentSubmitted}
        userComment={userComment}
        userRate={userRate}
        onDelete={handleDeleteFeedback}
      />
      <CommentsComponent comments={comments} />
      <PaginationComponent
        onPrevious={handlePrevious}
        onNext={handleNext}
        currentPage={currentPage + 1}
        hasNext={hasNext}
      />
    </div>
  );
};

ProductComments.propTypes = {
  productId: PropTypes.number.isRequired,
  productRating: PropTypes.number.isRequired,
  accountId: PropTypes.number.isRequired,
};

const styles = {
  container: {
    maxWidth: "800px",
    margin: "20px auto",
    border: "1px solid #ddd",
    borderRadius: "8px",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
    fontFamily: "Arial, sans-serif",
  },
  topContainer: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "10px 20px",
    borderBottom: "1px solid #ddd",
    backgroundColor: "#f9f9f9",
  },
  leftContent: {
    display: "flex",
    alignItems: "center",
    gap: "8px",
  },
  ratingText: {
    fontWeight: "bold",
  },
  addButton: {
    padding: "5px 10px",
    backgroundColor: "#0074D9",
    color: "#fff",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  editButton: {
    padding: "5px 10px",
    backgroundColor: "#FFA500",
    color: "#fff",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
    marginRight: "5px",
  },
  deleteButton: {
    padding: "5px 10px",
    backgroundColor: "#FF4136",
    color: "#fff",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
  },
  commentsContainer: {
    padding: "10px 20px",
    maxHeight: "300px",
    overflowY: "auto",
  },
  commentCard: {
    borderBottom: "1px solid #eee",
    padding: "10px 0",
  },
  commentHeader: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
  },
  commentText: {
    marginTop: "5px",
    fontSize: "14px",
    color: "#333",
  },
  commenterName: {
    fontStyle: "italic",
    color: "#555",
  },
  paginationContainer: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "10px 20px",
    borderTop: "1px solid #ddd",
    backgroundColor: "#f9f9f9",
  },
  pageIndicator: {
    fontWeight: "bold",
  },
};

export default ProductComments;
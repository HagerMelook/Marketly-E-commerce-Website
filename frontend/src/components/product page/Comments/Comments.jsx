import { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { getComments } from "../../../components/API/ProductPageApi";
import { getCommentByProduct, deleteComment} from "../../API/CommentsApi";
import { getRateByProduct, deleteRate} from "../../API/RatesApi";
import "./Comments.css";
import TopComponent from "./TopComponent";
import CommentsComponent from "./CommentsComponent";
import PaginationComponent from "./PaginationComponent";

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
      if (userComment && userComment.id) {
        await deleteComment(userComment.id);
      } else if (userRate && userRate.id) {
        await deleteRate(userRate.id);
      }
      fetchComments(0);
      fetchUserFeedback();
    } catch (error) {
      console.error("Failed to delete feedback:", error);
    }
  };

  return (
    <div className="container">
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

export default ProductComments;

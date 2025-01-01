import { useState } from "react";
import PropTypes from "prop-types";
import Rating from "@mui/material/Rating";
import AddCommentPopup from "./AddCommentPopUP";
import "./Comments.css";

const TopComponent = ({ productRating, productId, accountId, handler, userComment, userRate, onDelete }) => {
    const [isPopupOpen, setIsPopupOpen] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
  
    const openPopup = (isEditing) => {
      setIsPopupOpen(true);
      setIsEditing(isEditing ? true : false);
    };
  
    const closePopup = () => {
      setIsPopupOpen(false);
      setIsEditing(false);
    };
  
    return (
      <div className="topContainer">
        <div className="leftContent">
          <span className="ratingText">Product Rating: </span>
          <Rating precision={0.25} value={productRating} readOnly />
          {productRating.toFixed(1)}/5.0
        </div>
  
        {(userComment && userComment.id) || (userRate && userRate.id) ? (
          <div>
            <button className="editButton" onClick={() => openPopup(true)}>
              Edit Review
            </button>
            <button className="deleteButton" onClick={onDelete}>
              Delete Review
            </button>
          </div>
        ) : (
          <button className="addButton" onClick={() => openPopup(false)}>
            Add Review
          </button>
        )}
  
        {((userComment && userComment.id) || (userRate && userRate.id)) &&  <div className="commentCard" style={{padding: "10px", borderBottom: "none"}}>
          <h4>Your Review:</h4>
          <div className="commentHeader">
            <Rating value={userComment.id ? userComment.rating : userRate.rating} readOnly />
          </div>
          {userComment.id && <p className="commentText">{userComment.body}</p>}
        </div>}
  
        <AddCommentPopup
          open={isPopupOpen}
          onClose={closePopup}
          productId={productId}
          accountId={accountId}
          onCommentSubmitted={handler}
          existingComment={userComment}
          existingRate={userRate}
          isEditing={isEditing}
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

  export default TopComponent;
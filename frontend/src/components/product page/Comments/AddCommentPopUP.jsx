import { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { Box, Modal, Typography, Button, Rating, TextField } from "@mui/material";
import { createComment, updateComment} from "../../../components/API/CommentsApi";
import { createRate, updateRate} from "../../../components/API/RatesApi";

const AddCommentPopup = ({ 
  open, 
  onClose, 
  productId, 
  accountId, 
  onCommentSubmitted, 
  existingComment, 
  existingRate, 
  isEditing
}) => {
  const [rating, setRating] = useState(0);
  const [commentBody, setCommentBody] = useState("");

  useEffect(() => {
    if (existingComment && existingComment.id) {
      setRating(existingComment.rating);
      setCommentBody(existingComment.body || "");
    } else if (existingRate && existingRate.id) {
      setRating(existingRate.rating);
      setCommentBody(""); 
    } else {
      setRating(0);
      setCommentBody("");
    }
  }, [existingComment, existingRate]);

  const handleSubmit = async () => {
    try {
      if (existingComment && existingComment.id) {
        existingComment.rating = rating;
        existingComment.body = commentBody;
        await updateComment(existingComment.id, existingComment);
      } else if (existingRate && existingRate.id) {
        existingRate.rating = rating
        await updateRate(existingRate.id, existingRate);
      } else {
        if (commentBody) {
          await createComment({id: null, productId: productId, accountId: accountId, rating: rating, body: commentBody});
        } else {
          await createRate({id: null, productId: productId, accountId: accountId, rating: rating});
        }
      }
      onCommentSubmitted();
      onClose();
    } catch (error) {
      console.error("Submission failed:", error);
    }
  };

  return (
    <Modal open={open} onClose={onClose} aria-labelledby="add-comment-modal">
      <Box sx={styles.modalContainer}>
        <Typography variant="h6" sx={{ mb: 2 }}>{existingComment || existingRate ? "Edit Feedback" : "Add Feedback"}</Typography>

        <Box sx={{ mb: 2 }}>
          <Typography variant="subtitle1">Rate the product:</Typography>
          <Rating
            value={rating}
            onChange={(event, newValue) => setRating(newValue)}
          />
        </Box>

        {((existingComment && existingComment.id) || !isEditing) && <TextField
          label="Comment Body"
          placeholder="Write your comment here"
          multiline
          fullWidth
          rows={4}
          value={commentBody}
          onChange={(e) => setCommentBody(e.target.value)}
          sx={{ mb: 2 }}
        />}

        <Box sx={{ display: "flex", alignItems: "center", justifyContent: "flex-start", gap: 2 }}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSubmit}
            disabled={!rating || (isEditing && existingComment && existingComment.id && commentBody == "") || commentBody.length>255}
          >
            Submit
          </Button>
          <Typography variant="body2" sx={{ color: "gray" }}>
            {commentBody ? "Full Review" : "Rate Only"}
          </Typography>
          {commentBody.length>255 && <Typography variant="body2" sx={{ color: "red" }}>
            Body is too long.<br/> Max: 255 characters.
          </Typography>}
        </Box>
      </Box>
    </Modal>
  );
};

const styles = {
  modalContainer: {
    position: "absolute",
    top: "50%",
    left: "50%",
    transform: "translate(-50%, -50%)",
    width: 400,
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
    borderRadius: "8px",
    color: "black",
  },
};

AddCommentPopup.propTypes = {
  open: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  productId: PropTypes.number.isRequired,
  accountId: PropTypes.number.isRequired,
  onCommentSubmitted: PropTypes.func.isRequired,
  existingComment: PropTypes.object,
  existingRate: PropTypes.object,
  isEditing: PropTypes.bool
};

export default AddCommentPopup;

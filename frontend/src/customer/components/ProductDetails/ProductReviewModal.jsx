import React, { useState } from "react";
import { Box, Modal, Typography, Button, TextField, Rating } from "@mui/material";

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 350,
  bgcolor: 'background.paper',
  border: '2px solid #eee',
  boxShadow: 24,
  p: 4,
  borderRadius: 2
};

const ProductReviewModal = ({ open, onClose, onSubmit, product }) => {
  const [rating, setRating] = useState(0);
  const [review, setReview] = useState("");

  const handleSubmit = () => {
    if (rating > 0 && review.trim()) {
      onSubmit({ rating, review });
      setRating(0);
      setReview("");
    }
  };

  const handleClose = () => {
    setRating(0);
    setReview("");
    onClose();
  };

  return (
    <Modal open={open} onClose={handleClose}>
      <Box sx={style}>
        <Typography variant="h6" mb={2}>
          Review: {product?.title}
        </Typography>
        <Rating
          name="product-rating"
          value={rating}
          precision={0.5}
          onChange={(_, newValue) => setRating(newValue)}
        />
        <TextField
          label="Your review"
          multiline
          rows={3}
          fullWidth
          value={review}
          onChange={e => setReview(e.target.value)}
          sx={{ my: 2 }}
        />
        <Box display="flex" justifyContent="flex-end" gap={1}>
          <Button onClick={handleClose} color="secondary">Cancel</Button>
          <Button onClick={handleSubmit} variant="contained" disabled={rating === 0 || !review.trim()}>
            Send
          </Button>
        </Box>
      </Box>
    </Modal>
  );
};

export default ProductReviewModal; 
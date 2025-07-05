import { Avatar, Box, Grid, Rating } from "@mui/material";
import React from "react";
import { formatDate } from "../../../util/util";

const ProductReviewCard = ({ review }) => {
  return (
    <div>
      <Grid container spacing={2} gap={3}>
        <Grid item xs={1}>
          <Box>
            <Avatar
              className="text-white"
              sx={{ width: 56, height: 56, bgcolor: "#9155fd" }}
            >
              R
            </Avatar>
          </Box>
        </Grid>

        <Grid item xs={9}>
          <div className="space-y-2">
            <div>
              <p className="font-semibold text-lg">
                {(review.user?.lastName || "") +
                  " " +
                  (review.user?.firstName || "")}
              </p>
              <p className="opacity-70">{formatDate(review.createdAt)}</p>
            </div>
          </div>
          <Rating
            value={review.rating || 0}
            name="half-rating"
            readOnly
            precision={0.5}
          />
          <p>{review.review}</p>
        </Grid>
      </Grid>
    </div>
  );
};

export default ProductReviewCard;

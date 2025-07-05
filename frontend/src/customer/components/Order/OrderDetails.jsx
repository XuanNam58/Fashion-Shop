import React from "react";
import AddressCard from "../AddressCard/AddressCard";
import OrderTracker from "./OrderTracker";
import { Box, Grid } from "@mui/material";
import { deepPurple } from "@mui/material/colors";
import StarBorderIcon from "@mui/icons-material/StarBorder";
import { useSelector, useDispatch } from "react-redux";
import ProductReviewModal from "../ProductDetails/ProductReviewModal";
import { createRating } from "../../../State/Rating/Action";
import { createReview } from "../../../State/Review/Actions";

const OrderDetails = () => {
  const order = useSelector((store) => store.order.order);
  const [openReview, setOpenReview] = React.useState(false);
  const [selectedProduct, setSelectedProduct] = React.useState(null);
  const dispatch = useDispatch();

  const handleOpenReview = (product) => {
    setSelectedProduct(product);
    setOpenReview(true);
  };

  const handleCloseReview = () => {
    setOpenReview(false);
    setSelectedProduct(null);
  };

  const handleSubmitReview = ({ rating, review }) => {
    if (!selectedProduct) return;
    // Gửi rating
    dispatch(createRating({ productId: selectedProduct.id, rating }));
    // Gửi review
    dispatch(createReview({ productId: selectedProduct.id, review }));
    setOpenReview(false);
    setSelectedProduct(null);
  };

  return (
    <div className="px-5 lg:px-20">
      <div>
        <h1 className="font-semibold text-xl py-7">Delivery Address</h1>
        <AddressCard address={order?.shippingAddress} />
      </div>

      <div className="py-20">
        <OrderTracker activeStep={3} />
      </div>

      <Grid className="space-y-5" container>
        {order?.orderItems.map((item) => (
          <Grid
            item
            container
            className="shadow-xl rounded-md p-5 border"
            sx={{ alignItems: "center", justifyContent: "space-between" }}
            key={item._id}
          >
            <Grid item xs={6}>
              <div className="flex items-center space-x-4">
                <img
                  className="w-[5rem] h-[5rem] object-cover object-top"
                  src={item.product.imageUrl}
                  alt=""
                />

                <div className="space-y-2 ml-5">
                  <p className="font-semibold">{item.product.title}</p>
                  <p className="space-x-5 opacity-50 text-xs font-semibold">
                    <span>Color : {item.product.color}</span>
                    <span>Size : {item.size}</span>
                  </p>
                  <p>Seller: {item.product.brand}</p>
                  <p>{item.discountedPrice}</p>
                </div>
              </div>
            </Grid>

            <Grid item>
              <Box sx={{ color: deepPurple[500], cursor: "pointer" }} onClick={() => handleOpenReview(item.product)}>
                <StarBorderIcon sx={{ fontSize: "2rem" }} className="px-2" />
                <span>Rate & Review Product</span>
              </Box>
            </Grid>
          </Grid>
        ))}
      </Grid>

      <ProductReviewModal
        open={openReview}
        onClose={handleCloseReview}
        onSubmit={handleSubmitReview}
        product={selectedProduct}
      />
    </div>
  );
};

export default OrderDetails;

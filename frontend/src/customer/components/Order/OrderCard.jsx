import { Grid } from "@mui/material";
import React from "react";
import AdjustIcon from "@mui/icons-material/Adjust";
import { useNavigate } from "react-router-dom";
import { formatDate } from "../../../util/util";
import { getOrderById } from "../../../State/Order/Action";
import { useDispatch } from "react-redux";
const OrderCard = ({ order }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const handleClickOrderItem = () => {
    dispatch(getOrderById(order.id));
    navigate(`/account/order/${order.id}`);
  };
  return (
    <div
      onClick={handleClickOrderItem}
      className="p-5 shadow-md shadow-black hover:shadow-2xl border"
    >
      <Grid container spacing={2} sx={{ justifyContent: "space-between" }}>
        <Grid item xs={6}>
          <div className="flex cursor-pointer">
            <img
              className="w-[5rem] h-[5rem] object-cover object-top"
              src="https://th.bing.com/th/id/OIP.ggr4rNNMuNyXQoLevhgtIAHaLH?w=186&h=279&c=7&r=0&o=5&dpr=1.3&pid=1.7"
              alt=""
            />
            <div className="ml-5 space-y-2">
              <p className="">{formatDate(order.createdAt)}</p>
              <p className="opacity-50 text-xs font-semibold">
                Total Price: {order.totalDiscountedPrice}
              </p>
              <p className="opacity-50 text-xs font-semibold">
                Total Item: {order.totalItem}
              </p>
            </div>
          </div>
        </Grid>

        <Grid item xs={2}>
          <p>{order.orderStatus}</p>
        </Grid>

        <Grid item xs={4}>
          {order.deliveryDate && (
            <div>
              <p>
                <AdjustIcon
                  sx={{ width: "15px", height: "15px" }}
                  className="text-green-600 mr-2 text-sm"
                />
                <span>Delivered On {formatDate(order.deliveryDate)}</span>
              </p>
              <p className="text-xs">Your Item Has Been Delivered</p>
            </div>
          )}

          {!order.deliveryDate && (
            <p>
              <span>Expected Delivered On </span>
            </p>
          )}
        </Grid>
      </Grid>
    </div>
  );
};

export default OrderCard;

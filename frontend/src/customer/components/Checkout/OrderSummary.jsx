import React, { useEffect } from "react";
import AddressCard from "../AddressCard/AddressCard";
import { Button } from "@mui/material";
import CartItem from "../Cart/CartItem";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { getOrderById } from "../../../State/Order/Action";
import { api } from "../../../config/apiConfig";
import { toast } from "react-toastify";

const OrderSummary = () => {
  const dispatch = useDispatch();
  const location = useLocation();
  const navigate = useNavigate();
  const searchParams = new URLSearchParams(location.search);
  const orderId = searchParams.get("order_id");
  const { order } = useSelector((store) => store);

  useEffect(() => {
    if (!orderId) {
      navigate("/checkout?step=2");
      return;
    }
    dispatch(getOrderById(orderId));
  }, [orderId]);

  const handleCheckout = async () => {
    try {
      const res = await api.post("/api/payment/create", {
        orderId: order.order?.id,
        amount: order.order?.totalDiscountedPrice
      })
      window.location.href = res.data.vnpayUrl;
    } catch (error) {
      toast.error("Payment failed");
    }
  };
  return (
    <div>
      <div className="p-5 shadow-lg rounded-s-md border">
        <AddressCard address={order.order?.shippingAddress} />
      </div>

      <div>
        <div className="lg:grid grid-cols-3 relative">
          <div className="col-span-2">
            {order.order?.orderItems.map((item) => (
              <CartItem item={item} />
            ))}
          </div>
          <div className="px-5 sticky top-0 h-[100vh] mt-5 lg:mt-0">
            <div className="border">
              <p className="uppercase font-bold opacity-60 pb-4">
                Price details
              </p>
              <hr />
              <div className="space-y-3 font-semibold mb-10">
                <div className="flex justify-between pt-3 text-black">
                  <span>Price</span>
                  <span className="text-green-600">
                    {order.order?.totalPrice}
                  </span>
                </div>

                <div className="flex justify-between pt-3 ">
                  <span>Discount</span>
                  <span className="text-green-600">
                    -{order.order?.discount}
                  </span>
                </div>

                <div className="flex justify-between pt-3 ">
                  <span>Delivery Charge</span>
                  <span className="text-green-600">Free</span>
                </div>

                <div className="flex justify-between pt-3 font-bold">
                  <span>Total Amount</span>
                  <span className="text-green-600">
                    {order.order?.totalDiscountedPrice}
                  </span>
                </div>
              </div>

              <Button
                variant="contained"
                fullWidth
                sx={{ px: "2.5rem", py: ".7rem", bgcolor: "#9155fd" }}
                onClick={handleCheckout}
              >
                Checkout
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderSummary;

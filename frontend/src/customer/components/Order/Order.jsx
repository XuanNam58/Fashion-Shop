import { Grid } from "@mui/material";
import React, { useEffect } from "react";
import OrderCard from "./OrderCard";
import { useDispatch, useSelector } from "react-redux";
import { getOrderHistory } from "../../../State/Order/Action";

const orderStatus = [
  { label: "On The Way", value: "on_the_way" },
  { label: "Delivered", value: "delivered" },
  { label: "Canceled", value: "canceled" },
  { label: "Returned", value: "returned" },
];

const Order = () => {
  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(getOrderHistory());
  }, []);
  const { orders } = useSelector((store) => store.order);
  return (
    <div className="px-5 lg:px-20">
      <Grid container sx={{ justifyContent: "space-between" }}>
        <Grid item sx={2.5}>
          <div className="h-auto shadow-lg bg-white p-5 sticky top-5">
            <h1 className="font-bold text-lg">Filter</h1>

            <div className="space-y-4 mt-10">
              <h1 className="font-semibold">ORDER STATUS</h1>

              {orderStatus.map((option) => (
                <div className="flex items-center">
                  <input
                    defaultValue={option.value}
                    type="checkbox"
                    className="h-4 w-4 border-gray-300 text-indigo-600 focus:ring-indigo-500"
                  />

                  <label
                    className="ml-3 text-sm text-gray-600"
                    htmlFor={option.value}
                  >
                    {option.label}
                  </label>
                </div>
              ))}
            </div>
          </div>
        </Grid>

        <Grid item xs={9}>
          <div className="space-y-5">
            {orders.map((order) => (
              <OrderCard order={order} />
            ))}
          </div>
        </Grid>
      </Grid>
    </div>
  );
};

export default Order;

import React from "react";
import { useLocation } from "react-router-dom";

const PaymentResult = () => {
  const searchParams = new URLSearchParams(useLocation().search);
  const status = searchParams.get("status");

  return (
    <div className="flex flex-col items-center justify-center h-screen">
      {status === "success" ? (
        <div>
          <h2 className="text-2xl font-bold text-green-600">Payment successful!</h2>
          <p>Thank you for your purchase.</p>
        </div>
      ) : (
        <div>
          <h2 className="text-2xl font-bold text-red-600">Payment failed!</h2>
          <p>Please try again or contact support.</p>
        </div>
      )}
    </div>
  );
};

export default PaymentResult;

import { Box, Modal, Typography } from "@mui/material";
import React, { useEffect } from "react";
import RegisterForm from "./RegisterForm";
import { useLocation, useNavigate } from "react-router-dom";
import LoginForm from "./LoginForm";
import { useSelector } from "react-redux";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 500,
  bgcolor: "background.paper",
  outline: "none",
  boxShadow: 24,
  p: 4,
};

const AuthModal = ({ handleClose, open }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { jwt, isLoading } = useSelector((state) => state.auth);

  useEffect(() => {
    if (jwt && !isLoading) {
      handleClose(); // Đóng modal khi đăng nhập/thành công
      navigate("/"); // Chuyển hướng về trang chính
    }
  }, [jwt, isLoading, handleClose, navigate]);
  return (
    <div>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          {location.pathname === "/login" ? <LoginForm /> : <RegisterForm />}
        </Box>
      </Modal>
    </div>
  );
};

export default AuthModal;

import { Box, Modal } from "@mui/material";
import React, { useEffect, useState } from "react";
import RegisterForm from "./RegisterForm";
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

const AuthModal = ({ handleClose, open, initialMode = "login" }) => {
  const [mode, setMode] = useState(initialMode); // "login" hoặc "register"
  const { jwt, isLoading } = useSelector((state) => state.auth);

  useEffect(() => {
    if (jwt && !isLoading) {
      handleClose(); // Đóng modal khi đăng nhập thành công
    }
  }, [jwt, isLoading, handleClose]);

  // Khi modal mở lại, reset về initialMode
  useEffect(() => {
    if (open) setMode(initialMode);
  }, [open, initialMode]);

  return (
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        {mode === "login" ? (
          <LoginForm
            onSwitchToRegister={() => setMode("register")}
          />
        ) : (
          <RegisterForm
            onSwitchToLogin={() => setMode("login")}
          />
        )}
      </Box>
    </Modal>
  );
};

export default AuthModal;

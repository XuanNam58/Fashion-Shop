import "./App.css";
import { Route, Routes } from "react-router-dom";
import CustomerRouters from "./Routers/CustomerRouters.jsx";
import ScrollToTop from "./ScrollToTop.jsx";
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import AuthModal from "./customer/Auth/AuthModal";
import { useState } from "react";
import { AuthModalContext } from "./customer/Auth/AuthModalContext";

function App() {
  const [openAuthModal, setOpenAuthModal] = useState(false);
  const [authModalMode, setAuthModalMode] = useState("login"); // hoặc "register"

  // Hàm này bạn truyền xuống các component con qua context hoặc prop
  const openAuthModalFunc = (mode = "login") => {
    setAuthModalMode(mode);
    setOpenAuthModal(true);
  };

  return (
    <AuthModalContext.Provider
      value={{
        openAuthModal: openAuthModalFunc,
        setOpenAuthModal,
        openAuthModalState: openAuthModal,
      }}
    >
      <div className="">
        <ScrollToTop />
        <Routes>
          <Route path="/*" element={<CustomerRouters />}></Route>
        </Routes>
        <ToastContainer position="top-right" autoClose={3000} />
        <AuthModal
          open={openAuthModal}
          handleClose={() => setOpenAuthModal(false)}
          initialMode={authModalMode}
        />
      </div>
    </AuthModalContext.Provider>
  );
}

export default App;

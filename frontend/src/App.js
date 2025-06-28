import "./App.css";
import { Route, Routes } from "react-router-dom";
import CustomerRouters from "./Routers/CustomerRouters.jsx";
import ScrollToTop from "./ScrollToTop.jsx";
function App() {
  return (
    <div className="">
      <ScrollToTop />
      <Routes>
        <Route path="/*" element={<CustomerRouters />}></Route>
      </Routes>
      
      
    </div>
  );
}

export default App;

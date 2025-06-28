import { Email } from "@mui/icons-material";
import { Button, TextField, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getUser, login } from "../../State/Auth/Action";

const LoginForm = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { isLoading, error, jwt } = useSelector((store) => store.auth);

  useEffect(() => {
    if (jwt) {
      navigate("/");
    }
  }, [jwt, navigate]);

  const [userData, setUserData] = useState({ email: "", password: "" });

  const handleSubmit = (e) => {
    e.preventDefault();
    dispatch(login(userData));
  };

  const handleChange = (e) => {
    setUserData({ ...userData, [e.target.name]: e.target.value });
  };
  return (
    <div>
      <form onSubmit={handleSubmit}>
        <div className="space-y-6">
          <div>
            <TextField
              required
              id="email"
              name="email"
              label="Email"
              fullWidth
              autoComplete="email"
              value={userData.email}
              onChange={handleChange}
              disabled={isLoading}
            />
          </div>

          <div>
            <TextField
              required
              id="password"
              name="password"
              label="Password"
              fullWidth
              autoComplete="password"
              value={userData.password}
              onChange={handleChange}
              disabled={isLoading}
            />
          </div>

          <div>
            <Button
              className="bg-[#9155FD] w-full"
              type="submit"
              variant="contained"
              size="large"
              sx={{ padding: ".8rem 0", bgcolor: "#9155FD" }}
              disabled={isLoading}
            >
              {isLoading ? "Logging in..." : "Login"}
            </Button>
          </div>
          {error && (
            <div>
              <Typography color="error">{error}</Typography>
            </div>
          )}
        </div>
      </form>

      <div className="flex justify-center flex-col items-center">
        <div className="py-3 flex items-center">
          <p>If you don't have already account ?</p>
          <Button
            onClick={() => navigate("/register")}
            className="ml-5"
            size="small"
            disabled={isLoading}
          >
            Register
          </Button>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;

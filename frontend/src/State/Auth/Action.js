import axios from "axios";
import { API_BASE_URL } from "../../config/apiConfig";
import {
  GET_USER_FAILURE,
  GET_USER_REQUEST,
  GET_USER_SUCCESS,
  LOGIN_FAILURE,
  LOGIN_REQUEST,
  LOGIN_SUCCESS,
  REGISTER_FAILURE,
  REGISTER_REQUEST,
  REGISTER_SUCCESS,
} from "./ActionType";

const token = localStorage.getItem("jwt");

const registerRequest = () => ({ type: REGISTER_REQUEST });
const registerSuccess = (user) => ({ type: REGISTER_SUCCESS, payload: user });
const registerFailure = (error) => ({ type: REGISTER_FAILURE, payload: error });
export const register = (userData) => async (dispatch) => {
  dispatch(registerRequest());
  try {
    const response = await axios.post(`${API_BASE_URL}/auth/signup`, userData);
    const user = response.data;
    if (user.jwt) {
      localStorage.setItem("jwt", user.jwt);
    }
    console.log("user", user);
    dispatch(registerSuccess(user.jwt));
  } catch (error) {
    dispatch(registerFailure(error.message));
  }
};

const loginRequest = () => ({ type: LOGIN_REQUEST });
const loginSuccess = (user) => ({ type: LOGIN_SUCCESS, payload: user });
const loginFailure = (error) => ({ type: LOGIN_FAILURE, payload: error });
export const login = (userData) => async (dispatch) => {
  dispatch(loginRequest());
  try {
    /**
     * axios là một thư viện HTTP client dựa trên Promise, được sử dụng để thực hiện các yêu cầu HTTP (như GET, POST, PUT, DELETE) từ trình duyệt hoặc Node.js.
     * await dùng để đợi cho đến khi Promise được giải quyết, sau đó trả về kết quả.
     * async là một từ khóa được sử dụng để khai báo một hàm bất đồng bộ (asynchronous function).
     *  Khi một hàm được khai báo với async, nó sẽ tự động trả về một Promise. 
     *  Nếu hàm trả về một giá trị cụ thể, giá trị đó sẽ được bọc trong một Promise được resolve. Nếu hàm throw một lỗi, Promise sẽ bị reject.
     */


    /**
     * Bất đồng bộ (Asynchronous): Xử lý các tác vụ không cần chờ đợi, giúp chương trình chạy mượt mà hơn.
     * Promise: Đại diện cho kết quả của một tác vụ bất đồng bộ, có thể thành công (resolve) hoặc thất bại (reject).
     * Async/Await: Cú pháp giúp làm việc với Promise một cách dễ đọc và gọn gàng hơn. */ 

    const response = await axios.post(`${API_BASE_URL}/auth/signin`, userData);
    const user = response.data;
    if (user.jwt) {
      localStorage.setItem("jwt", user.jwt);
    }
    console.log("user", user);
    dispatch(loginSuccess(user.jwt));
  } catch (error) {
    dispatch(loginFailure(error.message));
  }
};

const getUserRequest = () => ({ type: GET_USER_REQUEST });
const getUserSuccess = (user) => ({ type: GET_USER_SUCCESS, payload: user });
const getUserFailure = (error) => ({ type: GET_USER_FAILURE, payload: error });
export const getUser = (jwt) => async (dispatch) => {
  dispatch(getUserRequest());
  try {
    const response = await axios.get(`${API_BASE_URL}/api/users/profile`, {
      headers: {
        Authorization: `Bearer ${jwt}`,
      },
    });
    const user = response.data;
    console.log("user", user);
    dispatch(getUserSuccess(user));
  } catch (error) {
    dispatch(getUserFailure(error.message));
  }
};

export const logout = () => (dispatch) => {
  dispatch({ type: "LOGOUT", payload: null });
  localStorage.clear();
};

import { api, API_BASE_URL } from "../../config/apiConfig";
import {
  GET_USER_FAILURE,
  GET_USER_REQUEST,
  GET_USER_SUCCESS,
  LOGIN_FAILURE,
  LOGIN_REQUEST,
  LOGIN_SUCCESS,
  LOGOUT,
  REGISTER_FAILURE,
  REGISTER_REQUEST,
  REGISTER_SUCCESS,
} from "./ActionType";

const token = localStorage.getItem("jwt");

let isRefreshing = false;
let refreshSubscribers = [];

const subscribeTokenRefresh = (cb) => {
  refreshSubscribers.push(cb);
};

const onRefreshed = (newToken) => {
  refreshSubscribers.forEach((cb) => cb(newToken));
  refreshSubscribers = [];
  api.defaults.headers.Authorization = `Bearer ${newToken}`;
};

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      if (!isRefreshing) {
        isRefreshing = true;
        originalRequest._retry = true;

        try {
          const response = await api.post(`${API_BASE_URL}/auth/refresh-token`, {}, { withCredentials: true });
          const newAccessToken = response.data.jwt;
          onRefreshed(newAccessToken);
        } catch (refreshError) {
          // Xử lý khi refresh thất bại (yêu cầu đăng nhập lại)
          localStorage.removeItem("jwt");
          return Promise.reject(refreshError);
        } finally {
          isRefreshing = false;
        }
      }

      return new Promise((resolve) => {
        subscribeTokenRefresh((newToken) => {
          originalRequest.headers.Authorization = `Bearer ${newToken}`;
          resolve(api(originalRequest));
        });
      });
    }
    return Promise.reject(error);
  }
);
export const register = (userData) => async (dispatch) => {
  dispatch({ type: REGISTER_REQUEST });
  try {
    const response = await api.post(`${API_BASE_URL}/auth/signup`, userData, { withCredentials: true });
    // { withCredentials: true } để gửi cookie
    const user = response.data;
    if (user.jwt) {
      localStorage.setItem("jwt", user.jwt);
      api.defaults.headers.Authorization = `Bearer ${user.jwt}`;
    }
    console.log("user", user);
    dispatch({ type: REGISTER_SUCCESS, payload: user });
  } catch (error) {
    dispatch({
      type: REGISTER_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

export const login = (userData) => async (dispatch) => {
  dispatch({ type: LOGIN_REQUEST });
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

    const response = await api.post(`${API_BASE_URL}/auth/signin`, userData, { withCredentials: true });
    const user = response.data;
    if (user.jwt) {
      localStorage.setItem("jwt", user.jwt);
      api.defaults.headers.Authorization = `Bearer ${user.jwt}`;
    }
    console.log("user", user);
    dispatch({ type: LOGIN_SUCCESS, payload: user });
  } catch (error) {
    dispatch({
      type: LOGIN_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

export const getUser = (jwt) => async (dispatch) => {
  dispatch({ type: GET_USER_REQUEST });
  try {
    const response = await api.get(`${API_BASE_URL}/api/users/profile`);
    const user = response.data;
    console.log("user", user);
    dispatch({ type: GET_USER_SUCCESS, payload: user });
  } catch (error) {
    dispatch({
      type: GET_USER_FAILURE,
      payload: error.response?.data || error.message,
    });
    
  }
};

export const logout = () => async (dispatch) => {
  try {
    await api.post(`${API_BASE_URL}/auth/logout`, {}, {withCredentials: true})
    dispatch({ type: LOGOUT });
    localStorage.clear();
    api.defaults.headers.Authorization = "";
  } catch (error) {
    dispatch({ type: LOGOUT }); // Thực hiện logout dù server thất bại
    localStorage.clear();
    api.defaults.headers.Authorization = "";
  }
};

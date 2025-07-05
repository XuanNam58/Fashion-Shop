import { api } from "../../config/apiConfig";
import {
  CREATE_RATING_FAILURE,
  CREATE_RATING_REQUEST,
  CREATE_RATING_SUCCESS,
  GET_PRODUCT_RATINGS_FAILURE,
  GET_PRODUCT_RATINGS_REQUEST,
  GET_PRODUCT_RATINGS_SUCCESS,
  GET_AVERAGE_RATING_REQUEST,
  GET_AVERAGE_RATING_SUCCESS,
  GET_AVERAGE_RATING_FAILURE,
} from "./ActionType";

export const createRating = (reqData) => async (dispatch) => {
  dispatch({ type: CREATE_RATING_REQUEST });
  try {
    const { data } = api.post("/api/ratings/create", reqData);
    dispatch({ type: CREATE_RATING_SUCCESS, payload: data });
  } catch (error) {
    dispatch({
      type: CREATE_RATING_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

export const getProductRatings = (reqData) => async (dispatch) => {
  const { productId } = reqData;
  dispatch({ type: GET_PRODUCT_RATINGS_REQUEST });
  try {
    const { data } = await api.get(`/api/ratings/product/${productId}`);
    dispatch({ type: GET_PRODUCT_RATINGS_SUCCESS, payload: data });
  } catch (error) {
    dispatch({
      type: GET_PRODUCT_RATINGS_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

export const getAverageRating = (productId) => async (dispatch) => {
  dispatch({ type: GET_AVERAGE_RATING_REQUEST });
  try {
    const { data } = await api.get(`/api/ratings/average/${productId}`);
    console.log("average rating", data);
    dispatch({ type: GET_AVERAGE_RATING_SUCCESS, payload: data });
  } catch (error) {
    dispatch({
      type: GET_AVERAGE_RATING_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

import { api } from "../../config/apiConfig";
import {
  CREATE_REVIEW_FAILURE,
  CREATE_REVIEW_REQUEST,
  CREATE_REVIEW_SUCCESS,
  GET_PRODUCT_REVIEWS_FAILURE,
  GET_PRODUCT_REVIEWS_REQUEST,
  GET_PRODUCT_REVIEWS_SUCCESS,
} from "./ActionType";

export const createReview = (reqData) => async (dispatch) => {
  dispatch({ type: CREATE_REVIEW_REQUEST });
  try {
    const { data } = api.post("/api/reviews/create", reqData);
    dispatch({ type: CREATE_REVIEW_SUCCESS, payload: data });
  } catch (error) {
    dispatch({
      type: CREATE_REVIEW_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

export const getProductReviews = (reqData) => async (dispatch) => {
  const { productId } = reqData;
  dispatch({ type: GET_PRODUCT_REVIEWS_REQUEST });
  try {
    const { data } = await api.get(`/api/reviews/product/${productId}`);
    dispatch({ type: GET_PRODUCT_REVIEWS_SUCCESS, payload: data });
  } catch (error) {
    dispatch({
      type: GET_PRODUCT_REVIEWS_FAILURE,
      payload: error.response?.data || error.message,
    });
  }
};

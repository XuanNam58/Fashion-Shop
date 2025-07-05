import {
  CREATE_REVIEW_FAILURE,
  CREATE_REVIEW_REQUEST,
  CREATE_REVIEW_SUCCESS,
  GET_PRODUCT_REVIEWS_FAILURE,
  GET_PRODUCT_REVIEWS_REQUEST,
  GET_PRODUCT_REVIEWS_SUCCESS,
} from "./ActionType";

const initialState = {
  reviews: [],
  review: null,
  loading: false,
  error: null,
};
export const reviewReducer = (state = initialState, action) => {
  switch (action.type) {
    case CREATE_REVIEW_REQUEST:
    case GET_PRODUCT_REVIEWS_REQUEST:
      return { ...state, loading: true, error: null };

    case CREATE_REVIEW_SUCCESS:
      return {
        ...state,
        loading: false,
        error: null,
        review: action.payload,
      };

    case GET_PRODUCT_REVIEWS_SUCCESS:
      return { ...state, loading: false, error: null, reviews: action.payload };

    case CREATE_REVIEW_FAILURE:
    case GET_PRODUCT_REVIEWS_FAILURE:
      return { ...state, loading: false, error: action.payload };

    default:
      return state;
  }
};

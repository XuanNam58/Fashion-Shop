import {
  CREATE_RATING_FAILURE,
  CREATE_RATING_REQUEST,
  CREATE_RATING_SUCCESS,
  GET_AVERAGE_RATING_FAILURE,
  GET_AVERAGE_RATING_REQUEST,
  GET_AVERAGE_RATING_SUCCESS,
  GET_PRODUCT_RATINGS_FAILURE,
  GET_PRODUCT_RATINGS_REQUEST,
  GET_PRODUCT_RATINGS_SUCCESS,
} from "./ActionType";

const initialState = {
  ratings: [],
  rating: null,
  loading: false,
  error: null,
  averageRating: 0,
  totalRatings: 0,
};
export const ratingReducer = (state = initialState, action) => {
  switch (action.type) {
    case CREATE_RATING_REQUEST:
    case GET_PRODUCT_RATINGS_REQUEST:
    case GET_AVERAGE_RATING_REQUEST:
      return { ...state, loading: true, error: null };

    case CREATE_RATING_SUCCESS:
      return {
        ...state,
        loading: false,
        error: null,
        rating: action.payload,
      };

    case GET_PRODUCT_RATINGS_SUCCESS:
      return { ...state, loading: false, error: null, ratings: action.payload };

    case GET_AVERAGE_RATING_SUCCESS:
      return {
        ...state,
        loading: false,
        averageRating: action.payload.averageRatings,
        totalRatings: action.payload.totalRatings,
      };

    case CREATE_RATING_FAILURE:
    case GET_PRODUCT_RATINGS_FAILURE:
    case GET_AVERAGE_RATING_FAILURE:
      return { ...state, loading: false, error: action.payload };

    default:
      return state;
  }
};

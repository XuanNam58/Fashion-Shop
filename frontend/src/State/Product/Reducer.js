import {
  FIND_PRODUCT_BY_ID_FAILURE,
  FIND_PRODUCT_BY_ID_REQUEST,
  FIND_PRODUCT_BY_ID_SUCCESS,
  FIND_PRODUCTS_FAILURE,
  FIND_PRODUCTS_REQUEST,
  FIND_PRODUCTS_SUCCESS,
  SET_PRODUCTS_BY_CATEGORY,
  SEARCH_SUGGESTIONS_REQUEST,
  SEARCH_SUGGESTIONS_SUCCESS,
  SEARCH_SUGGESTIONS_FAILURE,
  CLEAR_SEARCH_SUGGESTIONS,
  SEARCH_PRODUCTS_REQUEST,
  SEARCH_PRODUCTS_SUCCESS,
  SEARCH_PRODUCTS_FAILURE,
} from "./ActionType";

const initialState = {
  products: [],
  product: null,
  loading: false,
  error: null,
  productsByCategory: {
    vest: [],
    shoes: [],
    shirt: [],
    dress: [],
    jeans: []
    // ...
  },
  // Search suggestions state
  searchSuggestions: {
    data: [],
    loading: false,
    error: null,
  },
};

export const customerProductReducer = (state = initialState, action) => {
  switch (action.type) {
    case FIND_PRODUCTS_REQUEST:
    case FIND_PRODUCT_BY_ID_REQUEST:
      return { ...state, loading: true, error: null };

    case FIND_PRODUCTS_SUCCESS:
      return {
        ...state,
        loading: false,
        error: null,
        products: action.payload,
      };

    case FIND_PRODUCT_BY_ID_SUCCESS:
      return { ...state, loading: false, error: null, product: action.payload };

    case FIND_PRODUCTS_FAILURE:
    case FIND_PRODUCT_BY_ID_FAILURE:
      return { ...state, loading: false, error: action.payload };

    case SET_PRODUCTS_BY_CATEGORY:
      return {
        ...state,
        loading: false,
        error: null,
        productsByCategory: {
          ...state.productsByCategory,
          ...action.payload,
        },
      };

    // Search suggestions cases
    case SEARCH_SUGGESTIONS_REQUEST:
      return {
        ...state,
        searchSuggestions: {
          ...state.searchSuggestions,
          loading: true,
          error: null,
        },
      };

    case SEARCH_SUGGESTIONS_SUCCESS:
      return {
        ...state,
        searchSuggestions: {
          ...state.searchSuggestions,
          loading: false,
          error: null,
          data: action.payload,
        },
      };

    case SEARCH_SUGGESTIONS_FAILURE:
      return {
        ...state,
        searchSuggestions: {
          ...state.searchSuggestions,
          loading: false,
          error: action.payload,
          data: [],
        },
      };

    case CLEAR_SEARCH_SUGGESTIONS:
      return {
        ...state,
        searchSuggestions: {
          ...state.searchSuggestions,
          loading: false,
          error: null,
          data: [],
        },
      };

    // Search products cases
    case SEARCH_PRODUCTS_REQUEST:
      return {
        ...state,
        loading: true,
        error: null,
      };

    case SEARCH_PRODUCTS_SUCCESS:
      return {
        ...state,
        loading: false,
        error: null,
        products: action.payload,
      };

    case SEARCH_PRODUCTS_FAILURE:
      return {
        ...state,
        loading: false,
        error: action.payload,
        products: { content: [], totalElements: 0, totalPages: 0 },
      };

    default:
      return state;
  }
};

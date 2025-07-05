import { api } from "../../config/apiConfig";
import {
  FIND_PRODUCT_BY_ID_FAILURE,
  FIND_PRODUCT_BY_ID_REQUEST,
  FIND_PRODUCT_BY_ID_SUCCESS,
  FIND_PRODUCTS_FAILURE,
  FIND_PRODUCTS_REQUEST,
  FIND_PRODUCTS_SUCCESS,
  SEARCH_SUGGESTIONS_REQUEST,
  SEARCH_SUGGESTIONS_SUCCESS,
  SEARCH_SUGGESTIONS_FAILURE,
  CLEAR_SEARCH_SUGGESTIONS,
  SEARCH_PRODUCTS_REQUEST,
  SEARCH_PRODUCTS_SUCCESS,
  SEARCH_PRODUCTS_FAILURE,
} from "./ActionType";

export const findProducts = (reqData) => async (dispatch) => {
  dispatch({ type: FIND_PRODUCTS_REQUEST });
  const {
    category,
    colors,
    sizes,
    minPrice,
    maxPrice,
    minDiscount,
    sort,
    stock,
    pageNumber,
    pageSize,
  } = reqData;
  try {
    const { data } =
      await api.get(`/api/products?color=${colors}&size=${sizes}&minPrice=${minPrice}&maxPrice=${maxPrice}&minDiscount=${minDiscount}
      &category=${category}&stock=${stock}&sort=${sort}&pageNumber=${pageNumber}&pageSize=${pageSize}`);
    console.log("product data ", data);
    dispatch({ type: FIND_PRODUCTS_SUCCESS, payload: data });
  } catch (e) {
    dispatch({ type: FIND_PRODUCTS_FAILURE, payload: e.message });
  }
};

export const findProductById = (reqData) => async (dispatch) => {
  dispatch({ type: FIND_PRODUCT_BY_ID_REQUEST });
  const { productId } = reqData;
  try {
    const { data } = await api.get(`/api/products/id/${productId}`);

    dispatch({ type: FIND_PRODUCT_BY_ID_SUCCESS, payload: data });
  } catch (e) {
    dispatch({ type: FIND_PRODUCT_BY_ID_FAILURE, payload: e.message });
  }
};

export const findProductsByCategory = () => async (dispatch) => {
  const categories = ["vest", "shoes", "shirt", "dress", "jeans"];
  const results = await Promise.all(
    categories.map((cat) =>
      api.get(`/api/get-products-by-category?category=${cat}`).then(res => ({ [cat]: res.data }))
    )
  );
  // Gộp các object lại thành 1 object
  const productsByCategory = results.reduce((acc, cur) => ({ ...acc, ...cur }), {});
  dispatch({ type: "SET_PRODUCTS_BY_CATEGORY", payload: productsByCategory });
};

// Search suggestions action
export const fetchSearchSuggestions = (query, limit = 5) => async (dispatch) => {
  if (!query || query.trim().length === 0) {
    dispatch({ type: CLEAR_SEARCH_SUGGESTIONS });
    return;
  }

  dispatch({ type: SEARCH_SUGGESTIONS_REQUEST });
  
  try {
    const { data } = await api.get(
      `/api/products/suggestions?q=${encodeURIComponent(query.trim())}&limit=${limit}`
    );
    dispatch({ type: SEARCH_SUGGESTIONS_SUCCESS, payload: data });
  } catch (error) {
    dispatch({ type: SEARCH_SUGGESTIONS_FAILURE, payload: error.message });
  }
};

export const clearSearchSuggestions = () => (dispatch) => {
  dispatch({ type: CLEAR_SEARCH_SUGGESTIONS });
};

// Search products action
export const searchProducts = (searchParams) => async (dispatch) => {
  const { q = "", page = 0, size = 12, sort = "createdAt,desc" } = searchParams;
  
  dispatch({ type: SEARCH_PRODUCTS_REQUEST });
  
  try {
    let url = `/api/products/search?page=${page}&size=${size}&sort=${sort}`;
    if (q && q.trim().length > 0) {
      url += `&q=${encodeURIComponent(q.trim())}`;
    }
    
    const { data } = await api.get(url);
    dispatch({ type: SEARCH_PRODUCTS_SUCCESS, payload: data });
  } catch (error) {
    dispatch({ type: SEARCH_PRODUCTS_FAILURE, payload: error.message });
  }
};

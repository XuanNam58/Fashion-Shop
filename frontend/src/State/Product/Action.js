import { FIND_PRODUCTS_REQUEST } from "./ActionType";

const findProducts = (reqData) => async (dispatch) => {
    dispatch({type:FIND_PRODUCTS_REQUEST})
  const {
    colors,
    sizes,
    minPrice,
    maxPrice,
    minDiscount,
    maxDiscount,
    category,
    stock,
    sort,
    pageNumber,
    pageSize,
  } = reqData;
  try{
    

  }catch(e){

  }
};

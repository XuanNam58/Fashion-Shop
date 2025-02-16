import { applyMiddleware, combineReducers, legacy_createStore } from 'redux'
import { thunk } from 'redux-thunk'
import { authReducer } from './Auth/Reducer'
import { customerProductReducer } from './Product/Reducer'
import { cartReducer } from './Cart/Reducer'
import { orderReducer } from './Order/Reducer'

/* combineReducers: Hàm này dùng để kết hợp nhiều reducer lại thành một root reducer. 
Mỗi reducer sẽ quản lý một phần của state trong ứng dụng. */
const rootReducers = combineReducers({
    auth:authReducer,
    products:customerProductReducer,
    cart:cartReducer,
    order:orderReducer,
    
})
export const store = legacy_createStore(rootReducers, applyMiddleware(thunk))
/* applyMiddleware: Hàm này được sử dụng để áp dụng middleware vào Redux store. 
Middleware là một cách để can thiệp vào quá trình dispatch action trước khi nó đến reducer. 

    legacy_createStore: Đây là hàm dùng để tạo ra Redux store. 
Trong các phiên bản mới hơn của Redux, bạn có thể sử dụng configureStore thay vì legacy_createStore.

thunk: Redux Thunk là một middleware cho phép bạn viết các action creator trả về 
một hàm thay vì một action object. Điều này rất hữu ích khi bạn cần thực hiện các tác vụ bất đồng bộ như gọi API.
*/
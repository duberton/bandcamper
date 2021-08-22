import { applyMiddleware, createStore } from "redux";
import { authReducer } from "./auth/reducer";
import logger from "redux-logger";
import { persistStore, persistCombineReducers } from "redux-persist";
import storage from "redux-persist/lib/storage";

const persistConfig = {
  key: "auth",
  storage: storage,
  whitelist: ["auth"]
};

export const store = createStore(
  persistCombineReducers(persistConfig, {
    auth: authReducer
  }),
  applyMiddleware(logger)
);

export const persistor = persistStore(store);
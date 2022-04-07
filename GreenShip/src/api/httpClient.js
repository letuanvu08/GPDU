import axios from "axios";
import { Alert } from "react-native";
import storageKeys from "../utils/storageKeys";
import EncryptedStorage from "react-native-encrypted-storage";
import httpStatus from "./httpStatus";
const queryString = require("query-string");

const HOST = "http://localhost:8080/api";
const httpClient = axios.create({
  baseURL: HOST,
  timeout: 5000,
  headers: {
    "content-type": "application/json",
  },
  paramsSerializer: (params) => queryString.stringify(params),
});
httpClient.interceptors.request.use(async (config) => {
  try {
    const accessToken = await EncryptedStorage.getItem(
      storageKeys.ACCESS_TOKEN
    );
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
      return config;
    }
  } catch (e) {
    console.log("Config request err: ", e);
  }
  return config;
});

httpClient.interceptors.response.use(
  function (response) {
    if (response && response.data !== undefined) {
      return response.data;
    }
    Alert.alert("Kết nối không ổn định");
    return {};
  },
  async function (error) {
    try {
      if (error && error.response) {
        switch (error.response.status) {
          case httpStatus.UNAUTHORIZED:
            await processUnauthoried(error.response.config);
            break;
          case httpStatus.FORBBIDEN:
            Alert.alert("Bạn không có quyền truy cập");
            break;
          case httpStatus.NOT_FOUND:
            Alert.alert("Tài nguyên không tồn tại");
            break;
          case httpStatus.BAD_REQUEST:
            Alert.alert("Dữ liệu không hợp lệ");
            break;
          default:
            Alert.alert("Hệ thống đang bảo trì");
        }
      }
    } catch (e) {
      console.log("Response err: ", e);
    }
    return Promise.reject(error);
  }
);

const processUnauthoried = async (config) => {
  const refreshToken = await EncryptedStorage.getItem(
    storageKeys.REFRESH_TOKEN
  );
  if (refreshToken) {
    await EncryptedStorage.removeItem(storageKeys.ACCESS_TOKEN);
    await EncryptedStorage.removeItem(storageKeys.REFRESH_TOKEN);
    axios
      .post(`${HOST}/user/refresh-token`, { refreshToken: refreshToken })
      .then((res) => {
        if (res && res.data) {
          const promise1 = EncryptedStorage.setItem(
            storageKeys.ACCESS_TOKEN,
            res.data.token
          );
          const promise2 = EncryptedStorage.setItem(
            storageKeys.REFRESH_TOKEN,
            res.data.refresh_token
          );
          Promise.all([promise1, promise2]).then(() => {
            return;
          });
        }
      })
      .catch((err) => {
        console.log("Refresh Token: ", err);
      });
  }
};

export default httpClient;

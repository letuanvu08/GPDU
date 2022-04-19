import axios from "axios";
import httpStatus from "./httpStatus";
const queryString = require("query-string");

const HOST = "http://localhost:8080/api/admin/";
const httpClient = axios.create({
  baseURL: HOST,
  timeout: 5000,
  headers: {
    "content-type": "application/json",
  },
  paramsSerializer: (params) => queryString.stringify(params),
});
// httpClient.interceptors.request.use(async (config) => {
//   config.headers.Authorization = `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2MjRiYTQ2MDI0MjQwZDNlZWFhNzAzMWEiLCJ0eXBVc2VyIjoiQURNSU4iLCJ1c2VyTmFtZSI6InZ1MSIsImV4cCI6MTY1NTM2MzA3MywidXNlcklkIjoiNjI0YmE0NjAyNDI0MGQzZWVhYTcwMzFhIiwiaWF0IjoxNjUwMTc5MDczfQ.XqtOg61gsTtNOj011B1rp66M1ZBJmBXyxV9Avu5ikBc`;
//   return config;
// });

httpClient.interceptors.response.use(
  function (response) {
    if (response && response.data !== undefined) {
      return response.data;
    }
    console.log("Kết nối không ổn định");
    return {};
  },
  async function (error) {
    try {
      if (error && error.response) {
        switch (error.response.status) {
          case httpStatus.UNAUTHORIZED:
            console.log("Bạn không có token");
            break;
          case httpStatus.FORBBIDEN:
            console.log("Bạn không có quyền truy cập");
            break;
          case httpStatus.NOT_FOUND:
            console.log("Tài nguyên không tồn tại");
            break;
          case httpStatus.BAD_REQUEST:
            console.log("Dữ liệu không hợp lệ");
            break;
          default:
            console.log("Hệ thống đang bảo trì");
        }
      }
    } catch (e) {
      console.log("Response err: ", e);
    }
    return Promise.reject(error);
  }
);

export default httpClient;

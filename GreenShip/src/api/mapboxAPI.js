import axios from "axios";

const HOST = "https://api.mapbox.com/geocoding/v5/mapbox.places/";
const httpClient = axios.create({
  baseURL: HOST,
  timeout: 5000,
  headers: {
    "content-type": "application/json",
  },
  params: {
    access_token:
      "pk.eyJ1IjoidHVkYW9ia3UiLCJhIjoiY2tvZmQ2dnNtMDZlNjJwcG53bmw2YXRhYSJ9.quj8ggmNSoE2iKuzCOtU-g",
  },
});
const mapboxApi = {
  geocodingForward: ({ name }) => httpClient.get(),
  geocodingReverse: ({ longitude, latitude }) =>
    httpClient.get(`${longitude},${latitude}.json`),
};

export default mapboxApi;

import httpClient from "./httpClient";

export default {
  getVehicleInfoById: ({ vehicleId }) => {
    return httpClient.get(`/vehicles/${vehicleId}`);
  },
  getVehicleInfoByOwnerId: ({ ownerId }) => {
    return httpClient.get(`/vehicles/users/${ownerId}`);
  },
  updateLocationVehicle: ({ vehicleId, location }) => {
    return httpClient.post(`/vehicles/${vehicleId}/location`, location);
  },
  fetchVehicles: ({ offset, limit }) => {
    return httpClient.get("/vehicles", { params: { offset: offset, limit: limit } });
  },
};

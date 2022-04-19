import httpClient from './httpClient';
export default vehicleApi = {
  getVehicleInfoById: ({vehicleId}) => {
    return httpClient.get(`/vehicles/${vehicleId}`);
  },
  getVehicleInfoByOwnerId: ({ownerId}) => {
    return httpClient.get(`/vehicles/users/${ownerId}`);
  },
  updateLocationVehicle: ({vehicleId, location})=>{
   return httpClient.post(`/vehicles/${vehicleId}/location`, location)
  }
};

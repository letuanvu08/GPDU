import httpClient from './httpClient';

export default routingApi = {
  getRoutingByOrderId: ({orderId}) => {
    return httpClient.get(`/routing/orders/${orderId}`);
  },
  getRoutingByVehicleId: ({vehicleId}) => {
    return httpClient.get(`/routing/vehicles/${vehicleId}`);
  },
};

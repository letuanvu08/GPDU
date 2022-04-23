import httpClient from './httpClient';

const routingApi = {
  getRoutingByOrderId: ({orderId}) => {
    return httpClient.get(`/routing/orders/${orderId}`);
  },
  getRoutingByVehicleId: ({vehicleId}) => {
    return httpClient.get(`/routing/vehicles/${vehicleId}`);
  },
};

export default routingApi;

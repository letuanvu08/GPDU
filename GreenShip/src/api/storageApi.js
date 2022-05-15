import httpClient from './httpClient';

export default storageApi = {
  getRoutingByOrderId: () => {
    return httpClient.get(`/api/storage`);
  },
};
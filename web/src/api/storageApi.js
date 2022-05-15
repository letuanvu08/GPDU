import httpClient from './httpClient';

const storageApi = {
  getRoutingByOrderId: () => {
    return httpClient.get(`/storages`);
  },
};

export default storageApi;
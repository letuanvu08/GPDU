import httpClient from './httpClient';

export default orderApi = {
  createOrder: ({
    senderName,
    senderPhone,
    senderAddress,
    senderLocation,
    senderEarliestTime,
    senderLatestTime,
    receiverName,
    receiverPhone,
    receiverAddress,
    receiverLocation,
    receiverEarliestTime,
    receiverLatestTime,
    packageCategory,
    packageDescription,
    packageWeight,
    note,
  }) =>
    httpClient.post('/orders', {
      pickup: {
        customerName: senderName,
        phone: senderPhone,
        address: senderAddress,
        location: senderLocation,
        earliestTime: senderEarliestTime,
        latestTime: senderLatestTime,
      },
      delivery: {
        customerName: receiverName,
        phone: receiverPhone,
        address: receiverAddress,
        location: receiverLocation,
        earliestTime: receiverEarliestTime,
        latestTime: receiverLatestTime,
      },
      note,
      packageInfo: {
        category: packageCategory,
        name: packageDescription,
        weight: packageWeight,
      },
    }),
  fetchOrders: ({offset, limit}) =>
    httpClient.get('/orders', {params: {offset, limit: 5}}),

  getOrderById: ({orderId}) => httpClient.get(`/orders/${orderId}`),
  updateOrderStatus: ({orderId, status})=>
   httpClient.post(`/orders/${orderId}/status`, status)
};

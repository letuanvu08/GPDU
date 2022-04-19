import httpClient from "./httpClient";

export function fetchListOrders({ offset, limit }) {
  return httpClient.get("/orders", { params: { offset, limit: 4 } });
}

export function getOrderById({ orderId }) {
  return httpClient.get(`/orders/${orderId}`);
}
export default {
  fetchListOrders,
  getOrderById,
}

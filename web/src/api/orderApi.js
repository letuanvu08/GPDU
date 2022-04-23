import httpClient from "./httpClient";

export function fetchListOrders({ offset, limit }) {
  return httpClient.get("/orders", { params: { offset, limit: limit } });
}

export function getOrderById({ orderId }) {
  return httpClient.get(`/orders/${orderId}`);
}
export default {
  fetchListOrders,
  getOrderById,
}

import httpClient from "./httpClient";

export default orderApi = {
  createOrder: ({
    senderName,
    senderPhone,
    senderAddress,
    senderLocation,
    senderEarliestTime,
    senderLastestTime,
    receiverName,
    receiverPhone,
    receiverAddress,
    receiverLocation,
    receiverEarliestTime,
    receiverLastestTime,
    packageCategory,
    packageDescription,
    packageWeight,
    note,
  }) =>
    httpClient.post("/orders", {
      pickup: {
        customerName: senderName,
        phone: senderPhone,
        address: senderAddress,
        location: senderLocation,
        earliestTime: senderEarliestTime,
        lastestTime: senderLastestTime,
      },
      delivery: {
        customerName: receiverName,
        phone: receiverPhone,
        address: receiverAddress,
        location: receiverLocation,
        earliestTime: receiverEarliestTime,
        lastestTime: receiverLastestTime,
      },
      note,
      packageInfo: {
        category: packageCategory,
        name: packageDescription,
        weight: packageWeight,
      },
    }),
};

export default {
  MarkerData: [
    {
      orderID: '1',
      latitude: 10.775948327861144,
      longitude: 106.69959749384073,
      color: '#2F3136',
      name: 'Museum of Ho Chi Minh City',
      direction: 'Ly Tu Trong Street',
      type: 'send',
    },
    {
      orderID: '2',
      latitude: 10.765259921335089,
      longitude: 106.68125710978151,
      color: '#2F3136',
      name: 'Công viên Âu Lạc, Đường Hùng Vương, Ward 1, District 10, ',
      direction: 'Đường Hùng Vương, Ward 1, District 10',
      type: 'send',
    },
    {
      orderID: '2',
      latitude: 10.8797358,
      longitude: 106.8061911,
      name: 'Ho Chi Minh city University of Technology',
      direction:
        'Tạ Quang Bửu, Vietnam National University HCMC, Di An, Binh Duong province, Vietnam',
      type: 'receive',
    },
    {
      orderID: '1',
      latitude: 10.861733687480958,
      longitude: 106.78058970394318,
      name: 'Thu Duc General Hospital, ',
      direction: 'Đường số 16, Linh Trung Ward, Thu Duc City, Vietnam',
      type: 'receive',
    },
  ],
  coordinatesLine: [
    {latitude: 10.775948327861144, longitude: 106.69959749384073},
    {latitude: 10.765259921335089, longitude: 106.68125710978151},
    {latitude: 10.8797358, longitude: 106.8061911},
    {latitude: 10.861733687480958, longitude: 106.78058970394318},
  ],
  order: {
    id: '1234',
    sender: {
      name: 'le tuan vu',
      phone: '0919523753',
      address: ' Linh Trung, Thu Duc',
      coordinate: {
        latitude: 10.861733687480958,
        longitude: 106.78058970394318,
      },
    },
    recipient: {
      name: 'Dao Thanh Tu',
      phone: '0919523753',
      address: 'Ly Thuong kiet, Q10, HCM',
      coordinate: {
        latitude: 10.8797358,
        longitude: 106.8061911,
      },
    },
    package: {
      category: 'Device',
      name: 'Iphone 13 pro max',
      weight: 1,
    },
    status: {
      pickup: {
        status: 'finish',
        timestamp: 1648908095326,
      },
      delivery: {
        status: 'unfinish',
        timestamp: 1648908095326,
      },
    },
    distance: 1,
  },
};

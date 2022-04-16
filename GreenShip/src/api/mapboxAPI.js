import axios from 'axios';

const HOST = 'https://api.mapbox.com';
const httpClient = axios.create({
  baseURL: HOST,
  timeout: 5000,
  headers: {
    'content-type': 'application/json',
  },
  params: {
    access_token:
      'pk.eyJ1IjoibGV0dWFudnUiLCJhIjoiY2wwZXRhZ2Y0MGwwZDNrbGNxZmF3OXlhbiJ9.EhStD2wRsGq65yD_bGhk9Q',
  },
});
const mapboxApi = {
  geocodingForward: ({name}) => httpClient.get('/geocoding/v5/mapbox.places/'),
  geocodingReverse: ({longitude, latitude}) =>
    httpClient.get(`/geocoding/v5/mapbox.places/${longitude},${latitude}.json`),
  direction: locations => {
    const strLoc = locations
      .map(location => location.longitude + ',' + location.latitude)
      .join(';');
      console.log("str location: ", strLoc);
    return httpClient.get(`/directions/v5/mapbox/driving-traffic/${strLoc}`,{
      params: {
        geometries:'geojson',
      }
    });
  },
};

export default mapboxApi;

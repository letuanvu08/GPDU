package hcmut.thesis.gpduserver.service;

import hcmut.thesis.gpduserver.config.MapboxConfig;
import hcmut.thesis.gpduserver.constants.mapbox.NameParamRequest;
import hcmut.thesis.gpduserver.models.entity.Location;
import hcmut.thesis.gpduserver.models.reponse.mapBox.MapBoxResponse;
import hcmut.thesis.gpduserver.models.reponse.mapBox.MapBoxResponse.Routes;
import hcmut.thesis.gpduserver.models.request.mapbox.MapboxDirectionRequest;
import hcmut.thesis.gpduserver.service.http.HttpService;
import hcmut.thesis.gpduserver.utils.GsonUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class MapBoxService {

  @Autowired
  private HttpService httpService;

  @Autowired
  private MapboxConfig config;

  public List<Routes> getDirection(MapboxDirectionRequest request) {
    List<Location> locations = request.getLocations();
    try {
      UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
      String coordinates = locations.stream()
          .map(location -> location.getLongitude().toString() + "," + location.getLatitude().toString())
          .collect(Collectors.joining(";"));

      String url = uriComponentsBuilder.scheme(config.getScheme())
          .host(config.getDomain())
          .path(config.getPathDirections())
          .path(coordinates)
          .queryParam(NameParamRequest.ACCESS_TOKEN, config.getToken())
          .queryParam(NameParamRequest.GEOMETRIES, "geojson")
//          .queryParam(NameParamRequest.OVERVIEW, "full")
//          .queryParam(NameParamRequest.ANNOTATIONS, "distance,duration,speed,congestion,congestion_numeric,closure")
          .toUriString();
      log.info("getDirection: , locations: {}, request: {}", GsonUtils.toJsonString(locations), url);
      String response = httpService.sendGet(url, new HttpHeaders());
      log.info("getDirection: url: {}, response: {}", url, response);
      MapBoxResponse result = GsonUtils.fromJsonSnakeCase(response, MapBoxResponse.class);
      if ("Ok".equals(result.getCode())) {
        return result.getRoutes();
      }
      return null;
    } catch (Exception e) {
      log.error("getDirection: , locations: {}, exception: {}", GsonUtils.toJsonString(locations),
          e.getMessage());
      return null;
    }
  }
}

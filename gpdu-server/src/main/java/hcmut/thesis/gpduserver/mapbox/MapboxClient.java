package hcmut.thesis.gpduserver.mapbox;

import com.alibaba.fastjson.JSON;
import hcmut.thesis.gpduserver.config.MapboxConfig;
import hcmut.thesis.gpduserver.mapbox.commands.CallMatrixAPICommand;
import hcmut.thesis.gpduserver.mapbox.commands.GetDurationCommand;
import hcmut.thesis.gpduserver.mapbox.responses.DirectionResponse;
import hcmut.thesis.gpduserver.mapbox.responses.GeocodingResponse;
import hcmut.thesis.gpduserver.mapbox.responses.MatrixResponse;
import hcmut.thesis.gpduserver.models.entity.Location;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MapboxClient implements IMapboxClient {
    @Autowired
    private MapboxConfig mapboxConfig;
    @Autowired
    private OkHttpClient client;


    private Optional<List<List<Float>>> callMatrixAPI(CallMatrixAPICommand command) {
        String coordinates = command.getLocations().stream()
                .map(l -> l.getLongitude() + "," + l.getLatitude()).collect(Collectors.joining(";"));
        final String MATRIX_URL = "https://api.mapbox.com/directions-matrix/v1/mapbox/driving/";
        HttpUrl.Builder urlBuilder = HttpUrl.parse(MATRIX_URL + coordinates).newBuilder();
        urlBuilder.addQueryParameter("access_token", mapboxConfig.getToken());
        if (command.getSources() != null) {
            urlBuilder.addQueryParameter("sources", StringUtils.join(command.getSources(), ";"));
        }
        if (command.getDestinations() != null)
            urlBuilder.addQueryParameter("destinations", StringUtils.join(command.getDestinations(), ";"));
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                MatrixResponse matrixResponse = JSON.parseObject(response.body().string(), MatrixResponse.class);
                return Optional.ofNullable(matrixResponse.getDurations());
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Float getDuration(GetDurationCommand command) {
        final String DIRECTION_URL = "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/%f,%f;%f,%f";
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(String.format(DIRECTION_URL,
                command.getFromLocation().getLongitude(), command.getFromLocation().getLatitude(),
                command.getToLocation().getLongitude(), command.getToLocation().getLatitude())).newBuilder();
        urlBuilder.addQueryParameter("access_token", mapboxConfig.getToken());
        if (StringUtils.isNotBlank(command.getDepartAt())) {
            urlBuilder.addQueryParameter("depart_at", command.getDepartAt());
        }
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                DirectionResponse directionResponse = JSON.parseObject(response.body().string(), DirectionResponse.class);
                return directionResponse.getRoutes().get(0).getDuration();
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
        return 0f;
    }

    @Override
    public Optional<List<List<Float>>> retrieveDurationMatrix(List<Location> locations) {
        if (locations.size() > 48) return Optional.empty();
        if (locations.size() > 25) {
            List<Location> subLocations = locations.subList(0, 25);
            CallMatrixAPICommand command = CallMatrixAPICommand.builder()
                    .locations(subLocations)
                    .build();
            Optional<List<List<Float>>> matrixResponse = this.callMatrixAPI(command);
            if (matrixResponse.isEmpty()) return Optional.empty();
            List<List<Float>> result = matrixResponse.get();
            for (int i = 25; i < locations.size(); i++) {
                subLocations.set(command.getLocations().size() - 1, locations.get(i));
                command = CallMatrixAPICommand.builder()
                        .locations(subLocations)
                        .sources(List.of(command.getLocations().size() - 1))
                        .build();
                Optional<List<List<Float>>> res = this.callMatrixAPI(command);
                if (res.isEmpty()) return Optional.empty();
                result.add((res.get().get(0)));
                command = CallMatrixAPICommand.builder()
                        .locations(subLocations)
                        .destinations(List.of(command.getLocations().size() - 1))
                        .build();
                res = this.callMatrixAPI(command);
                if (res.isEmpty()) return Optional.empty();
                for (int j = 0; j < 24; j++) {
                    result.get(j).add(res.get().get(j).get(0));
                }
            }
            command = CallMatrixAPICommand.builder()
                    .locations(locations.subList(24, locations.size()))
                    .build();
            Optional<List<List<Float>>> res = this.callMatrixAPI(command);
            if (res.isEmpty()) return Optional.empty();
            for (int i = 24; i < locations.size(); i++) {
                result.get(i).remove(24);
                result.get(i).addAll(res.get().get(i - 24));
            }
            return Optional.of(result);
        }
        CallMatrixAPICommand command = CallMatrixAPICommand.builder()
                .locations(locations.subList(0, locations.size()))
                .build();
        return this.callMatrixAPI(command);
    }

    @Override
    public String reverseGeocoding(Location location) {
        final String REVERSE_GEOCODING_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/%f,%f.json";
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(String.format(REVERSE_GEOCODING_URL,
                location.getLongitude(), location.getLatitude())).newBuilder();
        urlBuilder.addQueryParameter("access_token", mapboxConfig.getToken());
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                GeocodingResponse geocodingResponse = JSON.parseObject(response.body().string(), GeocodingResponse.class);
                return geocodingResponse.getFeatures().get(0).getPlace_name();
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
        return "";
    }

}

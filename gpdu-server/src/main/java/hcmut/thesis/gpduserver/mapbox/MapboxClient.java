package hcmut.thesis.gpduserver.mapbox;

import com.alibaba.fastjson.JSON;
import hcmut.thesis.gpduserver.config.MapboxConfig;
import hcmut.thesis.gpduserver.mapbox.commands.GetDurationCommand;
import hcmut.thesis.gpduserver.mapbox.responses.DirectionResponse;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Objects;

@Component
public class MapboxClient implements IMapboxClient {
    @Autowired
    private MapboxConfig mapboxConfig;
    @Autowired
    private OkHttpClient client;

    private final String BASE_URL = "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/%f,%f;%f,%f";

    @Override
    public Float getDuration(GetDurationCommand command) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(String.format(BASE_URL,
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
}

package net.majorProject.journalApp.service;

import net.majorProject.journalApp.api.response.WeatherResponse;
import net.majorProject.journalApp.entity.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;
    private static final String API="https://api.weatherapi.com/v1/current.json?key=API_KEY&q=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public Weather getWeather(String city){
        String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body=response.getBody();
        Weather weather = new Weather();
        if(body != null && body.getCurrent() != null) {
            weather.setTemperature(
                    body.getCurrent().getTemperature()
            );
            weather.setCondition(
                    body.getCurrent()
                            .getCondition()
                            .getText()
            );
        }

        return weather;
    }
}

package com.sda.weather.forecast;

import com.sda.weather.exception.BadRequestException;
import com.sda.weather.exception.InternalServerException;
import com.sda.weather.forecast.client.ForecastClient;
import com.sda.weather.location.Location;
import com.sda.weather.location.LocationService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class ForecastService {

    private final LocationService locationService;
    private final ForecastClient forecastClient;
    private final ForecastRepository forecastRepository;

    Forecast getForecast(String locationId, String period) {
        period = (period == null || period.isBlank()) ? "1" : period;

        int periodValue;
        try {
            periodValue = Integer.parseInt(period);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Period value must be a number");
        }
        if (periodValue > 5 || periodValue < 1) {
            throw new BadRequestException("Period value must be in the range <1;5>");
        }

        Location location = locationService.getLocationById(locationId)
                .orElseThrow(() -> new BadRequestException("Location " + locationId + " does not exist"));

        LocalDate now = LocalDate.now();
        LocalDate forecastDate = now.plusDays(periodValue);

        return forecastRepository.findByLocationAndCreationDateAndForecastDate(location, now, forecastDate)
                .orElseGet(() -> generateForecast(location, forecastDate));
    }

    private Forecast generateForecast(Location location, LocalDate forecastDate) {
        Forecast forecast = forecastClient.getForecast((double) location.getLatitude(), (double) location.getLongitude(), forecastDate)
                .orElseThrow(() -> new InternalServerException("Forecast cannot be generated: " + location.getCity()));
        forecast.setLocation(location);
        forecast.setCreationDate(LocalDate.now());

        return forecastRepository.save(forecast);
    }
}

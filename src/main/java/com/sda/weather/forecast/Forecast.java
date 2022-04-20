package com.sda.weather.forecast;

import com.sda.weather.location.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float temperature;
    private int pressure;
    private int humidity;
    private float windSpeed;
    private int windDirection;
    private LocalDate creationDate;
    private LocalDate forecastDate;
    @ManyToOne
    private Location location;
}

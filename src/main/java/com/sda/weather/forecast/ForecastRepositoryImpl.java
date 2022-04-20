package com.sda.weather.forecast;

import com.sda.weather.location.Location;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ForecastRepositoryImpl implements ForecastRepository {

    private final SessionFactory sessionFactory;

    @Override
    public Forecast save(Forecast forecast) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(forecast);

        transaction.commit();
        session.close();

        return forecast;
    }

    @Override
    public Optional<Forecast> findByLocationAndCreationDateAndForecastDate(Location location, LocalDate creationDate, LocalDate forecastDate) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<Forecast> results = session.createQuery("select f from Forecast f where f.location.id = :locationId and f.creationDate = :creationDate and f.forecastDate = :forecastDate", Forecast.class)
                .setParameter("locationId", location.getId())
                .setParameter("creationDate", creationDate)
                .setParameter("forecastDate", forecastDate)
                .getResultList();

        transaction.commit();
        session.close();

        return results.stream().findFirst();
    }
}

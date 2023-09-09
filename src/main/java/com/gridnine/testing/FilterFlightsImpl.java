package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FilterFlightsImpl implements FilterFlights {
    private List<Flight> flights;

    public FilterFlightsImpl(List<Flight> flights) {
        this.flights = new ArrayList<>(flights);
    }

    @Override
    public List<Flight> build() {
        return flights;
    }

    @Override
    public FilterFlights departureBeforeNow() {
        flights.removeIf(flight -> flight.getSegments().stream().
                anyMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now())));
        return this;
    }


    @Override
    public FilterFlights arrivalBeforeDeparture() {
        flights.removeIf(flight -> flight.getSegments().stream().
                anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())));
        return this;
    }

    @Override
    public FilterFlights timeOnGroundMoreThanTwoHours() {
        flights.removeIf(flight -> {
            List<Segment> segments = flight.getSegments();
            LocalDateTime curDeparture;
            LocalDateTime lastArrival;
            Duration duration = Duration.ZERO;

            for (int i = 1; i < segments.size(); i++) {
                curDeparture = segments.get(i).getDepartureDate();
                lastArrival = segments.get(i-1).getArrivalDate();
                duration = duration.plus(Duration.between(curDeparture, lastArrival).abs());
            }
            return duration.toHours() >= 2;
        });
        return this;
    }
}

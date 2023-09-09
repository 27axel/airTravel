package com.gridnine.testing;

import java.util.List;

public interface FilterFlights {
    List<Flight> build();
    FilterFlights departureBeforeNow();

    FilterFlights arrivalBeforeDeparture();

    FilterFlights timeOnGroundMoreThanTwoHours();
}

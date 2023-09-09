package com.gridnine.testing;


import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        List<Flight> departureBeforeNow = new FilterFlightsImpl(flights).departureBeforeNow().build();
        List<Flight> arrivalBeforeDeparture = new FilterFlightsImpl(flights).arrivalBeforeDeparture().build();
        List<Flight> timeOnGroundMoreThanTwoHours = new FilterFlightsImpl(flights).timeOnGroundMoreThanTwoHours().build();

        System.out.println(flights);
        System.out.println(departureBeforeNow);
        System.out.println(arrivalBeforeDeparture);
        System.out.println(timeOnGroundMoreThanTwoHours);
    }
}

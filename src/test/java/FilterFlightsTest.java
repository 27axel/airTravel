import com.gridnine.testing.FilterFlights;
import com.gridnine.testing.FilterFlightsImpl;
import com.gridnine.testing.Flight;
import com.gridnine.testing.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterFlightsTest {
    static LocalDateTime inTwoHours = LocalDateTime.now().plusHours(2);
    static LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
    FilterFlights filterFlights;

    List<Flight> testFlights;
    List<Flight> flightsDeparturesBeforeNow;
    List<Flight> moreThanTwoHoursOnGroundFlights;
    List<Flight> arrivalBeforeDeparture;

    public void initFlightsDeparturesBeforeNow() {
        flightsDeparturesBeforeNow = new ArrayList<>();
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);

        List<Segment> singleSegment = new ArrayList<>();
        singleSegment.add(new Segment(yesterday, yesterday.plusHours(4)));
        Flight singleSegmentFlight = new Flight(singleSegment);

        List<Segment> singleSegmentFromPastToFuture = new ArrayList<>();
        singleSegmentFromPastToFuture.add(new Segment(LocalDateTime.now().minusHours(5), LocalDateTime.now().plusHours(3)));
        Flight normalSingleSegmentFromYesterdayToTodayFlight = new Flight(singleSegmentFromPastToFuture);

        List<Segment> normalMultiSegments = new ArrayList<>();
        normalMultiSegments.add(new Segment(threeDaysAgo, threeDaysAgo.plusHours(4)));
        normalMultiSegments.add(new Segment(threeDaysAgo.plusHours(5).plusMinutes(30), threeDaysAgo.plusHours(10)));
        Flight normalMultiSegmentFlight = new Flight(normalMultiSegments);

        flightsDeparturesBeforeNow.add(singleSegmentFlight);
        flightsDeparturesBeforeNow.add(normalSingleSegmentFromYesterdayToTodayFlight);
        flightsDeparturesBeforeNow.add(normalMultiSegmentFlight);
    }

    public void initMoreThanTwoHoursOnGroundFlights() {
        moreThanTwoHoursOnGroundFlights = new ArrayList<>();
        List<Segment> multiSegmentMoreThanOneHour = new ArrayList<>();
        multiSegmentMoreThanOneHour.add(new Segment(inTwoHours, inTwoHours.plusHours(4)));
        multiSegmentMoreThanOneHour.add(new Segment(inTwoHours.plusHours(6).plusMinutes(1), inTwoHours.plusHours(10)));
        Flight singleSegmentFlight = new Flight(multiSegmentMoreThanOneHour);

        List<Segment> multiSegmentsThreeHour = new ArrayList<>();
        multiSegmentsThreeHour.add(new Segment(tomorrow, tomorrow.plusHours(2)));
        multiSegmentsThreeHour.add(new Segment(tomorrow.plusHours(3), tomorrow.plusHours(7)));
        multiSegmentsThreeHour.add(new Segment(tomorrow.plusHours(8).plusMinutes(1), tomorrow.plusHours(12)));
        Flight multiSegmentFlight = new Flight(multiSegmentsThreeHour);

        moreThanTwoHoursOnGroundFlights.add(singleSegmentFlight);
        moreThanTwoHoursOnGroundFlights.add(multiSegmentFlight);
    }

    public void initArrivalBeforeDeparture() {
        arrivalBeforeDeparture = new ArrayList<>();
        List<Segment> singleSegment = new ArrayList<>();
        singleSegment.add(new Segment(tomorrow, tomorrow.minusHours(4)));
        Flight singleSegmentFlight = new Flight(singleSegment);

        List<Segment> multiSegment = new ArrayList<>();
        multiSegment.add(new Segment(tomorrow, tomorrow.minusHours(4)));
        multiSegment.add(new Segment(tomorrow.minusHours(5), tomorrow.minusHours(10)));
        Flight multiSegmentFlight = new Flight(multiSegment);

        arrivalBeforeDeparture.add(singleSegmentFlight);
        arrivalBeforeDeparture.add(multiSegmentFlight);
    }

    @BeforeEach
    void setUp() {
        testFlights = new ArrayList<>();

        initFlightsDeparturesBeforeNow();
        initMoreThanTwoHoursOnGroundFlights();
        initArrivalBeforeDeparture();

        testFlights.addAll(flightsDeparturesBeforeNow);
        testFlights.addAll(moreThanTwoHoursOnGroundFlights);
        testFlights.addAll(arrivalBeforeDeparture);

        filterFlights = new FilterFlightsImpl(testFlights);
    }

    @Test
    void departureBeforeNowTest() {
        List<Flight> expectedFlights = new ArrayList<>(testFlights);
        expectedFlights.removeAll(flightsDeparturesBeforeNow);

        List<Flight> filteredFlights = filterFlights
                .departureBeforeNow()
                .build();
        assertEquals(expectedFlights, filteredFlights);
    }

    @Test
    void arrivalBeforeDeparture() {
        List<Flight> expectedFlight = new ArrayList<>(testFlights);
        expectedFlight.removeAll(arrivalBeforeDeparture);

        List<Flight> filteredFlights = filterFlights
                .arrivalBeforeDeparture()
                .build();
        assertEquals(expectedFlight, filteredFlights);
    }

    @Test
    void timeOnGroundMoreThanTwoHoursTest() {
        List<Flight> expectedFlight = new ArrayList<>(testFlights);
        expectedFlight.removeAll(moreThanTwoHoursOnGroundFlights);

        List<Flight> filteredFlights = filterFlights
                .timeOnGroundMoreThanTwoHours()
                .build();
        assertEquals(expectedFlight, filteredFlights);
    }
}

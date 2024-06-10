package org.max.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.max.models.*;
import org.max.repositories.AmenityRepository;
import org.max.repositories.hotel.HotelRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel testHotel1;

    private Hotel testHotel2;

    private Hotel testHotel3;

    @BeforeEach
    void setUp() {

        Contacts testContacts1 = new Contacts();
        testContacts1.setPhone("+375440000000");
        testContacts1.setEmail("test1@mail.com");

        Contacts testContacts2 = new Contacts();
        testContacts2.setPhone("+375441111111");
        testContacts2.setEmail("test2@mail.com");

        Address testAddress1 = new Address();
        testAddress1.setCity("Test city1");
        testAddress1.setCountry("Test country1");
        testAddress1.setStreet("Test street1");
        testAddress1.setHouseNumber(1);
        testAddress1.setPostCode("220004");

        Address testAddress2 = new Address();
        testAddress2.setCity("Test city2");
        testAddress2.setCountry("Test country2");
        testAddress2.setStreet("Test street2");
        testAddress2.setHouseNumber(2);
        testAddress2.setPostCode("220004");

        ArrivalTime testArrivalTime1 = new ArrivalTime();
        testArrivalTime1.setCheckIn(LocalTime.of(10, 20));
        testArrivalTime1.setCheckOut(LocalTime.of(22, 20));

        ArrivalTime testArrivalTime2 = new ArrivalTime();
        testArrivalTime2.setCheckIn(LocalTime.of(2, 10));
        testArrivalTime2.setCheckOut(LocalTime.of(12, 50));

        Set<Amenity> testAmenities1 = new HashSet<>();
        testAmenities1.add(new Amenity(1L, "test amenity1"));
        testAmenities1.add(new Amenity(2L, "test amenity2"));
        testAmenities1.add(new Amenity(3L, "test amenity3"));

        Set<Amenity> testAmenities2 = new HashSet<>();

        testHotel1 = new Hotel();
        testHotel1.setName("test hotel1");
        testHotel1.setBrand("test brand1");
        testHotel1.setDescription("test description1");
        testHotel1.setAddress(testAddress1);
        testHotel1.setContacts(testContacts1);
        testHotel1.setArrivalTime(testArrivalTime1);
        testHotel1.setAmenities(testAmenities1);

        testHotel2 = new Hotel();
        testHotel2.setName("test hotel2");
        testHotel2.setBrand("test brand2");
        testHotel2.setDescription("test description2");
        testHotel2.setAddress(testAddress2);
        testHotel2.setContacts(testContacts2);
        testHotel2.setArrivalTime(testArrivalTime2);
        testHotel2.setAmenities(testAmenities2);

        testHotel3 = new Hotel();
        testHotel3.setName("test hotel1");
        testHotel3.setBrand("test brand1");
        testHotel3.setDescription("test description1");
        testHotel3.setAddress(testAddress1);
        testHotel3.setContacts(testContacts1);
        testHotel3.setArrivalTime(testArrivalTime1);
        testHotel3.setAmenities(testAmenities1);
    }


    @Test
    void addHotel_ValidHotel_SaveHotel() {

        hotelService.addHotel(testHotel1);

        verify(hotelRepository, times(1)).save(testHotel1);
    }


    @Test
    void findByHotelById_HotelExists_ReturnHotel() {

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(testHotel1));

        Hotel hotel = hotelService.findByHotelById(1L);

        assertNotNull(hotel);
        assertEquals(testHotel1, hotel);
        verify(hotelRepository, times(1)).findById(1L);
    }


    @Test
    void findByHotelById_HotelDoesNotExist_ThrowEntityNotFoundException() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                                                            () -> hotelService.findByHotelById(1L));

        assertEquals("Hotel with id '1' not found.", exception.getMessage());
        verify(hotelRepository, times(1)).findById(1L);
    }


    @Test
    void findAllHotels_HotelsExist_ReturnHotelList() {

        when(hotelRepository.findAll()).thenReturn(Arrays.asList(testHotel1, testHotel2));

        List<Hotel> hotels = hotelService.findAllHotels();

        assertNotNull(hotels);
        assertEquals(2, hotels.size());
        assertTrue(hotels.contains(testHotel1));
        assertTrue(hotels.contains(testHotel2));
        verify(hotelRepository, times(1)).findAll();
    }


    @Test
    void findAllHotels_NoHotelsExist_ReturnEmptyList() {

        when(hotelRepository.findAll()).thenReturn(Collections.emptyList());

        List<Hotel> hotels = hotelService.findAllHotels();

        assertNotNull(hotels);
        assertTrue(hotels.isEmpty());
        verify(hotelRepository, times(1)).findAll();
    }


    @Test
    void searchHotels_WithMultipleMatchingHotels_ReturnsExpectedHotels() {
        List<Hotel> expectedHotels = List.of(testHotel1, testHotel3);

        String name = testHotel1.getName();
        String brand = testHotel1.getBrand();
        String city = testHotel1.getAddress().getCity();
        String country = testHotel1.getAddress().getCountry();
        List<String> amenities = testHotel1.getAmenities().stream().map(Amenity::getName).toList();

        when(hotelRepository.searchHotels(name, brand, city, country, amenities)).thenReturn(expectedHotels);

        List<Hotel> actualHotels = hotelService.searchHotels(name, brand, city, country, amenities);

        assertEquals(expectedHotels, actualHotels);
        assertEquals(expectedHotels.size(), actualHotels.size());
        verify(hotelRepository, times(1)).searchHotels(name, brand, city, country, amenities);
    }


    @Test
    void searchHotels_WithSingleMatchingHotel_ReturnsExpectedHotel() {

        List<Hotel> expectedHotels = List.of(testHotel2);

        String name = testHotel2.getName();
        String brand = testHotel2.getBrand();
        String city = testHotel2.getAddress().getCity();
        String country = testHotel2.getAddress().getCountry();
        List<String> amenities = testHotel2.getAmenities().stream().map(Amenity::getName).toList();

        when(hotelRepository.searchHotels(name, brand, city, country, amenities)).thenReturn(expectedHotels);

        List<Hotel> actualHotels = hotelService.searchHotels(name, brand, city, country, amenities);

        assertEquals(expectedHotels, actualHotels);
        assertEquals(expectedHotels.size(), actualHotels.size());
        verify(hotelRepository, times(1)).searchHotels(name, brand, city, country, amenities);
    }


    @Test
    void searchHotels_WithAllParametersNull_ReturnsAllHotels() {

        List<Hotel> expectedHotels = List.of(testHotel1, testHotel2, testHotel3);

        String name = null;
        String brand = null;
        String city = null;
        String country = null;
        List<String> amenities = null;

        when(hotelRepository.searchHotels(name, brand, city, country, amenities)).thenReturn(expectedHotels);

        List<Hotel> actualHotels = hotelService.searchHotels(name, brand, city, country, amenities);

        assertEquals(expectedHotels, actualHotels);
        assertEquals(expectedHotels.size(), actualHotels.size());
        verify(hotelRepository, times(1)).searchHotels(name, brand, city, country, amenities);
    }


    @Test
    void searchHotels_WithNoMatchingHotels_ReturnsEmptyList() {

        List<Hotel> expectedHotels = Collections.emptyList();

        String name = "empty";
        String brand = "empty";
        String city = "empty";
        String country = "empty";
        List<String> amenities = Collections.emptyList();

        when(hotelRepository.searchHotels(name, brand, city, country, amenities)).thenReturn(expectedHotels);

        List<Hotel> actualHotels = hotelService.searchHotels(name, brand, city, country, amenities);

        assertEquals(expectedHotels, actualHotels);
        assertEquals(0, actualHotels.size());
        verify(hotelRepository, times(1)).searchHotels(name, brand, city, country, amenities);
    }


    @Test
    public void addAmenities_NonExistingHotel_ThrowsEntityNotFoundException() {

        when(hotelRepository.findById(0L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> hotelService.addAmenities(0L, new HashSet<>()));

        assertEquals("Hotel with id '0' not found.", exception.getMessage());
        verify(hotelRepository, times(1)).findById(0L);
    }


    @Test
    public void addAmenities_AddToExistingAmenitySet_AmenitiesAddedToHotel() {

        Set<String> amenitiesToAdd = Set.of("Spa", "Restaurant");

        Set<String> expectedAmenities = new HashSet<>(Set.of("Spa", "Restaurant"));
        expectedAmenities.addAll(testHotel1.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet()));

        when(hotelRepository.findById(testHotel1.getId())).thenReturn(Optional.of(testHotel1));

        Amenity spaAmenity = new Amenity();
        spaAmenity.setName("Spa");

        Amenity restaurantAmenity = new Amenity();
        restaurantAmenity.setName("Restaurant");

        when(amenityRepository.findAmenityByName("Spa")).thenReturn(Optional.of(spaAmenity));
        when(amenityRepository.findAmenityByName("Restaurant")).thenReturn(Optional.of(restaurantAmenity));
        hotelService.addAmenities(testHotel1.getId(), amenitiesToAdd);

        assertEquals(expectedAmenities, testHotel1.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet()));
        verify(hotelRepository, times(1)).save(testHotel1);
        verify(amenityRepository, times(1)).findAmenityByName("Spa");
        verify(amenityRepository, times(1)).findAmenityByName("Restaurant");
    }


    @Test
    public void addAmenities_ReplaceEmptyAmenitySet_NewAmenitiesSetForHotel() {

        Set<String> amenitiesToAdd = Set.of("Spa", "Restaurant");

        Amenity spaAmenity = new Amenity();
        spaAmenity.setName("Spa");

        Amenity restaurantAmenity = new Amenity();
        restaurantAmenity.setName("Restaurant");

        when(amenityRepository.findAmenityByName("Spa")).thenReturn(Optional.of(spaAmenity));
        when(amenityRepository.findAmenityByName("Restaurant")).thenReturn(Optional.of(restaurantAmenity));

        when(hotelRepository.findById(testHotel2.getId())).thenReturn(Optional.of(testHotel2));

        hotelService.addAmenities(testHotel2.getId(), amenitiesToAdd);

        assertEquals(amenitiesToAdd, testHotel2.getAmenities().stream().map(Amenity::getName).collect(Collectors.toSet()));
        verify(hotelRepository, times(1)).save(testHotel2);
        verify(amenityRepository, times(1)).findAmenityByName("Spa");
        verify(amenityRepository, times(1)).findAmenityByName("Restaurant");
    }


    @Test
    public void getHotelHistogramByParam_NonExistentParam_ReturnsEmptyHistogram() {

        String param = "non-existed";

        Map<String, Long> histogram = hotelService.getHotelHistogramByParam(param);

        assertTrue(histogram.isEmpty());
    }


    @Test
    public void getHotelHistogramByParam_Brand_ReturnsBrandHistogram() {

        String param = "brand";

        List<Object[]> brandHistogram = new ArrayList<>();
        brandHistogram.add(new Object[] { testHotel1.getBrand(), 2L });
        brandHistogram.add(new Object[] { testHotel2.getBrand(), 1L });

        Map<String, Long> expectedHistogramMap = new HashMap<>();
        expectedHistogramMap.put(testHotel1.getBrand(), 2L);
        expectedHistogramMap.put(testHotel2.getBrand(), 1L);


        when(hotelRepository.countHotelsGroupedByBrand()).thenReturn(brandHistogram);

        Map<String, Long> histogram = hotelService.getHotelHistogramByParam(param);

        assertEquals(expectedHistogramMap, histogram);
    }


    @Test
    public void getHotelHistogramByParam_City_ReturnsCityHistogram() {

        String param = "city";

        List<Object[]> cityHistogram = new ArrayList<>();
        cityHistogram.add(new Object[] { testHotel1.getBrand(), 2L });
        cityHistogram.add(new Object[] { testHotel2.getBrand(), 1L });

        Map<String, Long> expectedCityHistogram = new HashMap<>();
        expectedCityHistogram.put(testHotel1.getBrand(), 2L);
        expectedCityHistogram.put(testHotel2.getBrand(), 1L);

        when(hotelRepository.countHotelsGroupedByCity()).thenReturn(cityHistogram);

        Map<String, Long> histogram = hotelService.getHotelHistogramByParam(param);

        assertEquals(expectedCityHistogram, histogram);
    }


    @Test
    public void getHotelHistogramByParam_Country_ReturnsCountryHistogram() {

        String param = "country";

        List<Object[]> countryHistogram = new ArrayList<>();
        countryHistogram.add(new Object[] { testHotel1.getBrand(), 2L });
        countryHistogram.add(new Object[] { testHotel2.getBrand(), 1L });

        Map<String, Long> expectedCountryHistogram = new HashMap<>();
        expectedCountryHistogram.put(testHotel1.getBrand(), 2L);
        expectedCountryHistogram.put(testHotel2.getBrand(), 1L);

        when(hotelRepository.countHotelsGroupedByCountry()).thenReturn(countryHistogram);

        Map<String, Long> histogram = hotelService.getHotelHistogramByParam(param);

        assertEquals(expectedCountryHistogram, histogram);
    }


    @Test
    public void getHotelHistogramByParam_Amenities_ReturnsAmenitiesHistogram() {

        String param = "amenities";

        List<Object[]> amenitiesHistogram = new ArrayList<>();
        amenitiesHistogram.add(new Object[] { testHotel1.getBrand(), 2L });
        amenitiesHistogram.add(new Object[] { testHotel2.getBrand(), 1L });

        Map<String, Long> expectedAmenitiesHistogram = new HashMap<>();
        expectedAmenitiesHistogram.put(testHotel1.getBrand(), 2L);
        expectedAmenitiesHistogram.put(testHotel2.getBrand(), 1L);

        when(hotelRepository.countHotelsGroupedByAmenities()).thenReturn(amenitiesHistogram);

        Map<String, Long> histogram = hotelService.getHotelHistogramByParam(param);

        assertEquals(expectedAmenitiesHistogram, histogram);
    }
}
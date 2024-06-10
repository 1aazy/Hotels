package org.max.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.max.dto.hotel.HotelDTO;
import org.max.dto.hotel.HotelListDTO;
import org.max.models.*;
import org.max.services.HotelService;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelsController.class)
@ExtendWith(MockitoExtension.class)
class HotelsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Hotel testHotel1;
    private Hotel testHotel2;
    private Hotel testHotel3;

    private ModelMapper testMapper;


    @BeforeEach
    void setUp() {

        testMapper = new ModelMapper();

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
    public void getAllHotels_AllHotelsExist_ReturnsAllHotels() throws Exception {

        List<Hotel> hotels = Arrays.asList(testHotel1, testHotel2);

        HotelListDTO hotelListDTO1 = testMapper.map(testHotel1, HotelListDTO.class);
        HotelListDTO hotelListDTO2 = testMapper.map(testHotel2, HotelListDTO.class);
        HotelListDTO hotelListDTO3 = testMapper.map(testHotel3, HotelListDTO.class);

        List<HotelListDTO> hotelListDTOs = Arrays.asList(hotelListDTO1, hotelListDTO2);

        when(hotelService.findAllHotels()).thenReturn(hotels);
        when(modelMapper.map(testHotel1, HotelListDTO.class)).thenReturn(hotelListDTO1);
        when(modelMapper.map(testHotel2, HotelListDTO.class)).thenReturn(hotelListDTO2);
        when(modelMapper.map(testHotel3, HotelListDTO.class)).thenReturn(hotelListDTO3);

        MvcResult result = mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1)).findAllHotels();

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelListDTOs);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    public void getAllHotels_NoHotelsExist_ReturnsEmptyList() throws Exception {

        List<Hotel> hotels = Arrays.asList(testHotel1, testHotel2);

        HotelListDTO hotelListDTO1 = testMapper.map(testHotel1, HotelListDTO.class);
        HotelListDTO hotelListDTO2 = testMapper.map(testHotel2, HotelListDTO.class);
        HotelListDTO hotelListDTO3 = testMapper.map(testHotel3, HotelListDTO.class);

        List<HotelListDTO> hotelListDTOs = Arrays.asList(hotelListDTO1, hotelListDTO2);

        when(hotelService.findAllHotels()).thenReturn(hotels);
        when(modelMapper.map(testHotel1, HotelListDTO.class)).thenReturn(hotelListDTO1);
        when(modelMapper.map(testHotel2, HotelListDTO.class)).thenReturn(hotelListDTO2);
        when(modelMapper.map(testHotel3, HotelListDTO.class)).thenReturn(hotelListDTO3);

        MvcResult result = mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1)).findAllHotels();

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelListDTOs);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelById_HotelExists_ReturnHotel() throws Exception {

        HotelDTO hotelDTO = testMapper.map(testHotel1, HotelDTO.class);

        when(hotelService.findByHotelById(testHotel1.getId())).thenReturn(testHotel1);
        when(modelMapper.map(testHotel1, HotelDTO.class)).thenReturn(hotelDTO);

        MvcResult result = mockMvc.perform(get("/property-view/hotels/{id}", testHotel1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1)).findByHotelById(testHotel1.getId());

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelDTO);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelById_HotelDoesNotExist_ThrowEntityNotFoundException() throws Exception {

        when(hotelService.findByHotelById(testHotel1.getId())).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/property-view/hotels/{id}", testHotel1.getId()))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(hotelService, times(1)).findByHotelById(testHotel1.getId());
    }


    @Test
    void searchHotels_WithMultipleMatchingHotels_ReturnsExpectedHotels() throws Exception {

        List<Hotel> expectedHotels = List.of(testHotel1, testHotel3);

        String name = testHotel1.getName();
        String brand = testHotel1.getBrand();
        String city = testHotel1.getAddress().getCity();
        String country = testHotel1.getAddress().getCountry();
        List<String> amenities = testHotel1.getAmenities().stream().map(Amenity::getName).toList();

        String amenitiesString = testHotel1.getAmenities().stream().map(Amenity::getName).collect(Collectors.joining(", "));

        HotelListDTO hotelListDTO1 = testMapper.map(testHotel1, HotelListDTO.class);
        HotelListDTO hotelListDTO2 = testMapper.map(testHotel3, HotelListDTO.class);

        List<HotelListDTO> hotelListDTOs = Arrays.asList(hotelListDTO1, hotelListDTO2);

        when(hotelService.searchHotels(name, brand, city, country, amenities))
                .thenReturn(expectedHotels);
        when(modelMapper.map(testHotel1, HotelListDTO.class)).thenReturn(hotelListDTO1);
        when(modelMapper.map(testHotel2, HotelListDTO.class)).thenReturn(hotelListDTO2);

        MvcResult result = mockMvc.perform(get("/property-view/search")
                        .param("name", name)
                        .param("brand", brand)
                        .param("city", city)
                        .param("country", country)
                        .param("amenities", amenitiesString))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .searchHotels(name, brand, city, country, amenities);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelListDTOs);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void searchHotels_WithSingleMatchingHotel_ReturnsExpectedHotel() throws Exception {

        List<Hotel> expectedHotels = List.of(testHotel2);

        String name = testHotel2.getName();
        String brand = testHotel2.getBrand();
        String city = testHotel2.getAddress().getCity();
        String country = testHotel2.getAddress().getCountry();
        List<String> amenities = testHotel2.getAmenities().stream().map(Amenity::getName).toList();

        String amenitiesString = testHotel2.getAmenities().stream().map(Amenity::getName).collect(Collectors.joining(", "));

        HotelListDTO hotelListDTO = testMapper.map(testHotel2, HotelListDTO.class);

        List<HotelListDTO> hotelListDTOs = Collections.singletonList(hotelListDTO);

        when(hotelService.searchHotels(name, brand, city, country, amenities))
                .thenReturn(expectedHotels);
        when(modelMapper.map(testHotel2, HotelListDTO.class)).thenReturn(hotelListDTO);

        MvcResult result = mockMvc.perform(get("/property-view/search")
                        .param("name", name)
                        .param("brand", brand)
                        .param("city", city)
                        .param("country", country)
                        .param("amenities", amenitiesString))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .searchHotels(name, brand, city, country, amenities);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelListDTOs);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void searchHotels_WithAllParametersNull_ReturnsAllHotels() throws Exception {

        List<Hotel> expectedHotels = List.of(testHotel1, testHotel2, testHotel3);

        String name = null;
        String brand = null;
        String city = null;
        String country = null;
        List<String> amenities = null;

        String amenitiesString = null;

        HotelListDTO hotelListDTO1 = testMapper.map(testHotel1, HotelListDTO.class);
        HotelListDTO hotelListDTO2 = testMapper.map(testHotel2, HotelListDTO.class);
        HotelListDTO hotelListDTO3 = testMapper.map(testHotel3, HotelListDTO.class);

        List<HotelListDTO> hotelListDTOs = Arrays.asList(hotelListDTO1, hotelListDTO2, hotelListDTO3);

        when(hotelService.searchHotels(name, brand, city, country, amenities))
                .thenReturn(expectedHotels);
        when(modelMapper.map(testHotel1, HotelListDTO.class)).thenReturn(hotelListDTO1);
        when(modelMapper.map(testHotel2, HotelListDTO.class)).thenReturn(hotelListDTO2);
        when(modelMapper.map(testHotel3, HotelListDTO.class)).thenReturn(hotelListDTO3);

        MvcResult result = mockMvc.perform(get("/property-view/search")
                        .param("name", name)
                        .param("brand", brand)
                        .param("city", city)
                        .param("country", country)
                        .param("amenities", amenitiesString))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .searchHotels(name, brand, city, country, amenities);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelListDTOs);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void searchHotels_WithNoMatchingHotels_ReturnsEmptyList() throws Exception {

        List<Hotel> expectedHotels = Collections.emptyList();

        String name = "name";
        String brand = "brand";
        String city = "city";
        String country = "country";
        List<String> amenities = Collections.emptyList();

        String amenitiesString = "";

        when(hotelService.searchHotels(name, brand, city, country, amenities))
                .thenReturn(expectedHotels);

        MvcResult result = mockMvc.perform(get("/property-view/search")
                        .param("name", name)
                        .param("brand", brand)
                        .param("city", city)
                        .param("country", country)
                        .param("amenities", amenitiesString)
                )
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .searchHotels(name, brand, city, country, amenities);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(expectedHotels);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelHistogramByParam_NonExistentParam_ReturnsEmptyMap() throws Exception {

        String parameter = "non-existed";

        when(hotelService.getHotelHistogramByParam(parameter))
                .thenReturn(new HashMap<>());

        MvcResult result = mockMvc.perform(get("/property-view/histogram/" + parameter))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .getHotelHistogramByParam(parameter);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(new HashMap<>());
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelHistogramByParam_BrandParam_ReturnsBrandHistogram() throws Exception {

        String parameter = "brand";

        Map<String, Long> hotelsMap = new HashMap<>();
        hotelsMap.put(testHotel1.getBrand(), 2L);
        hotelsMap.put(testHotel2.getBrand(), 1L);

        when(hotelService.getHotelHistogramByParam(parameter)).thenReturn(hotelsMap);

        MvcResult result = mockMvc.perform(get("/property-view/histogram/" + parameter))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .getHotelHistogramByParam(parameter);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelsMap);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelHistogramByParam_CityParam_ReturnsCityHistogram() throws Exception {

        String parameter = "city";

        Map<String, Long> hotelsMap = new HashMap<>();
        hotelsMap.put(testHotel1.getBrand(), 2L);
        hotelsMap.put(testHotel2.getBrand(), 1L);

        when(hotelService.getHotelHistogramByParam(parameter)).thenReturn(hotelsMap);

        MvcResult result = mockMvc.perform(get("/property-view/histogram/" + parameter))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .getHotelHistogramByParam(parameter);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelsMap);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelHistogramByParam_CountryParam_ReturnsCountryHistogram() throws Exception {

        String parameter = "country";

        Map<String, Long> hotelsMap = new HashMap<>();
        hotelsMap.put(testHotel1.getBrand(), 2L);
        hotelsMap.put(testHotel2.getBrand(), 1L);

        when(hotelService.getHotelHistogramByParam(parameter)).thenReturn(hotelsMap);

        MvcResult result = mockMvc.perform(get("/property-view/histogram/" + parameter))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .getHotelHistogramByParam(parameter);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelsMap);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelHistogramByParam_AmenitiesParam_ReturnsAmenitiesHistogram() throws Exception {

        String parameter = "amenities";

        Map<String, Long> hotelsMap = new HashMap<>();
        hotelsMap.put(testHotel1.getBrand(), 2L);
        hotelsMap.put(testHotel2.getBrand(), 1L);

        when(hotelService.getHotelHistogramByParam(parameter)).thenReturn(hotelsMap);

        MvcResult result = mockMvc.perform(get("/property-view/histogram/" + parameter))
                .andExpect(status().isOk())
                .andReturn();

        verify(hotelService, times(1))
                .getHotelHistogramByParam(parameter);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelsMap);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void getHotelHistogramByParam_WhiteSpaceParam_ThrowsBadRequest() throws Exception {

        String parameter = " ";

        when(hotelService.getHotelHistogramByParam(parameter)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/property-view/histogram/" + parameter))
                .andExpect(status().isBadRequest());
    }


    @Test
    void addHotel_ValidHotel_ReturnsCreated() throws Exception {

        HotelListDTO hotelListDTO = testMapper.map(testHotel1, HotelListDTO.class);

        when(modelMapper.map(testHotel1, HotelListDTO.class)).thenReturn(hotelListDTO);

        String testHotelAsString = objectMapper.writeValueAsString(testHotel1);

        MvcResult result = mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testHotelAsString))
                .andExpect(status().isCreated())
                .andReturn();

        verify(hotelService, times(1)).addHotel(testHotel1);

        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = objectMapper.writeValueAsString(hotelListDTO);
        assertEquals(expectedResult, actualResult);
    }


    @Test
    void addHotel_InvalidHotel_ReturnsBadRequest() throws Exception {

        testHotel1.getContacts().setEmail("not_email");

        String testHotelAsString = objectMapper.writeValueAsString(testHotel1);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testHotelAsString))
                .andExpect(status().isBadRequest());

        verify(hotelService, never()).addHotel(testHotel1);
    }


    @Test
    void addAmenities_NoHotelExist_ReturnsBadRequest() throws Exception {

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", 0))
                .andExpect(status().isBadRequest());
    }


    @Test
    void addAmenities_AddAmenities_ReturnsCreated() throws Exception {

        Set<String> amenitiesToAdd = Set.of("Spa", "Restaurant");
        String requestString = objectMapper.writeValueAsString(amenitiesToAdd);

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", testHotel1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isCreated());

        verify(hotelService, times(1)).addAmenities(testHotel1.getId(), amenitiesToAdd);
    }


    @Test
    void addAmenities_ReplaceAmenities_ReturnsCreated() throws Exception {

        testHotel1.setAmenities(new HashSet<>());

        Set<String> amenitiesToAdd = Set.of("Spa", "Restaurant");
        String requestString = objectMapper.writeValueAsString(amenitiesToAdd);

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", testHotel1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isCreated());

        verify(hotelService, times(1)).addAmenities(testHotel1.getId(), amenitiesToAdd);
    }


    @Test
    void addAmenities_AmenitiesContainsEmptyString_ReturnsBadRequest() throws Exception {

        testHotel1.setAmenities(null);

        Set<String> amenitiesToAdd = Set.of("");
        String requestString = objectMapper.writeValueAsString(amenitiesToAdd);

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", testHotel1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isBadRequest());
    }
}
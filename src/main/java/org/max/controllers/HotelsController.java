package org.max.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.max.dto.hotel.HotelDTO;
import org.max.dto.hotel.HotelListDTO;
import org.max.exceptions.response.CommonExceptionResponse;
import org.max.models.Hotel;
import org.max.services.HotelService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Hotels", description = "Operations related to hotels")
@AllArgsConstructor
@RestController
@RequestMapping("property-view")
public class HotelsController {

    private final ModelMapper modelMapper;
    private final HotelService hotelServiceImpl;


    @Operation(summary = "Get all hotels", description = "Retrieve a list of all hotels")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of hotels list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelListDTO.class),
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "name": "DoubleTree by Hilton Minsk",
                                        "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
                                        "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                                        "phone": "+375 17 309-80-00"
                                    }
                                ]
                            """))
            )
    })
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelListDTO>> getAllHotels() {
        List<HotelListDTO> hotelsListDto = hotelServiceImpl.findAllHotels()
                                                    .stream()
                                                    .map(hotel -> modelMapper.map(hotel, HotelListDTO.class)).toList();
        return ResponseEntity.ok(hotelsListDto);
    }


    @Operation(summary = "Get hotel by ID", description = "Retrieve a hotel by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of hotel",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Hotel.class),
                            examples = @ExampleObject(value = """
                                {
                                             "id": 1,
                                             "name": "DoubleTree by Hilton Minsk",
                                             "brand": "Hilton",
                                             "address": {
                                                 "houseNumber": 9,
                                                 "street": "Pobediteley Avenue",
                                                 "city": "Minsk",
                                                 "country": "Belarus",
                                                 "postCode": "220004"
                                             },
                                             "contacts": {
                                                 "phone": "+375 17 309-80-00",
                                                 "email": "doubletreeminsk.info@hilton.com"
                                             },
                                             "arrivalTime": {
                                                 "checkIn": "14:00",
                                                 "checkOut": "12:00"
                                             },
                                             "amenities": [
                                                 "Business center",
                                                 "Fitness center",
                                                 "Concierge",
                                                 "Pet-friendly rooms",
                                                 "Room service",
                                                 "Free parking",
                                                 "Meeting rooms",
                                                 "On-site restaurant",
                                                 "Free WiFi",
                                                 "Non-smoking rooms"
                                             ]
                                         }
                            """))),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                {
                                    "message": "Hotel with id '10' not found."
                                }
                            """)))
    })
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@Parameter(description = "ID of the hotel to be retrieved", required = true)
                                                     @PathVariable("id") Long id) {

        Hotel hotel = hotelServiceImpl.findByHotelById(id);
        HotelDTO hotelDTO = modelMapper.map(hotel, HotelDTO.class);

        return ResponseEntity.ok(hotelDTO);
    }


    @Operation(summary = "Search hotels", description = "Search for hotels by various criteria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelListDTO.class),
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "name": "DoubleTree by Hilton Minsk",
                                        "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
                                        "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                                        "phone": "+375 17 309-80-00"
                                    }
                                ]
                            """)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<HotelListDTO>> searchHotels(
            @Parameter(description = "Name of the hotel") @RequestParam(required = false) String name,
            @Parameter(description = "Brand of the hotel") @RequestParam(required = false) String brand,
            @Parameter(description = "City where the hotel is located") @RequestParam(required = false) String city,
            @Parameter(description = "Country where the hotel is located") @RequestParam(required = false) String country,
            @Parameter(description = "List of amenities offered by the hotel") @RequestParam(required = false) List<String> amenities) {

        List<HotelListDTO> hotelListDTO = hotelServiceImpl.searchHotels(name, brand, city, country, amenities)
                                                .stream()
                                                .map(hotel -> modelMapper.map(hotel, HotelListDTO.class)).toList();

        return ResponseEntity.ok(hotelListDTO);
    }


    @Operation(summary = "Get hotels histogram", description = "Retrieve a histogram of hotels based on a parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of histogram",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "Minsk": 1,
                                    "Moscow": 2,
                                    "Mogilev": 0
                                }
                            """))),
            @ApiResponse(responseCode = "404", description = "Hotel not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "message": "Parameter cannot be empty"
                                }
                            """)))
    })
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHotelsHistogram(
            @Parameter(description = "Parameter to generate histogram by",
                    examples = {
                            @ExampleObject(name = "brand", value = "brand", description = "Return histogram of hotels by brand"),
                            @ExampleObject(name = "city", value = "city", description = "Return histogram of hotels by city"),
                            @ExampleObject(name = "county", value = "county", description = "Return histogram of hotels by county"),
                            @ExampleObject(name = "amenities", value = "amenities", description = "Return histogram of hotels by amenities")
                    })
            @PathVariable("param") String parameter) {

        if (parameter.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter cannot be empty");
        }

        Map<String, Long> histogramMap = hotelServiceImpl.getHotelHistogramByParam(parameter);

        return new ResponseEntity<>(histogramMap, HttpStatus.OK);
    }


    @Operation(summary = "Add a new hotel", description = "Add a new hotel to the database",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Hotel object to be added",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Hotel.class),
                            examples = @ExampleObject(value = """
                                {
                                    "name": "DoubleTree by Hilton Minsk",
                                    "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
                                    "brand": "Hilton",
                                    "address": {
                                        "houseNumber": 9,
                                        "street": "Pobediteley Avenue",
                                        "city": "Minsk",
                                        "county": "Belarus",
                                        "postCode": "220004"
                                    },
                                    "contacts": {
                                        "phone": "+375 17 309-80-00",
                                        "email": "doubletreeminsk.info@hilton.com"
                                    },
                                    "arrivalTime": {
                                        "checkIn": "14:00",
                                        "checkOut": "12:00"
                                    }
                                }
                            """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelListDTO.class),
                            examples = @ExampleObject(value = """
                        {
                            "id": 1,
                            "name": "DoubleTree by Hilton Minsk",
                            "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
                            "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                            "phone": "+375 17 309-80-00"
                        }
                        """))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonExceptionResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "message": "Invalid time format. Please provide the time in the format HH:mm."
                            }
                            """)))
    })

    @PostMapping("/hotels")
    public ResponseEntity<HotelListDTO> addHotel(
            @Parameter(description = "Hotel object to be added", required = true,
                    content = @Content(schema = @Schema(implementation = Hotel.class)))
            @RequestBody @Valid Hotel hotel,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new ValidationException(errorMessages);
        }

        hotelServiceImpl.addHotel(hotel);
        HotelListDTO hotelListDTO = modelMapper.map(hotel, HotelListDTO.class);

        return new ResponseEntity<>(hotelListDTO, HttpStatus.CREATED);
    }


    @Operation(summary = "Add amenities to a hotel", description = "Add amenities to a specific hotel",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Name of amenities to be added to the hotel",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Set.class),
                            examples = @ExampleObject(value = """
                                [
                                             "Free parking",
                                             "Free WiFi",
                                             "Non-smoking rooms",
                                             "Concierge",
                                             "On-site restaurant",
                                             "Fitness center",
                                             "Pet-friendly rooms",
                                             "Room service",
                                             "Business center",
                                             "Meeting rooms"
                                         ]
                            """))))

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Amenities successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonExceptionResponse.class),
                            examples = @ExampleObject(value = """
                                {
                                    "message": "Amenity names cannot be null or blank."
                                }
                            """)))
    })
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenitiesToHotel(
            @Parameter(description = "ID of the hotel to add amenities to", required = true) @PathVariable("id") Long id,
            @Parameter(description = "Set of amenities to be added", required = true, content = @Content(schema = @Schema(implementation = Set.class)))
            @RequestBody Set<String> amenities) {

        if (amenities.stream().anyMatch(a -> a == null || a.trim().isBlank())) {
            throw new ValidationException("Amenity names cannot be null or blank.");
        }

        hotelServiceImpl.addAmenities(id, amenities);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
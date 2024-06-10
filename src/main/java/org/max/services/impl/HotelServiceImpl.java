package org.max.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.max.models.Amenity;
import org.max.models.Hotel;
import org.max.repositories.AmenityRepository;
import org.max.repositories.hotel.HotelRepository;
import org.max.services.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;

    @Transactional
    @Override
    public void addHotel(Hotel hotel) {
        hotelRepository.save(hotel);
    }


    @Override
    public Hotel findByHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel with id '" + id + "' not found."));
    }


    @Override
    public List<Hotel> findAllHotels() {
        return hotelRepository.findAll();
    }


    @Override
    public List<Hotel> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        return hotelRepository.searchHotels(name, brand, city, country, amenities);
    }


    @Transactional
    @Override
    public void addAmenities(Long id, Set<String> amenities) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel with id '" + id + "' not found."));

        Set<Amenity> amenityList = amenities.stream()
                .map(name -> amenityRepository.findAmenityByName(name)
                        .orElseGet(() -> {
                            Amenity newAmenity = new Amenity();
                            newAmenity.setName(name);
                            return amenityRepository.save(newAmenity);
                        }))
                .collect(Collectors.toSet());

        if (!hotel.getAmenities().isEmpty()) {
            hotel.getAmenities().addAll(amenityList);
        } else {
            hotel.setAmenities(amenityList);
        }
        hotelRepository.save(hotel);
    }


    @Override
    public Map<String, Long> getHotelHistogramByParam(String param) {
        Map<String, Long> histogram;

        switch (param.toLowerCase()) {
            case "brand":
                histogram = convertToMap(hotelRepository.countHotelsGroupedByBrand());
                break;
            case "city":
                histogram = convertToMap(hotelRepository.countHotelsGroupedByCity());
                break;
            case "country":
                histogram = convertToMap(hotelRepository.countHotelsGroupedByCountry());
                break;
            case "amenities":
                histogram = convertToMap(hotelRepository.countHotelsGroupedByAmenities());
                break;
            default:
                return new HashMap<>();
        }

        return histogram;
    }

    private Map<String, Long> convertToMap(List<Object[]> results) {
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }
}

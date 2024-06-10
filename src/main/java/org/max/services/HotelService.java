package org.max.services;

import org.max.models.Hotel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface HotelService {

    void addHotel(Hotel hotel);

    Hotel findByHotelById(Long id);

    List<Hotel> findAllHotels();
    List<Hotel> searchHotels(String name, String brand, String city, String country, List<String> amenities);

    void addAmenities(Long id, Set<String> amenities);

    Map<String, Long> getHotelHistogramByParam(String param);
}

package org.max.repositories.hotel;

import org.max.models.Hotel;

import java.util.List;

public interface HotelSearchRepository {
    List<Hotel> searchHotels(String name, String brand, String city, String country, List<String> amenities);
}
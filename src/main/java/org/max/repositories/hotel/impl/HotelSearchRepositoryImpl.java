package org.max.repositories.hotel.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.max.models.Amenity;
import org.max.models.Hotel;
import org.max.repositories.hotel.HotelSearchRepository;

import java.util.ArrayList;
import java.util.List;

public class HotelSearchRepositoryImpl implements HotelSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Hotel> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Hotel> query = cb.createQuery(Hotel.class);
        Root<Hotel> hotel = query.from(Hotel.class);
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(hotel.get("name"), "%" + name + "%"));
        }

        if (brand != null && !brand.isEmpty()) {
            predicates.add(cb.like(hotel.get("brand"), "%" + brand + "%"));
        }

        if (city != null && !city.isEmpty()) {
            predicates.add(cb.like(hotel.get("address").get("city"), "%" + city + "%"));
        }

        if (country != null && !country.isEmpty()) {
            predicates.add(cb.like(hotel.get("address").get("country"), "%" + country + "%"));
        }

        if (amenities != null && !amenities.isEmpty()) {
            Join<Hotel, Amenity> amenityJoin = hotel.join("amenities", JoinType.LEFT);
            predicates.add(amenityJoin.get("name").in(amenities));
            query.distinct(true);
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}

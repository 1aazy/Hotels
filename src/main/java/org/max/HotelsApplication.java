package org.max;

import org.max.dto.AddressDTO;
import org.max.dto.ArrivalTimeDTO;
import org.max.dto.ContactsDTO;
import org.max.dto.hotel.HotelDTO;
import org.max.dto.hotel.HotelListDTO;
import org.max.models.Address;
import org.max.models.Amenity;
import org.max.models.Hotel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class HotelsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HotelsApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Hotel, HotelListDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setDescription(source.getDescription());
                using(ctx -> {
                    Address address = ((Hotel) ctx.getSource()).getAddress();
                    return address.getHouseNumber() + " " + address.getStreet() + ", " + address.getCity() + ", " + address.getPostCode() + ", " + address.getCountry();
                }).map(source, destination.getAddress());
                map().setPhone(source.getContacts().getPhone());
            }
        });

        modelMapper.addMappings(new PropertyMap<Hotel, HotelDTO>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setName(source.getName());
                map().setBrand(source.getBrand());
                using(ctx -> modelMapper.map(((Hotel) ctx.getSource()).getAddress(), AddressDTO.class))
                        .map(source, destination.getAddress());
                using(ctx -> modelMapper.map(((Hotel) ctx.getSource()).getContacts(), ContactsDTO.class))
                        .map(source, destination.getContacts());
                using(ctx -> modelMapper.map(((Hotel) ctx.getSource()).getArrivalTime(), ArrivalTimeDTO.class))
                        .map(source, destination.getArrivalTime());
                using(ctx -> {
                    Set<Amenity> amenities = ((Hotel) ctx.getSource()).getAmenities();
                    return amenities == null ? null : amenities.stream().map(Amenity::getName).collect(Collectors.toSet());
                }).map(source, destination.getAmenities());
            }
        });

        return modelMapper;
    }
}

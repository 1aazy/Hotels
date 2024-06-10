package org.max.dto.hotel;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelListDTO {

    Long id;

    String name;

    String description;

    String address;

    String phone;
}

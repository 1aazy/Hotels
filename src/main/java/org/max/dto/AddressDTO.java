package org.max.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private int houseNumber;

    private String street;

    private String city;

    private String country;

    private String postCode;
}

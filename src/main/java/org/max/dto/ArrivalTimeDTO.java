package org.max.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTimeDTO {

    private String checkIn;

    private String checkOut;
}
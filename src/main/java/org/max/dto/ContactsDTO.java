package org.max.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactsDTO {

    private String phone;

    private String email;
}

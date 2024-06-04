package org.max.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arrival_time")
public class ArrivalTime {
// TODO add validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // TODO rewrite to Date class
    @Column(name = "check_in")
    private String checkIn;

    // TODO rewrite to Date class
    @Column(name = "check_out")
    private String checkOut;
}

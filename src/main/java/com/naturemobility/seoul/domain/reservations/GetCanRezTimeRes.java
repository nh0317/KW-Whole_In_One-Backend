package com.naturemobility.seoul.domain.reservations;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetCanRezTimeRes {
    private String Time;
}

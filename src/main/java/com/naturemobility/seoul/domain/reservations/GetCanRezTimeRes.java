package com.naturemobility.seoul.domain.reservations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class GetCanRezTimeRes {
    private String Time;
}

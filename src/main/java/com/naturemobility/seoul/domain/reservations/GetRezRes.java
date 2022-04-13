package com.naturemobility.seoul.domain.reservations;

import com.naturemobility.seoul.domain.payment.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@NoArgsConstructor
public class GetRezRes {
    private Long reservationIdx;
    private String storeName;
    private String reservationTIme; //yyyy.mm.dd 오전 hh:mm
    private String paymentTime; // yyyy.mm.dd
    private Integer useTime;
    private Integer selectedHall;
    private Integer personCount;
    private Boolean alreadyUsed;
    private Integer reservePrice;
    private Integer discountPrice;
    private Integer payPrice;
    private String createdAt;
    private String payMethod;
    private String refundStatus;

    public GetRezRes(ReservationInfo reservation) {
        this.reservationIdx = reservation.getReservationIdx();
        this.storeName = reservation.getStoreName();
        this.useTime = reservation.getUseTime();
        this.selectedHall = reservation.getSelectedHall();
        this.personCount = reservation.getPersonCount();
        this.alreadyUsed = reservation.getAlreadyUsed();
        this.reservePrice = reservation.getReservePrice();
        this.discountPrice = reservation.getDiscountPrice();
        this.payPrice = reservation.getPayPrice();
        this.payMethod = reservation.getPayMethod();
        this.refundStatus = RefundStatus.getMsg(reservation.getRefundStatus());
        try{
            DateTimeFormatter format;
            format = DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm").withLocale(Locale.KOREA);
            this.reservationTIme = reservation.getReservationTime().format(format);
            format = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            if(reservation.getPaymentTime()!=null)
                this.paymentTime = reservation.getPaymentTime().format(format);
            else this.paymentTime = "미 결제";
            this.createdAt = reservation.getCreatedAt().format(format);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}

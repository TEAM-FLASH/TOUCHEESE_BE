//package com.team4.toucheese.studio.dto;
//
//import com.team4.toucheese.studio.entity.Menu;
//import com.team4.toucheese.studio.entity.Reservation;
//import com.team4.toucheese.studio.entity.Studio;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class CompleteReservationDto {
//    private Long id;
//    private Long studioId;
//    private String studioName;
//    private Long user_id;
//    private LocalDate date;
//    private LocalTime start_time;
//    private LocalTime end_time;
//    private String note;
//    private String status;
//    private LocalDateTime created_at;
//    private LocalDateTime updated_at;
//    private Long menuId;
//    private String menuName;
//    private List<Long> additionalOptionIds;
//    private String visitingCustomerName;
//    private String visitingCustomerPhone;
//    private Long totalPrice;
//    private String paymentMethod;
//    private String impUid;
//    private String merchantUid;
//
//    private boolean existReview;
//
//    public static CompleteReservationDto fromReservationEntity(Reservation reservation) {
//        return CompleteReservationDto.builder()
//                .studioId(reservation.getId())
//                .studioName(reservation.getStudio().getName())
//                .user_id(reservation.getUser_id())
//                .date(reservation.getDate())
//                .start_time(reservation.getStart_time())
//                .end_time(reservation.getEnd_time())
//                .note(reservation.getNote())
//                .status("COMPLETE")
//                .menuId(reservation.getMenu().getId())
//                .menuName(reservation.getMenu().getName())
//                .additionalOptionIds(reservation.getAdditionalOptionIds())
//                .visitingCustomerName(reservation.getVisitingCustomerName())
//                .visitingCustomerPhone(reservation.getVisitingCustomerPhone())
//                .totalPrice(reservation.getTotalPrice())
//                .paymentMethod(reservation.getPaymentMethod())
//                .impUid(reservation.getImpUid())
//                .merchantUid(reservation.getMerchantUid())
//                .build();
//    }
//}

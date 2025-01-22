package com.team4.toucheese.studio.service;

import com.team4.toucheese.studio.dto.AvailableTimeDto;
import com.team4.toucheese.studio.dto.AvailableTimeResultDto;
import com.team4.toucheese.studio.dto.AvailableTimeWithDateDto;
import com.team4.toucheese.studio.dto.ReservationRequest;
import com.team4.toucheese.studio.entity.*;
import com.team4.toucheese.studio.repository.*;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudioHolidayRepository studioHolidayRepository;
    private final StudioSpecialHolidayRepository studioSpecialHolidayRepository;
    private final StudioOpeningHoursRepository studioOpeningHoursRepository;
    private final UserRepository userRepository;
    private final StudioRepository studioRepository;
    private final MenuRepository menuRepository;
    private final AdditionalOptionRepository additionalOptionRepository;

    public AvailableTimeResultDto getAvailableTime(LocalDate date, Long studioId, Integer duration){

        int year = date.getYear();
        int month = date.getMonthValue();


        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate startOfCalendar = startOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));    //달력 첫 날 일요일
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        LocalDate endOfCalendar = endOfMonth.with(DayOfWeek.SATURDAY);      //달력 마지막 토요일

        //1. 예약된 일정 가져오기
        List<Reservation> reservations  = reservationRepository.findReservationsForStudio(studioId, year, month);

        //2. 스튜디오의 운영시간, 휴무일, 특별 휴무일 가져오기
        //운영시간
        List<StudioOpeningHours> openingHours = studioOpeningHoursRepository.findByStudioId((studioId));
        //휴무일
        List<StudioHoliday> holidays = studioHolidayRepository.findByStudioId(studioId);
        //특수 휴무일
        List<StudioSpecialHoliday> specialHolidays = studioSpecialHolidayRepository.findSpecialHolidaysForStudio(studioId, year, month);

        //3. 결과를 저장할 리스트
        List<AvailableTimeWithDateDto> availableDates = new ArrayList<>();
        List<String> disableDates = new ArrayList<>();

        // 4. 확대된 날짜 범위 반복
        for (LocalDate currentDate = startOfCalendar; currentDate.isBefore(endOfCalendar.plusDays(1)); currentDate = currentDate.plusDays(1)) {
            AvailableTimeWithDateDto dailySlot = new AvailableTimeWithDateDto();

            // 4-1. 휴무일인지 확인
            if (isHoliday(currentDate, holidays, specialHolidays)) {
                disableDates.add(currentDate.getMonthValue() + "-" + currentDate.getDayOfMonth());
                continue;
            }

            // 4-2. 해당 날짜의 운영 시간 가져오기
            LocalDate finalCurrentDate = currentDate;
            StudioOpeningHours dayHours = openingHours.stream()
                    .filter(h -> h.getDayOfWeek() == finalCurrentDate.getDayOfWeek())
                    .findFirst()
                    .orElse(null);

            if (dayHours == null || dayHours.isClosed()) {
                disableDates.add(currentDate.getMonthValue() + "-" + currentDate.getDayOfMonth());
                continue;
            }

            // 4-3. 하루의 예약 가능한 시간 계산
            List<AvailableTimeDto> timeSlots = calculateAvailableTimes(
                    currentDate, reservations,
                    dayHours.getOpenTime().toLocalTime(),
                    dayHours.getCloseTime().toLocalTime(),
                    duration
            );

            // 예약 가능한 시간이 없다면 'disableDates'에 추가
            if (timeSlots.isEmpty()) {
                disableDates.add(currentDate.getMonthValue() + "-" + currentDate.getDayOfMonth());
                continue;
            }

            // 4-4. 날짜와 시간 상태 저장
            dailySlot.setDate(currentDate.toString()); // 예: "2025-01-01"
            dailySlot.setAvailableTimeDto(timeSlots); // 시간 및 가능 여부 저장
            availableDates.add(dailySlot);
        }

        // 5. 결과 반환
        AvailableTimeResultDto resultDto = new AvailableTimeResultDto();
        resultDto.setAvailableTimeWithDates(availableDates);
        resultDto.setDisableDates(disableDates); // 비활성 날짜
        return resultDto;
    }

    private boolean isHoliday(LocalDate date, List<StudioHoliday> holidays, List<StudioSpecialHoliday> specialHolidays){
        //주간 휴무 확인
        for (StudioHoliday holiday : holidays){
            if (holiday.getDayOfWeek() == date.getDayOfWeek() &&
                holiday.getWeekOfMonth() == calculateWeekOfMonth(date)){
                return true;
                }
        }

        //특별 휴무일 확인
        for (StudioSpecialHoliday specialHoliday : specialHolidays){
            if (specialHoliday.getDate().equals(date)){
                return true;
            }
        }

        return false;
    }

    private List<AvailableTimeDto> calculateAvailableTimes(LocalDate date, List<Reservation> reservations,
                                                     LocalTime openTime, LocalTime closeTime, int duration) {

        List<AvailableTimeDto> availableTimelist = new ArrayList<>();
        LocalTime currentTime = openTime;

        while (currentTime.plusMinutes(duration).isBefore(closeTime) || currentTime.plusMinutes(duration).equals(closeTime)) {
            LocalTime endTime = currentTime.plusMinutes(duration);
            AvailableTimeDto availableTimes = new AvailableTimeDto();

            //예약 중복 여부 확인
            LocalTime finalCurrentTime = currentTime;
            boolean isAvailable = reservations.stream().noneMatch(reservation ->
                    reservation.getDate().equals(date) &&
                    !(finalCurrentTime.isAfter(reservation.getEnd_time()) ||
                            endTime.isBefore(reservation.getStart_time()) ||
                            finalCurrentTime.equals(reservation.getEnd_time()) ||
                            endTime.equals(reservation.getStart_time()))
            );

            //결과 저장
            availableTimes.setTime(currentTime.toString());
            availableTimes.setAvailable(isAvailable);
            availableTimelist.add(availableTimes);
            currentTime = currentTime.plusMinutes(30);
        }

        return availableTimelist;
    }

    public int calculateWeekOfMonth(LocalDate date) {
        // 월의 첫 번째 주의 일요일 기준으로 시작
        LocalDate firstSundayOfMonth = date.withDayOfMonth(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        // 입력 날짜까지 몇 주가 지났는지 계산
        int weekOfMonth = (int) ChronoUnit.WEEKS.between(firstSundayOfMonth, date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))) + 1;
        return weekOfMonth;
    }


    public void makeReservation(ReservationRequest reservationRequest, String userEmail){
        Reservation reservation = new Reservation();
        Optional<UserEntity> user = userRepository.findByEmail(userEmail);
        Optional<Studio> studio = studioRepository.findById(reservationRequest.getStudioId());
        Optional<Menu> menu = menuRepository.findById(reservationRequest.getMenuId());
        List<AdditionalOption> additionalOptions = additionalOptionRepository.findAllById(reservationRequest.getAdditionalOptionIds());
        if (user.isEmpty() || studio.isEmpty()) {
            return;
        }else {
            Long userId = user.get().getId();
            reservation.toBuilder()
                    .user_id(userId)
                    .studio(studio.get())
                    .additionalOptions(additionalOptions)
                    .menu(menu.get())
                    .date(reservationRequest.getDate())
                    .start_time(reservationRequest.getStartTime())
                    .note(reservationRequest.getNote())
                    .build();
        }


    }


}

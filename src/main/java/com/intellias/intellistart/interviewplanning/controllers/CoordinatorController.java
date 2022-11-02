package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.controllers.dto.BookingDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from users with Coordinator role.
 */
@RestController
public class CoordinatorController {

    @PostMapping("bookings/{id}")
    BookingDto createBooking(@RequestBody BookingDto request, @PathVariable Long id){
        return request;
    }

    @PostMapping("bookings")
    BookingDto updateBooking(@RequestBody BookingDto request){
        return request;
    }

}

package com.intellias.intellistart.interviewplanning.controllers;

import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for processing requests from unauthorized users.
 */
@RestController
@RequestMapping("weeks")
public class GuestController {

  private final WeekService weekService;

  @Autowired
  public GuestController(WeekService weekService) {
    this.weekService = weekService;
  }

  @GetMapping("current")
  public Week currentWeek(){
    return weekService.getCurrentWeek();
  }

  @GetMapping("next")
  public Week nextWeek(){
    return weekService.getNextWeek();
  }
}

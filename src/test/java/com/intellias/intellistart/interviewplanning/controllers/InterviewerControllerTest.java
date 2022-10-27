package com.intellias.intellistart.interviewplanning.controllers;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellias.intellistart.interviewplanning.controllers.dtos.InterviewerSlotDto;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDtoValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.period.PeriodService;
import com.intellias.intellistart.interviewplanning.model.period.services.OverlapService;
import com.intellias.intellistart.interviewplanning.model.period.services.TimeConverter;
import com.intellias.intellistart.interviewplanning.model.period.services.validation.PeriodValidator;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.user.UserService;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekRepository;
import com.intellias.intellistart.interviewplanning.model.week.WeekService;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.not;

@AutoConfigureMockMvc
@SpringBootTest
public class InterviewerControllerTest {
  static UserRepository userRepository = Mockito.mock(UserRepository.class);
 // @MockBean
  static UserService userService = new UserService(userRepository);
  static PeriodRepository periodRepository = Mockito.mock(PeriodRepository.class);
  static TimeConverter timeConverter = new TimeConverter();
  static PeriodValidator periodValidator = new PeriodValidator();
  static OverlapService overlapService = new OverlapService();
  @MockBean
  @Autowired
  static PeriodService periodService;/* = new PeriodService(periodRepository, timeConverter,
      periodValidator, overlapService);*/

  static WeekRepository weekRepository = Mockito.mock(WeekRepository.class);
  @MockBean
  @Autowired
  WeekService weekService;

  static InterviewerSlotRepository interviewerSlotRepository =
      Mockito.mock(InterviewerSlotRepository.class);
  @MockBean
  static InterviewerSlotDtoValidator interviewerSlotDtoValidator;/* = new InterviewerSlotDtoValidator(
      periodService, userService, weekService, interviewerSlotRepository
  );*/

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

//@MockBean
//@Autowired
  static InterviewerSlotService interviewerSlotService;


  @AllArgsConstructor
  @Getter
  @Setter
  static class JsonObj {

    Long week;
    String dayOfWeek;
    String from;
    String to;
  }

  @BeforeAll
  static void initialize() {
    interviewerSlotRepository = Mockito.mock(InterviewerSlotRepository.class);
    interviewerSlotService = new InterviewerSlotService(
        interviewerSlotRepository
    );
  }

//  @Test
//  void shouldGetDTO() throws Exception {
//    InterviewerSlotDto dto = new InterviewerSlotDto(1L, 200L,
//        "TUE", "10:00", "20:00");
//    Period p1 = new Period(1L, LocalTime.of(10, 0), LocalTime.of(20, 0),
//        new HashSet<>(), new HashSet<>(), new HashSet<>());
//    Week w1 = new Week(200L, new HashSet<>());
//    User u1 = new User(1L, "interviewer@gmail.com", Role.INTERVIEWER);
//    InterviewerSlot toReturn = new InterviewerSlot(1L, w1, DayOfWeek.TUE, p1, null, u1);
//    JsonObj jsonObj = new JsonObj(200L, "TUE", "10:00", "20:00");
//    when(userService.getUserById(1L)).thenReturn(Optional.of(u1));
//    when(periodService.getPeriod(dto.getFrom(), dto.getTo())).thenReturn(p1);
//    when(weekService.getWeekByWeekNum(anyLong())).thenReturn(w1);
//    when(weekService.getCurrentWeek()).thenReturn(new Week(20L, new HashSet<>()));
//    when(interviewerSlotDtoValidator.interviewerSlotValidateDto(any(InterviewerSlotDto.class)))
//        .thenReturn(toReturn);
//    when(interviewerSlotService.saveInRepo(any(InterviewerSlot.class))).thenReturn(null);
//
//
//    mockMvc.perform(MockMvcRequestBuilders.post("/interviewers/{interviewerId}/slots", 1L)
//            .content(objectMapper.writeValueAsString(jsonObj))
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.interviewerId", Matchers.is(1)))
//        .andExpect(jsonPath("$.week", Matchers.is(200)))
//        .andExpect(jsonPath("$.dayOfWeek", Matchers.is("TUE")));
//  }

  @Test
  void postRequestwithNullDayAngGetBadRequest() throws Exception {
    JsonObj jsonObj = new JsonObj(50L, null, "10:00", "20:00");
    mockMvc.perform(MockMvcRequestBuilders.post("/interviewers/{interviewerId}/slots", 1L)
            .content(objectMapper.writeValueAsString(jsonObj))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void postRequestwithNullFromAngGetBadRequest() throws Exception {
    JsonObj jsonObj = new JsonObj(50L, "THU", null, "20:00");
    mockMvc.perform(MockMvcRequestBuilders.post("/interviewers/{interviewerId}/slots", 1L)
            .content(objectMapper.writeValueAsString(jsonObj))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void postRequestwithNullToAngGetBadRequest() throws Exception {
    JsonObj jsonObj = new JsonObj(50L, "SUN", "10:00", null);
    mockMvc.perform(MockMvcRequestBuilders.post("/interviewers/{interviewerId}/slots", 1L)
            .content(objectMapper.writeValueAsString(jsonObj))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

//  @Test
//  void shouldGetDTOandChangeSlot() throws Exception {
//    InterviewerSlotDto dto = new InterviewerSlotDto(1L, 201L,
//        "TUE", "10:00", "20:00");
//    Period p1 = new Period(1L, LocalTime.of(10, 0), LocalTime.of(20, 0),
//        new HashSet<>(), new HashSet<>(), new HashSet<>());
//    Week w1 = new Week(201L, new HashSet<>());
//    User u1 = new User(1L, "interviewer@gmail.com", Role.INTERVIEWER);
//    InterviewerSlot interviewerSlot1 = (new InterviewerSlot(1L, w1, DayOfWeek.TUE, p1, null, u1));
//    InterviewerSlot toReturn = new InterviewerSlot(1L, w1, DayOfWeek.TUE, p1, null, u1);
//
//    JsonObj jsonObj = new JsonObj(201L, "TUE", "10:00", "20:00");
//    when(interviewerSlotDtoValidator.interviewerSlotValidateDto(dto)).thenReturn(toReturn);
//    mockMvc.perform(
//            MockMvcRequestBuilders.post("/interviewers/{interviewerId}/slots/{slotId}", 1L, 1L)
//                .content(objectMapper.writeValueAsString(jsonObj))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.interviewerId", Matchers.is(1)));
//
//    InterviewerSlot interviewerSlot2 = interviewerSlotService.getSlotById(1L).get();
//
//    assertThat(interviewerSlot1.getWeek().getId(),
//        not(equalTo(interviewerSlot2.getWeek().getId())));
//    assertThat(interviewerSlot1.getDayOfWeek(), not(equalTo(interviewerSlot2.getDayOfWeek())));
//    assertEquals(interviewerSlot1.getId(), interviewerSlot2.getId());
//
//  }
}



package com.intellias.intellistart.interviewplanning.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellias.intellistart.interviewplanning.controllers.dto.InterviewerSlotDTO;
import com.intellias.intellistart.interviewplanning.exceptions.CannotEditThisWeekException;
import com.intellias.intellistart.interviewplanning.exceptions.InvalidDayOfWeekException;
import com.intellias.intellistart.interviewplanning.model.dayofweek.DayOfWeek;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlot;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotDTOValidator;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotRepository;
import com.intellias.intellistart.interviewplanning.model.interviewerslot.InterviewerSlotService;
import com.intellias.intellistart.interviewplanning.model.period.Period;
import com.intellias.intellistart.interviewplanning.model.period.PeriodRepository;
import com.intellias.intellistart.interviewplanning.model.user.Role;
import com.intellias.intellistart.interviewplanning.model.user.User;
import com.intellias.intellistart.interviewplanning.model.user.UserRepository;
import com.intellias.intellistart.interviewplanning.model.week.Week;
import com.intellias.intellistart.interviewplanning.model.week.WeekRepository;
import java.time.LocalTime;
import java.util.HashSet;
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
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.not;

@AutoConfigureMockMvc
@SpringBootTest
public class InterviewerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  //@MockBean
  static InterviewerSlotService interviewerSlotService;
  @MockBean
  static InterviewerSlotDTOValidator interviewerSlotDTOValidator;

  static InterviewerSlotRepository interviewerSlotRepository;
  static PeriodRepository periodRepository;

  @AllArgsConstructor
  @Getter
  @Setter
  static class JsonObj{
    Long week;
    String dayOfWeek;
    String from;
    String to;
  }

  @BeforeAll
  static void initialize(){
    interviewerSlotRepository = Mockito.mock(InterviewerSlotRepository.class);
    periodRepository =  Mockito.mock(PeriodRepository.class);
    interviewerSlotService = new InterviewerSlotService(
        interviewerSlotDTOValidator, interviewerSlotRepository, periodRepository
    );
  }
  @Test
  void shouldGetDTO() throws Exception {
    JsonObj jsonObj = new JsonObj(200L, "THU", "10:00", "20:00");
    mockMvc.perform(MockMvcRequestBuilders.post("/interviewers/{interviewerId}/slots", 1L)
          .content(objectMapper.writeValueAsString(jsonObj))
          .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.interviewerId", Matchers.is(1)))
        .andExpect(jsonPath("$.week",Matchers.is(200)))
        .andExpect(jsonPath("$.dayOfWeek",Matchers.is("THU")));
  }

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

  @Test
  void shouldGetDTOandChangeSlot() throws Exception {
    Period p1 = new Period(1L, LocalTime.of(10, 0), LocalTime.of(20, 0),
        new HashSet<>(), new HashSet<>(), new HashSet<>());
    Week w1 = new Week(50L, new HashSet<>());
    User u1 = new User(1L, "interviewer@gmail.com", Role.INTERVIEWER);
    InterviewerSlot interviewerSlot1 = (new InterviewerSlot(1L, w1, DayOfWeek.TUE, p1, null, u1));


    JsonObj jsonObj = new JsonObj(46L, "FRI", "10:00", "20:00");
    mockMvc.perform(MockMvcRequestBuilders.post("/interviewers/{interviewerId}/slots/{slotId}", 1L, 1L)
          .content(objectMapper.writeValueAsString(jsonObj))
          .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.interviewerId", Matchers.is(1)));

    InterviewerSlot interviewerSlot2 = interviewerSlotService.getSlotByIdOne().get();

    assertThat(interviewerSlot1.getWeek().getId(), not(equalTo(interviewerSlot2.getWeek().getId())));
    assertThat(interviewerSlot1.getDayOfWeek(), not(equalTo(interviewerSlot2.getDayOfWeek())));
    assertEquals(interviewerSlot1.getId(), interviewerSlot2.getId());

  }
}



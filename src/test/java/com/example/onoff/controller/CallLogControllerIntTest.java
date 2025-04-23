package com.example.onoff.controller;

import com.example.onoff.domain.CallLog;
import com.example.onoff.domain.CallStatus;
import com.example.onoff.repository.CallLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "username")
public class CallLogControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CallLogRepository callLogRepository;

    @Test
    public void testGetAnswerRateWrongDateFormatReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-13-11")
            .param("numberOfShades", "7")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("error.validationFailed"))
        .andExpect(jsonPath("$.message", startsWith("Invalid value '2025-13-11' for parameter 'dateInput")));
    }

    @Test
    public void testGetAnswerRateMissingDateReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("numberOfShades", "7")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", startsWith("Required request parameter 'dateInput' for method parameter")));
    }

    @Test
    public void testGetAnswerRateMissingNumberOfShadesReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-11-12")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", startsWith("Required request parameter 'numberOfShades' for method parameter type")));
    }

    @Test
    public void testGetAnswerRateTooHighNumberOfShadesReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-10-11")
            .param("numberOfShades", "11")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("numberOfShades: must be less than or equal to 10")));
    }

    @Test
    public void testGetAnswerRateTooLowNumberOfShadesReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-10-11")
            .param("numberOfShades", "1")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("numberOfShades: must be greater than or equal to 3")));
    }

    @Test
    public void testGetAnswerRateTooHighStartHourReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-10-11")
            .param("numberOfShades", "8")
            .param("startHour", "24")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("startHour: must be less than or equal to 23")));
}

    @Test
    public void testGetAnswerRateTooLowStartHourReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "1999-10-11")
            .param("numberOfShades", "9")
            .param("startHour", "-2")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("startHour: must be greater than or equal to 0")));
    }

    @Test
    public void testGetAnswerRateTooHighEndHourReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-01-31")
            .param("numberOfShades", "9")
            .param("startHour", "22")
            .param("endHour", "24")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("endHour: must be less than or equal to 23")));
    }

    @Test
    public void testGetAnswerRateTooLowEndHourReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-10-11")
            .param("numberOfShades", "4")
            .param("startHour", "2")
            .param("endHour", "-1")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("endHour: must be greater than or equal to 0")));
    }

    @Test
    public void testGetAnswerRateEndHourLowerThanStartHourReturns400() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-10-11")
            .param("numberOfShades", "5")
            .param("startHour", "20")
            .param("endHour", "19")
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("End hour must be greater than or equal to start hour")));
    }

    @Test
    public void testGetAnswerRateReturnHeatMap() throws Exception {

        callLogRepository.deleteAll();
        LocalDate date = LocalDate.of(2025, 4, 20);

        // hour 1: 3 calls, 2 answered, 1 missed
        LocalDateTime h1 = date.atTime(1, 0);

        CallLog c1 = new CallLog();
        c1.setUserId("1");
        c1.setUsername("user1");
        c1.setOnoffNumber("100");
        c1.setContactNumber("200");
        c1.setStatus(CallStatus.ANSWERED);
        c1.setIncoming(true);
        c1.setDuration(60);
        c1.setStartedAt(h1);
        c1.setEndedAt(h1.plusSeconds(60));

        CallLog c2 = new CallLog();
        c2.setUserId("2");
        c2.setUsername("user2");
        c2.setOnoffNumber("101");
        c2.setContactNumber("201");
        c2.setStatus(CallStatus.ANSWERED);
        c2.setIncoming(true);
        c2.setDuration(120);
        c2.setStartedAt(h1);
        c2.setEndedAt(h1.plusSeconds(120));

        CallLog c3 = new CallLog();
        c3.setUserId("3");
        c3.setUsername("user3");
        c3.setOnoffNumber("102");
        c3.setContactNumber("202");
        c3.setStatus(CallStatus.MISSED);
        c3.setIncoming(false);
        c3.setDuration(30);
        c3.setStartedAt(h1);
        c3.setEndedAt(h1.plusSeconds(30));

        // hour 2: 3 calls, 0 answered
        LocalDateTime h2 = date.atTime(2, 0);

        CallLog c4 = new CallLog();
        c4.setUserId("u4");
        c4.setUsername("user4");
        c4.setOnoffNumber("103");
        c4.setContactNumber("203");
        c4.setStatus(CallStatus.MISSED);
        c4.setIncoming(true);
        c4.setDuration(45);
        c4.setStartedAt(h2);
        c4.setEndedAt(h2.plusSeconds(45));

        CallLog c5 = new CallLog();
        c5.setUserId("u5");
        c5.setUsername("user5");
        c5.setOnoffNumber("104");
        c5.setContactNumber("204");
        c5.setStatus(CallStatus.MISSED);
        c5.setIncoming(true);
        c5.setDuration(30);
        c5.setStartedAt(h2);
        c5.setEndedAt(h2.plusSeconds(30));

        CallLog c6 = new CallLog();
        c6.setUserId("u6");
        c6.setUsername("user6");
        c6.setOnoffNumber("105");
        c6.setContactNumber("205");
        c6.setStatus(CallStatus.MISSED);
        c6.setIncoming(false);
        c6.setDuration(15);
        c6.setStartedAt(h2);
        c6.setEndedAt(h2.plusSeconds(15));

        // hour 3: 2 calls, 1 answered
        LocalDateTime h3 = date.atTime(3, 0);

        CallLog c7 = new CallLog();
        c7.setUserId("u7");
        c7.setUsername("user7");
        c7.setOnoffNumber("106");
        c7.setContactNumber("206");
        c7.setStatus(CallStatus.ANSWERED);
        c7.setIncoming(false);
        c7.setDuration(90);
        c7.setStartedAt(h3);
        c7.setEndedAt(h3.plusSeconds(90));

        CallLog c8 = new CallLog();
        c8.setUserId("u8");
        c8.setUsername("user8");
        c8.setOnoffNumber("107");
        c8.setContactNumber("207");
        c8.setStatus(CallStatus.MISSED);
        c8.setIncoming(false);
        c8.setDuration(30);
        c8.setStartedAt(h3);
        c8.setEndedAt(h3.plusSeconds(30));

        callLogRepository.saveAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8));

        mockMvc.perform(get("/api/heatmap/answer-rate")
            .param("dateInput", "2025-04-20")
            .param("numberOfShades", "4")
            .param("startHour", "1")
            .param("endHour", "3")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        // hour 1
        .andExpect(jsonPath("$[0].hour").value(1))
        .andExpect(jsonPath("$[0].answeredCalls").value(2))
        .andExpect(jsonPath("$[0].totalCalls").value(3))
        .andExpect(jsonPath("$[0].rate").value(66.667))
        .andExpect(jsonPath("$[0].shade").value("Shade3"))
        // hour 2
        .andExpect(jsonPath("$[1].hour").value(2))
        .andExpect(jsonPath("$[1].answeredCalls").value(0))
        .andExpect(jsonPath("$[1].totalCalls").value(3))
        .andExpect(jsonPath("$[1].rate").value(0.0))
        .andExpect(jsonPath("$[1].shade").value("Shade1"))
        // hour 3
        .andExpect(jsonPath("$[2].hour").value(3))
        .andExpect(jsonPath("$[2].answeredCalls").value(1))
        .andExpect(jsonPath("$[2].totalCalls").value(2))
        .andExpect(jsonPath("$[2].rate").value(50.0))
        .andExpect(jsonPath("$[2].shade").value("Shade2"));
    }
}

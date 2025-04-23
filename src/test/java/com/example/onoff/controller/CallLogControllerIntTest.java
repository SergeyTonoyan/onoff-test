package com.example.onoff.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
}

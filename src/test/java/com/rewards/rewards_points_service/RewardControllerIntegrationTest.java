package com.rewards.rewards_points_service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewards.rewards_points_service.controller.RewardController;
import com.rewards.rewards_points_service.model.RewardResponse;
import com.rewards.rewards_points_service.service.RewardService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllRewards_withData() throws Exception {
        RewardResponse reward = new RewardResponse("cust1", Map.of("January", 90), 90);
        List<RewardResponse> rewards = List.of(reward);

        when(rewardService.calculateAllRewards()).thenReturn(rewards);

        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerId").value("cust1"))
                .andExpect(jsonPath("$[0].totalRewards").value(90))
                .andExpect(jsonPath("$[0].monthlyRewards.January").value(90));
    }

    @Test
    void testGetAllRewards_noData() throws Exception {
        when(rewardService.calculateAllRewards()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetRewardsByCustomerId_withData() throws Exception {
        RewardResponse reward = new RewardResponse("cust1", Map.of("March", 120), 120);

        when(rewardService.getRewardsByCustomerId("cust1")).thenReturn(reward);

        mockMvc.perform(get("/api/rewards/cust1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("cust1"))
                .andExpect(jsonPath("$.totalRewards").value(120))
                .andExpect(jsonPath("$.monthlyRewards.March").value(120));
    }

    @Test
    void testGetRewardsByCustomerId_noData() throws Exception {
        RewardResponse emptyReward = new RewardResponse("custX", new HashMap<>(), 0);

        when(rewardService.getRewardsByCustomerId("custX")).thenReturn(emptyReward);

        mockMvc.perform(get("/api/rewards/custX"))
                .andExpect(status().isNoContent());
    }
}
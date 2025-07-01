package com.rewards.rewards_points_service;

import com.rewards.rewards_points_service.controller.RewardController;
import com.rewards.rewards_points_service.model.RewardResponse;
import com.rewards.rewards_points_service.service.RewardService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardController rewardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRewards_withData() {
        List<RewardResponse> mockRewards = List.of(
            new RewardResponse("cust1", Map.of("January", 90), 90)
        );

        when(rewardService.calculateAllRewards()).thenReturn(mockRewards);

        ResponseEntity<List<RewardResponse>> response = rewardController.getAllRewards();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(rewardService, times(1)).calculateAllRewards();
    }

    @Test
    void testGetAllRewards_noData() {
        when(rewardService.calculateAllRewards()).thenReturn(Collections.emptyList());

        ResponseEntity<List<RewardResponse>> response = rewardController.getAllRewards();

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testGetRewardsByCustomerId_withData() {
        RewardResponse mockResponse = new RewardResponse("cust1", Map.of("March", 120), 120);

        when(rewardService.getRewardsByCustomerId("cust1")).thenReturn(mockResponse);

        ResponseEntity<RewardResponse> response = rewardController.getRewardsByCustomerId("cust1");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("cust1", response.getBody().getCustomerId());
        assertEquals(120, response.getBody().getTotalRewards());
    }

    @Test
    void testGetRewardsByCustomerId_noData() {
        RewardResponse emptyResponse = new RewardResponse("custX", new HashMap<>(), 0);

        when(rewardService.getRewardsByCustomerId("custX")).thenReturn(emptyResponse);

        ResponseEntity<RewardResponse> response = rewardController.getRewardsByCustomerId("custX");

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
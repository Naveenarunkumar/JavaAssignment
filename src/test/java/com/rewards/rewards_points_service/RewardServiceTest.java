package com.rewards.rewards_points_service;

import com.rewards.rewards_points_service.model.RewardResponse;
import com.rewards.rewards_points_service.service.RewardService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

class RewardServiceTest {

    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        rewardService = new RewardService();
    }

    @Test
    void testCalculateAllRewards_returnsCorrectData() {
        List<RewardResponse> rewards = rewardService.calculateAllRewards();

        assertThat(rewards).isNotEmpty();
        assertThat(rewards).anyMatch(r -> r.getCustomerId().equals("cust1"));
        assertThat(rewards).anyMatch(r -> r.getTotalRewards() > 0);
    }

    @Test
    void testGetRewardsByCustomerId_validCustomer() {
        RewardResponse response = rewardService.getRewardsByCustomerId("cust1");

        assertThat(response).isNotNull();
        assertThat(response.getCustomerId()).isEqualTo("cust1");
        assertThat(response.getTotalRewards()).isGreaterThan(0);
        assertThat(response.getMonthlyRewards()).isNotEmpty();
    }

    @Test
    void testGetRewardsByCustomerId_invalidCustomer_returnsEmpty() {
        RewardResponse response = rewardService.getRewardsByCustomerId("unknown");

        assertThat(response).isNotNull();
        assertThat(response.getCustomerId()).isEqualTo("unknown");
        assertThat(response.getTotalRewards()).isEqualTo(0);
        assertThat(response.getMonthlyRewards()).isEmpty();
    }

    @Test
    void testGetRewardsByCustomerId_nullCustomer_returnsEmpty() {
        RewardResponse response = rewardService.getRewardsByCustomerId(null);

        assertThat(response).isNotNull();
        assertThat(response.getCustomerId()).isEmpty();
        assertThat(response.getTotalRewards()).isEqualTo(0);
        assertThat(response.getMonthlyRewards()).isEmpty();
    }
}
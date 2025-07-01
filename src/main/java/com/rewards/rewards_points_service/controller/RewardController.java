package com.rewards.rewards_points_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rewards.rewards_points_service.model.RewardResponse;
import com.rewards.rewards_points_service.service.RewardService;

/**
 * REST controller to handle reward calculation requests.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private static final Logger logger = LoggerFactory.getLogger(RewardController.class);

    @Autowired
    private RewardService rewardService;

    /**
     * Retrieves the reward details for all customers.
     *
     * @return ResponseEntity containing a list of RewardResponse objects
     */
    @GetMapping
    public ResponseEntity<List<RewardResponse>> getAllRewards() {
        logger.info("Fetching rewards for all customers");
        List<RewardResponse> rewards = rewardService.calculateAllRewards();

        if (rewards.isEmpty()) {
            logger.info("No rewards found for any customer");
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        logger.info("Successfully retrieved rewards for all customers");
        return ResponseEntity.ok(rewards); // 200 OK
    }

    /**
     * Retrieves reward details for a specific customer by ID.
     *
     * @param customerId the ID of the customer
     * @return ResponseEntity containing the reward details or a 204 No Content response if no data is found
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardResponse> getRewardsByCustomerId(@PathVariable("customerId") String customerId) {
        logger.info("Received request to fetch rewards for customer ID: {}", customerId);

        RewardResponse response = rewardService.getRewardsByCustomerId(customerId);

        if (response.getMonthlyRewards().isEmpty() && response.getTotalRewards() == 0) {
            logger.info("No rewards found for customer ID: {}", customerId);
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        logger.info("Successfully retrieved rewards for customer ID: {}", customerId);
        return ResponseEntity.ok(response); // 200 OK
    }
}

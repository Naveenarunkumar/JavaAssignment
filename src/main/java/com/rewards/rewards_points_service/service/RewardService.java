package com.rewards.rewards_points_service.service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.rewards.rewards_points_service.exception.InvalidTransactionException;
import com.rewards.rewards_points_service.model.RewardResponse;
import com.rewards.rewards_points_service.model.Transaction;

/**
 * Service class containing the core business logic to calculate rewards.
 * It supports:
 * - Monthly and total reward points for all customers
 * - Monthly and total reward points for a specific customer
 */
@Service
public class RewardService {

    private static final Logger logger = LoggerFactory.getLogger(RewardService.class);

    /**
     * A sample list of transactions.
     * In a real-world application, this would likely come from a database.
     */
    private static final List<Transaction> transactions = new ArrayList<>(
        List.of(
            new Transaction("cust1", 120, "2025-01-15"),
            new Transaction("cust1", 80, "2025-02-10"),
            new Transaction("cust1", 120, "2025-03-15"),
            new Transaction("cust2", 80, "2025-01-10"),
            new Transaction("cust2", 120, "2025-02-15"),
            new Transaction("cust2", 80, "2025-03-10")
        )
    );

    /**
     * Calculates rewards for all customers per month and in total.
     *
     * @return list of reward responses
     */
    public List<RewardResponse> calculateAllRewards() {
        Map<String, Map<String, Integer>> customerRewards = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                throw new InvalidTransactionException(
                    "Transaction amount cannot be negative: " + transaction.getAmount());
            }

            String customerId = transaction.getCustomerId();
            Month month = transaction.getTransactionDate().getMonth();
            String monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            int points = calculatePoints(transaction.getAmount());

            customerRewards
                .computeIfAbsent(customerId, k -> new HashMap<>())
                .merge(monthName, points, Integer::sum);
        }

        logger.info("Calculating rewards for all customers...");

        return customerRewards.entrySet().stream()
            .map(entry -> {
                Map<String, Integer> monthly = entry.getValue();
                int total = monthly.values().stream().mapToInt(Integer::intValue).sum();
                logger.info("Customer ID: {}", entry.getKey());
                logger.info("Monthly rewards: {}", monthly);
                logger.info("Total rewards: {}", total);
                return new RewardResponse(entry.getKey(), monthly, total);
            })
            .collect(Collectors.toList());
    }

    /**
     * Calculates rewards for a single customer.
     *
     * @param customerId the ID of the customer
     * @return RewardResponse with monthly and total rewards, or empty if no transactions found
     */
    public RewardResponse getRewardsByCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            logger.warn("Invalid customer ID provided: {}", customerId);
            return new RewardResponse("", new HashMap<>(), 0);
        }

        Map<String, Integer> monthlyRewards = new HashMap<>();
        boolean customerExists = false;

        for (Transaction transaction : transactions) {
            if (!transaction.getCustomerId().equals(customerId)) {
                continue;
            }

            customerExists = true;

            if (transaction.getAmount() < 0) {
                throw new InvalidTransactionException(
                    "Transaction amount cannot be negative: " + transaction.getAmount());
            }

            int points = calculatePoints(transaction.getAmount());
            String monthName = transaction.getTransactionDate().getMonth()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            monthlyRewards.merge(monthName, points, Integer::sum);
        }

        if (!customerExists) {
            logger.info("No transactions found for customer ID: {}", customerId);
            return new RewardResponse(customerId, new HashMap<>(), 0);
        }

        int totalPoints = monthlyRewards.values().stream().mapToInt(Integer::intValue).sum();
        logger.info("Calculating rewards for customer ID: {}", customerId);
        logger.info("Monthly rewards: {}", monthlyRewards);
        logger.info("Total rewards: {}", totalPoints);

        return new RewardResponse(customerId, monthlyRewards, totalPoints);
    }

    /**
     * Calculates reward points based on the transaction amount.
     *
     * Rewards are given as:
     * - 2 points for every dollar spent over $100
     * - 1 point for every dollar spent over $50 up to $100
     *
     * @param amount the transaction amount
     * @return the calculated reward points
     */
    private int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += 2 * ((int) amount - 100);
            points += 50; // for $50-$100
        } else if (amount > 50) {
            points += (int) amount - 50;
        }
        return points;
    }
}

package com.example.drools;

import com.example.drools.model.CustomerType;
import com.example.drools.model.Order;
import com.example.drools.model.OrderResult;
import com.example.drools.service.RuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RuleServiceTest {

    @Autowired
    private RuleService ruleService;

    @Test
    public void testVipDiscount() {
        Order order = new Order(250, 3, CustomerType.VIP, null);
        OrderResult result = ruleService.evaluate(order);

        assertEquals(15, result.getDiscountPercentage(), 0.01);
        assertTrue(result.isFreeShipping());
        assertTrue(result.getLoyaltyPoints() > 0);
        assertFalse(result.getFiredRules().isEmpty());
    }

    @Test
    public void testPremiumDiscount() {
        Order order = new Order(100, 2, CustomerType.PREMIUM, null);
        OrderResult result = ruleService.evaluate(order);

        assertEquals(10, result.getDiscountPercentage(), 0.01);
        assertTrue(result.isFreeShipping());
    }

    @Test
    public void testRegularDiscount() {
        Order order = new Order(30, 1, CustomerType.REGULAR, null);
        OrderResult result = ruleService.evaluate(order);

        assertEquals(5, result.getDiscountPercentage(), 0.01);
        assertFalse(result.isFreeShipping());
    }

    @Test
    public void testLargeOrderBonus() {
        Order order = new Order(600, 2, CustomerType.REGULAR, null);
        OrderResult result = ruleService.evaluate(order);

        assertEquals(10, result.getDiscountPercentage(), 0.01);
    }

    @Test
    public void testCouponSave20() {
        Order order = new Order(100, 1, CustomerType.REGULAR, "SAVE20");
        OrderResult result = ruleService.evaluate(order);

        assertTrue(result.getDiscountPercentage() >= 25);
    }

    @Test
    public void testLoyaltyPointsVipMultiplier() {
        Order order = new Order(100, 1, CustomerType.VIP, null);
        OrderResult result = ruleService.evaluate(order);

        assertTrue(result.getLoyaltyPoints() >= 30);
    }
}

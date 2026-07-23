package com.example.drools.service;

import com.example.drools.model.Order;
import com.example.drools.model.OrderResult;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.springframework.stereotype.Service;

@Service
public class RuleService {

    private final KieContainer kieContainer;

    public RuleService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public OrderResult evaluate(Order order) {
        KieSession kieSession = kieContainer.newKieSession();
        OrderResult result = new OrderResult();

        kieSession.addEventListener(new DefaultAgendaEventListener() {
            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                result.addFiredRule(event.getMatch().getRule().getName());
            }
        });

        kieSession.insert(order);
        kieSession.insert(result);
        kieSession.fireAllRules();
        kieSession.dispose();

        result.setDiscountAmount(order.getTotalAmount() * result.getDiscountPercentage() / 100.0);
        result.setFinalAmount(order.getTotalAmount() - result.getDiscountAmount());

        return result;
    }
}

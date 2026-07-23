package com.example.drools.controller;

import com.example.drools.model.Order;
import com.example.drools.model.OrderResult;
import com.example.drools.service.RuleService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class OrderController {

    private final RuleService ruleService;
    private final KieContainer kieContainer;

    public OrderController(RuleService ruleService, KieContainer kieContainer) {
        this.ruleService = ruleService;
        this.kieContainer = kieContainer;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/api/orders/evaluate")
    @ResponseBody
    public ResponseEntity<OrderResult> evaluateOrder(@RequestBody Order order) {
        OrderResult result = ruleService.evaluate(order);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/rules/info")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> getRulesInfo() {
        List<Map<String, String>> rules = new ArrayList<Map<String, String>>();
        for (KiePackage kiePackage : kieContainer.getKieBase().getKiePackages()) {
            for (Rule rule : kiePackage.getRules()) {
                Map<String, String> ruleInfo = new LinkedHashMap<String, String>();
                ruleInfo.put("name", rule.getName());
                ruleInfo.put("package", rule.getPackageName());
                rules.add(ruleInfo);
            }
        }
        return ResponseEntity.ok(rules);
    }
}

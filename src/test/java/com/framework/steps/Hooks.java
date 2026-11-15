package com.framework.steps;

import com.framework.core.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class Hooks {

    private final ScenarioContext ctx;

    public Hooks(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed() && ctx.hasPage()) {
            scenario.attach(ctx.page().screenshot(), "image/png", "failure-screenshot");
        }
        ctx.close();
    }
}

package com.framework.steps;

import com.framework.config.ConfigManager;
import com.framework.core.ScenarioContext;
import com.framework.ui.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UiSteps {

    private final ScenarioContext ctx;

    public UiSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Given("I open the login page")
    public void openLoginPage() {
        new LoginPage(ctx.page()).open();
    }

    @Given("I wait for {int} seconds")
    public void waitForSeconds(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }

    @When("I log in with username {string} and password {string}")
    public void logIn(String username, String password) {
        new LoginPage(ctx.page()).login(username, password);
    }

    @When("I log in with configured credentials")
    public void logInWithConfiguredCredentials() {
        logIn(ConfigManager.require("ui.username"), ConfigManager.require("ui.password"));
    }

    @Then("I should see the message {string}")
    public void shouldSeeMessage(String message) {
        assertThat(ctx.page().locator("#flash")).containsText(message);
    }

        @When("user click logout")
    public void userClickLogout() {
        // new LoginPage(ctx.page()).logout();
    }
}

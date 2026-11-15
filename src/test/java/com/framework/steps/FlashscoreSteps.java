package com.framework.steps;

import com.framework.config.ConfigManager;
import com.framework.core.ScenarioContext;
import com.framework.ui.pages.FlashscorePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FlashscoreSteps {

    private final ScenarioContext ctx;

    public FlashscoreSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    /** Opens the site whose URL is given as ui.NAME.url, e.g. -PflashscoreUrl=https://... */
    @Given("^I open the \"([^\"]*)\" site$")
    public void openSite(String name) {
        String key = "ui." + name + ".url";
        String url = ConfigManager.get(key);
        if (url == null || url.isBlank()) {
            throw new IllegalStateException("No URL for site '" + name + "'. Pass -P" + name + "Url=https://...");
        }
        ctx.page().navigate(url);
    }

    @When("I click the 4th item in the sport top menu")
    public void clickSportMenuItem4() {
        new FlashscorePage(ctx.page()).clickSportMenuItem4();
    }

    @Then("I should see the 4th item in the live table")
    public void shouldSeeLiveTableItem4() {
        assertThat(new FlashscorePage(ctx.page()).liveTableItem4()).isVisible();
    }

    @Then("I close the browser")
    public void closeBrowser() {
        ctx.close();
    }
}

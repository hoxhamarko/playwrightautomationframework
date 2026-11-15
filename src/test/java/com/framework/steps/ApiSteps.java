package com.framework.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.framework.api.ApiClient;
import com.framework.core.ScenarioContext;
import com.microsoft.playwright.APIResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiSteps {

    private final ScenarioContext ctx;

    public ApiSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    private ApiClient client() {
        return new ApiClient(ctx.api());
    }

    private APIResponse response() {
        return ctx.get("response");
    }

    @When("I send a GET request to {string}")
    public void get(String path) {
        ctx.put("response", client().get(path));
    }

    @When("I send a POST request to {string} with body:")
    public void post(String path, String body) {
        ctx.put("response", client().post(path, body));
    }

    @When("I send a PUT request to {string} with body:")
    public void put(String path, String body) {
        ctx.put("response", client().put(path, body));
    }

    @When("I send a DELETE request to {string}")
    public void delete(String path) {
        ctx.put("response", client().delete(path));
    }

    @Then("the response status should be {int}")
    public void statusShouldBe(int status) {
        assertThat(response().status()).isEqualTo(status);
    }

    /** Dot notation for nested fields, e.g. "address.city". */
    @Then("the response JSON field {string} should be {string}")
    public void jsonFieldShouldBe(String field, String expected) {
        JsonNode node = ApiClient.parse(response()).at("/" + field.replace('.', '/'));
        assertThat(node.asText()).isEqualTo(expected);
    }
}

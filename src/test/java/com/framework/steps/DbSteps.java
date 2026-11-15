package com.framework.steps;

import com.framework.core.ScenarioContext;
import com.framework.db.DbClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DbSteps {

    private final ScenarioContext ctx;

    public DbSteps(ScenarioContext ctx) {
        this.ctx = ctx;
    }

    @Given("I execute SQL query {string}")
    public void executeQuery(String sql) {
        ctx.put("rows", DbClient.query(sql));
    }

    @Given("I execute SQL update {string}")
    public void executeUpdate(String sql) {
        DbClient.update(sql);
    }

    @Then("the query should return {int} row(s)")
    public void rowCountShouldBe(int count) {
        assertThat(rows()).hasSize(count);
    }

    /** Column names are matched case-insensitively, since Oracle returns them in upper case. */
    @Then("row {int} column {string} should be {string}")
    public void cellShouldBe(int row, String column, String expected) {
        Map<String, Object> record = rows().get(row - 1);
        Object actual = record.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(column))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new AssertionError("No column named " + column));
        assertThat(String.valueOf(actual)).isEqualTo(expected);
    }

    private List<Map<String, Object>> rows() {
        return ctx.get("rows");
    }
}

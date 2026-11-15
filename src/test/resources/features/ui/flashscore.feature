@ui
Feature: Flashscore

  @flashscore
  Scenario: Open Flashscore and check the live table
    Given I open the "flashscore" site
    When I click the 4th item in the sport top menu
    Then I should see the 4th item in the live table
    And I wait for 20 seconds
    And I close the browser

  @flashscore1
  Scenario: Open Livescore
    Given I open the "livescore" site
    And I wait for 20 seconds
    And I close the browser
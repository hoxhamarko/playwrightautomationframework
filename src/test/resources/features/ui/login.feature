@ui
Feature: Login page

@testlogin
  Scenario: Successful login
    Given I open the login page
  And I wait for 30 seconds
  When I log in with configured credentials
  Then I should see the message "You logged into a secure area!"
  And user click logout

 @ermalis
  Scenario: Failed login with wrong username
    Given I open the login page
    When I log in with username "wrong" and password "wrong"
    Then I should see the message "Your username is invalid!"

    @newbranchWork
  Scenario: Failed login with wrong username
    Given I open the login page
    When I log in with username "wrong" and password "wrong"
    Then I should see the message "Your username is invalid!"

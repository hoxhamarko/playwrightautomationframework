@db
Feature: Database checks
  # Replace the SQL and expected values with your own schema.

  Scenario: A user row exists with the expected email
    Given I execute SQL query "SELECT id, email FROM users WHERE id = 1"
    Then the query should return 1 row
    And row 1 column "email" should be "user@example.com"

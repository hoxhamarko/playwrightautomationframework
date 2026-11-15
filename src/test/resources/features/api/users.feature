@api
Feature: Users and posts API

  Scenario: Get a user
    When I send a GET request to "/users/1"
    Then the response status should be 200
    And the response JSON field "name" should be "Leanne Graham"

  Scenario: Create a post
    When I send a POST request to "/posts" with body:
      """
      {"title": "foo", "body": "bar", "userId": 1}
      """
    Then the response status should be 201
    And the response JSON field "title" should be "foo"

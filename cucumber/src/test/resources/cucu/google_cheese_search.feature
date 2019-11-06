Feature: Google Cheese Search
  Search for a cheese in google

  Scenario: Finding some cheese
   Given I am on the Google search page
   When I search for "Cheese!"
   Then the page title should start with "cheese"
Feature: Text select and copy
  Select entire line at current cursor and put it in clipboard

  Scenario: Selecting text
   Given I am on the Notepad++
   When I select and copy entire line and copy
   Then clipboard should not be empty
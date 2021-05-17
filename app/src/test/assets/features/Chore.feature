Feature: Create Chore

  @Chore
  Scenario: Validate Chore
    Given the user that fills up a chore without an assignee
    When the user tries to create the chore
    Then the user will not be able to create it

  @Chore
  Scenario: Importance Low By Default
    Given the user wants to create a new chore
    When the user enters the add chore page
    Then the user will see that the importance is set to Low by default

  @Chore
  Scenario: Today's Date By Default
    Given the user wants to create a new chore
    When the user enters the add chore page
    Then the user will see that the expiration date is set to today by default

  @Chore
  Scenario: Expiration Date Selection
    Given the user wants to create a new chore
    When the user selects a new expiration date
    Then the user will see that the expiration date is set to the one selected

  @Chore
  Scenario Outline: Valid Length 1
    Given the user that enters a chore "<name>"
    When the user validates the chore name
    Then the chore name length must be less or equal than 32
    Examples:
      | name                                       |
      | Recoger la habitación                      |
      | Lavar los platos y la cocina hoy           |

  @Chore
  Scenario Outline: Valid Length 2
    Given the user that enters a chore "<name>"
    When the user validates the chore name
    Then the chore name length must be greater or equal than 5
    Examples:
      | name                                       |
      | Lavar coche                                |
      | Lavar                                      |

  @Chore
  Scenario Outline: Invalid Length 1
    Given the user that enters a chore "<name>"
    When the user validates the chore name
    Then the chore name length must not be greater than 32
    Examples:
      | name                                            |
      | Lavar los platos y la cocina para ayer          |

  @Chore
  Scenario Outline: Invalid Length 2
    Given the user that enters a chore "<name>"
    When the user validates the chore name
    Then the chore name length must not be less than 5
    Examples:
      | name |
      | Casa |

  @Chore
  Scenario Outline: Emptiness
    Given the user that enters a chore "<name>"
    When the user validates the chore name
    Then the chore name must not be empty
    Examples:
      | name              |
      |      \0           |

  @Chore
  Scenario Outline: Valid Characters
    Given the user that enters a chore "<name>"
    When the user validates the chore name
    Then the chore name must only contain alphanumerical characters with spaces
    Examples:
      | name |
      | Avisar a César Núñez a las 8 PM |

  @Chore
  Scenario Outline: Invalid Characters
    Given the user that enters a chore "<name>"
    When the user validates the chore name
    Then the chore name must not contain special characters
    Examples:
      | name                     |
      | *o$* Lavar Ropa *$o*     |
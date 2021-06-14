Feature: Create Chore

  @Chore
  Scenario: Validate Invalid Chore
    Given the user that fills up a invalid chore
    When the user tries to create the chore
    Then the user will not be able to create the chore

  @Chore
  Scenario: Validate Valid Chore
    Given the user that fills up a valid chore
    When the user tries to create the chore
    Then the user will be able to create the chore

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

  @Chore
  Scenario Outline: (Creator) User Has Permissions For Updating a Chore
    Given a user with id "<id>" that wants to update a chore
    When the chore creator is "<creator>"
    Then the user is permitted to update it
    Examples:
      | id   | creator |
      | RAUL | RAUL    |

  @Chore
  Scenario Outline: (Admin) User Has Permissions For Updating a Chore
    Given a user with id "<id>" that wants to update a chore
    When the user is the admin of the group
    Then the user is permitted to update it
    Examples:
      | id   |
      | RAUL |

  @Chore
  Scenario Outline: User Has Not Permissions For Updating a Chore
    Given a user with id "<id>" that wants to update a chore
    When the chore creator is "<creator>"
    Then the user is not permitted to update it
    Examples:
      | id   | creator |
      | RAUL | GABO    |

  @Chore
  Scenario Outline: (Admin) User Has Permissions For Removing a Chore
    Given a user with id "<id>" that wants to remove a chore
    When the user is the admin of the group
    Then the user is permitted to remove it
    Examples:
      | id   |
      | RAUL |

  @Chore
  Scenario Outline: (Creator) User Has Permissions For Removing a Chore
    Given a user with id "<id>" that wants to remove a chore
    When the chore creator is "<creator>"
    Then the user is permitted to remove it
    Examples:
      | id   | creator |
      | RAUL | RAUL    |

  @Chore
  Scenario Outline: User Has Not Permissions For Removing a Chore
    Given a user with id "<id>" that wants to remove a chore
    When the chore is not the creator "<creator>" of the chore nor the admin
    Then the user is not permitted to remove it
    Examples:
      | id   | creator |
      | RAUL | GABO    |

  @Chore
  Scenario Outline: (Admin) User Has Permissions For Completing a Chore
    Given a user with id "<id>" that wants to complete a chore
    When the user is the admin of the group
    Then the user is permitted to complete it
    Examples:
      | id   |
      | RAUL |

  @Chore
  Scenario Outline: (Creator/Assignee) User Has Permissions For Completing a Chore
    Given a user with id "<id>" that wants to complete a chore
    When the chore creator is either the "<creator>" or the assignee "<assignee>"
    Then the user is permitted to complete it
    Examples:
      | id   | creator | assignee |
      | RAUL | RAUL    | RAUL     |

  @Chore
  Scenario Outline: User Has Not Permissions For Completing a Chore
    Given a user with id "<id>" that wants to complete a chore
    When the chore is not the creator "<creator>" of the chore, nor the assignee "<assignee>", nor the admin
    Then the user is not permitted to complete it
    Examples:
      | id   | creator | assignee |
      | RAUL | GABO    | ALEX     |
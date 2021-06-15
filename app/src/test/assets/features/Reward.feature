Feature: Create Reward

  @Reward
  Scenario: Validate Invalid Reward
    Given the user that fills up a invalid reward
    When the user tries to create the reward
    Then the user will not be able to create the reward

  @Reward
  Scenario: Validate Valid Reward
    Given the user that fills up a valid reward
    When the user tries to create the reward
    Then the user will be able to create the reward

  @Reward
  Scenario Outline: Valid Length 1
    Given the user that enters the reward description "<description>"
    When the user validates its reward
    Then the reward description length must be less or equal than 48
    Examples:
      | description                                      |
      | Una cerveza para tomar fresqui pagada por Olmedo |
      | Cenita pagada entre todos para el ganador        |

  @Reward
  Scenario Outline: Valid Length 2
    Given the user that enters the reward description "<description>"
    When the user validates its reward
    Then the reward description length must be greater or equal than 5
    Examples:
      | description                                |
      | Cerve                                      |
      | Cerves                                     |

  @Reward
  Scenario Outline: Invalid Length 1
    Given the user that enters the reward description "<description>"
    When the user validates its reward
    Then the reward description length must not be greater than 48
    Examples:
      | description                                        |
      | Una cerveza para tomar fresquita pagada por Olmedo |

  @Reward
  Scenario Outline: Invalid Length 2
    Given the user that enters the reward description "<description>"
    When the user validates its reward
    Then the reward description length must not be less than 5
    Examples:
      | description |
      | Agua        |

  @Reward
  Scenario Outline: Emptiness
    Given the user that enters the reward description "<description>"
    When the user validates its reward
    Then the reward description must not be empty
    Examples:
      | description |
      |      \0     |

  @Reward
  Scenario Outline: Valid Characters
    Given the user that enters the reward description "<description>"
    When the user validates its reward
    Then the reward description must only contain alphanumerical characters with spaces
    Examples:
      | description           |
      | Gabriel paga 20 euros |

  @Reward
  Scenario Outline: Invalid Characters
    Given the user that enters the reward description "<description>"
    When the user validates its reward
    Then the reward description must not contain special characters
    Examples:
      | description                     |
      | Gabriel paga mucha plata $$$    |

  @Reward
  Scenario: (Admin) User Has Permissions For Creating the Reward
    Given a user that wants to create the reward
    When the user is the administrator of the group
    Then the user is permitted to modify it

  @Reward
  Scenario: User Has Not Permissions For Creating the Reward
    Given a user that wants to create the reward
    When the user is not the admin of the group
    Then the user is not permitted to modify it

  @Reward
  Scenario: (Admin) User Has Permissions For Updating the Reward
    Given a user that wants to update the reward
    When the user is the administrator of the group
    Then the user is permitted to modify it

  @Reward
  Scenario: User Has Not Permissions For Updating the Reward
    Given a user that wants to update the reward
    When the user is not the admin of the group
    Then the user is not permitted to modify it

  @Reward
  Scenario: (Admin) User Has Permissions For Removing the Reward
    Given a user that wants to remove the reward
    When the user is the administrator of the group
    Then the user is permitted to modify it

  @Reward
  Scenario: User Has Not Permissions For Removing the Reward
    Given a user that wants to remove the reward
    When the user is not the admin of the group
    Then the user is not permitted to modify it
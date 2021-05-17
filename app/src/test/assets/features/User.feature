Feature: Create User

  @User
  Scenario Outline: Valid Length 1
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username length must be less or equal than 32
    Examples:
      | name                                       |
      | Gabriel                                    |
      | Sergio Valdivieso Gomezos Garcia           |

  @User
  Scenario Outline: Valid Length 2
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username length must be greater or equal than 3
    Examples:
      | name                                       |
      | Gabriel                                    |
      | Aya                                        |

  @User
  Scenario Outline: Invalid Length 1
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username length must not be greater than 32
    Examples:
      | name                                         |
      | Sergio Valdivieso Gomez Garcia de los Santos |

  @User
  Scenario Outline: Invalid Length 2
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username length must not be less than 3
    Examples:
      | name |
      | Po   |

  @User
  Scenario Outline: Emptiness
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username must not be empty
    Examples:
      | name              |
      |        \0         |

  @User
  Scenario Outline: Valid Characters
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username must only contain alphabetical characters with spaces
    Examples:
      | name |
      | Gabriel Garçía López |

  @User
  Scenario Outline: Invalid Characters 1
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username must not contain numbers
    Examples:
      | name          |
      | 1997 Raul     |

  @User
  Scenario Outline: Invalid Characters 2
    Given the user that enters its username "<name>"
    When the user validates its username
    Then the username must not contain special characters
    Examples:
      | name                    |
      | (**Olmedo**) - ¡El Amo? |
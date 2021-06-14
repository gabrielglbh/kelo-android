Feature: Create Group

  @Group
  Scenario Outline: Valid Length 1
    Given the user that enters its group name "<name>"
    When the user validates its group name
    Then the group name length must be less or equal than 32
    Examples:
      | name                                       |
      | Casita de Verano                           |
      | Casa Rural de Verano 2021 Madrid           |

  @Group
  Scenario Outline: Valid Length 2
    Given the user that enters its group name "<name>"
    When the user validates its group name
    Then the group name length must be greater or equal than 5
    Examples:
      | name                                       |
      | La Casa del Ron                            |
      | Casas                                      |

  @Group
  Scenario Outline: Invalid Length 1
    Given the user that enters its group name "<name>"
    When the user validates its group name
    Then the group name length must not be greater than 32
    Examples:
      | name                                         |
      | Verano 2022 Free Corona House Indoor         |

  @Group
  Scenario Outline: Invalid Length 2
    Given the user that enters its group name "<name>"
    When the user validates its group name
    Then the group name length must not be less than 5
    Examples:
      | name |
      | Casa |

  @Group
  Scenario Outline: Emptiness
    Given the user that enters its group name "<name>"
    When the user validates its group name
    Then the group name must not be empty
    Examples:
      | name              |
      |      \0           |

  @Group
  Scenario Outline: Valid Characters
    Given the user that enters its group name "<name>"
    When the user validates its group name
    Then the group name must only contain alphanumerical characters with spaces
    Examples:
      | name                        |
      | 2022 Verano Guipúzcua Ópera |

  @Group
  Scenario Outline: Invalid Characters
    Given the user that enters its group name "<name>"
    When the user validates its group name
    Then the group name must not contain special characters
    Examples:
      | name                     |
      | *o$* Casita Rural *$o*   |

  @Group
  Scenario: (Admin) User Has Permissions For Updating the Group Name
    Given a user that wants to update the group name
    When the user is the unique administrator of the group
    Then the user is permitted to modify the group name

  @Group
  Scenario: User Has Not Permissions For Updating the Group Name
    Given a user that wants to update the group name
    When the user is not the unique administrator of the group
    Then the user is not permitted to modify the group name

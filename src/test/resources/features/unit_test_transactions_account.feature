Feature: Unit tests collection to manage transactions of a Nubank credit card
  Scenario: Create account with an active credit card and valid available limit amount
    Given Active card: true, available limit: 100
    Then Account is created successfully
  Scenario: The existing account is immutable
    Given Active card: true, available limit: 100, and id account: "400c8afa-7920-4f04-94b6-5a4ba34298a6"
    When Trying modify existing account
    Then Return account already initialized "account-already-initialized" as violation
  Scenario: No transaction should be accepted without a properly initialized account
    Given Account with id "400c8afa-7920-4f04-94b6-5a4ba34298a2"
    And Transaction amount: 20, merchant: "KFC", time: "2019-02-13T11:00:00.000Z"
    Then Return violation on create transaction "account-not-initialized"
  Scenario: Create transaction of a valid credit card
    Given Active card: true, available limit: 100, and id account: "400c8afa-7920-4f04-94b6-5a4ba34298a7"
    And Transaction amount: 50, merchant: "Burger King", time: "2019-02-13T11:00:00.000Z"
    Then Account is created successfully
    Then Create transaction successfully
  Scenario: No transaction should be accepted when the card is not active
    Given Active card: false, available limit: 100, and id account: "400c8afa-7920-4f04-94b6-5a4ba34298a8"
    Then Account is created successfully
    And Transaction amount: 10, merchant: "McDonald's", time: "2019-02-13T11:00:00.000Z"
    Then Return violation on create transaction "card-not-active"
  Scenario: The transaction amount should not exceed the available limit: insufficient-limit
    Given Active card: true, available limit: 100, and id account: "400c8afa-7920-4f04-94b6-5a4ba34298a0"
    And Transaction amount: 200, merchant: "Burger King", time: "2019-02-13T11:00:00.000Z"
    Then Account is created successfully
    Then Return violation on create transaction "insufficient-limit"
  Scenario: There should be no more than 3 transactions within a 2 minutes interval
    Given Active card: true, available limit: 100, and id account: "400c8afa-7920-4f04-94b6-5a4ba34298b0"
    And Transaction amount: 10, merchant: "Burger King", time: "2019-02-13T11:00:00.000Z"
    Then Account is created successfully
    Then Create transaction successfully
    And Transaction amount: 5, merchant: "Texas Chicken", time: "2019-02-13T11:00:05.000Z"
    Then Create transaction successfully
    And Transaction amount: 20, merchant: "KFC", time: "2019-02-13T11:00:10.000Z"
    Then Create transaction successfully
    And Transaction amount: 8, merchant: "Mc Donald's", time: "2019-02-13T11:02:00.000Z"
    Then Return violation on create transaction "high-frequency-small-interval"
  Scenario: There should be no more than 1 similar transaction (same amount and merchant) within a 2 minutes interval
    Given Active card: true, available limit: 100, and id account: "400c8afa-7920-4f04-94b6-8a4ba34298b0"
    And Transaction amount: 10, merchant: "Burger King", time: "2019-02-13T11:00:00.000Z"
    Then Account is created successfully
    Then Create transaction successfully
    And Transaction amount: 10, merchant: "Burger King", time: "2019-02-13T11:02:01.000Z"
    Then Create transaction successfully
    And Transaction amount: 10, merchant: "Burger King", time: "2019-02-13T11:02:10.000Z"
    Then Return violation on create transaction "doubled-transaction"
  Scenario: Validate multiple violations in transaction
    Given Active card: true, available limit: 100, and id account: "400c8afa-7920-4f74-94b6-8a4ba34298b0"
    Then Account is created successfully
    And Transaction amount: 10, merchant: "McDonald's", time: "2019-02-13T11:00:01.000Z"
    Then Create transaction successfully
    And Transaction amount: 20, merchant: "Burger King", time: "2019-02-13T11:00:02.000Z"
    Then Create transaction successfully
    And Transaction amount: 5, merchant: "Burger King", time: "2019-02-13T11:00:07.000Z"
    Then Create transaction successfully
    And Transaction amount: 5, merchant: "Burger King", time: "2019-02-13T11:00:08.000Z"
    Then Return multiple violations on create transaction: "high-frequency-small-interval","doubled-transaction"
    And Transaction amount: 150, merchant: "Burger King", time: "2019-02-13T11:00:18.000Z"
    Then Return multiple violations on create transaction: "insufficient-limit","high-frequency-small-interval"
    And Transaction amount: 15, merchant: "Burger King", time: "2019-02-13T12:00:27.000Z"
    Then Create transaction successfully






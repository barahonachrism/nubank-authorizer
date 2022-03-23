Feature: Integration tests collection to manage transactions of a Nubank credit card
  Scenario: Processing a transaction successfully
    Given File input "transactions_successfully.request" file
    When Process request
    Then Output response is equal to "transactions_successfully.response" file
  Scenario: Create account already initialized
    Given File input "transactions_account_already_initialized.request" file
    When Process request
    Then Output response is equal to "transactions_account_already_initialized.response" file
  Scenario: Processing a transaction which violates the account-not-initialized logic
    Given File input "transactions_account_not_initialized.request" file
    When Process request
    Then Output response is equal to "transactions_account_not_initialized.response" file
  Scenario: Processing a transaction which violates card-not-active logic
    Given File input "transactions_card_not_active.request" file
    When Process request
    Then Output response is equal to "transactions_card_not_active.response" file
  Scenario: Processing a transaction which violates insufficient-limit logic
    Given File input "transactions_insufficient_limit.request" file
    When Process request
    Then Output response is equal to "transactions_insufficient_limit.response" file
  Scenario: Processing a transaction which violates the high-frequency-small-interval logic
    Given File input "transaction_high_frequency_small_interval.request" file
    When Process request
    Then Output response is equal to "transaction_high_frequency_small_interval.response" file
  Scenario: Processing a transaction which violates the doubled-transaction logic
    Given File input "transactions_doubled_transaction.request" file
    When Process request
    Then Output response is equal to "transactions_doubled_transaction.response" file
  Scenario: Processing transactions that violate multiple logics
    Given File input "transactions_violate_multiple_logics.request" file
    When Process request
    Then Output response is equal to "transactions_violate_multiple_logics.response" file
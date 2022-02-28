package com.zinkworks.atm.constants;

public enum ErrorMessages {

    INVALID_ACCOUNT("No account found for given account number"),
    AMOUNT_NOT_AVAILABLE_ACCOUNT("The requested amount is not available in the account"),
    SMALLER_THAN_5("The request can't be completed since this ATM doesn't hold bills smaller than €5"),
    ATM_OUT_OF_BILLS("This ATM is currently depleted of bills"),
    NOT_ENOUGH_BILLS_AVAILABLE("The requested amount can't be dispensed by this ATM"),
    NEGATIVE_AMOUNT("Amount requested must be positive");

    private String description;

    ErrorMessages(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

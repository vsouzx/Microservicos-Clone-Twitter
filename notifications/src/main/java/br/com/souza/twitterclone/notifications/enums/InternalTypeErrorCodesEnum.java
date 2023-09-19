package br.com.souza.twitterclone.notifications.enums;

public enum InternalTypeErrorCodesEnum {

    E500000("500.000", "Error while calling another microservice."),
    E420001("420.001", "This tweet doesn't exists.");

    private final String code;
    private final String message;

    InternalTypeErrorCodesEnum(String code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getValue() {
        return this.name();
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("Fault code: %s = %s.", getMessage());
    }
}

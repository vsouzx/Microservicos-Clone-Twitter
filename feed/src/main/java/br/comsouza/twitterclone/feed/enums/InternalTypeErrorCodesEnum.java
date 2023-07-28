package br.comsouza.twitterclone.feed.enums;

public enum InternalTypeErrorCodesEnum {

    E500000("500.000", "Internal error without mapped cause."),
    E420000("420.000", "Message or attachment need to have value."),
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

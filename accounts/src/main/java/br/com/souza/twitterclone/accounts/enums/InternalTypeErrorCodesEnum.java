package br.com.souza.twitterclone.accounts.enums;

public enum InternalTypeErrorCodesEnum {

    E500000("500.000", "Internal error without mapped cause."),
    E410001("410.000", "User not found.");

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

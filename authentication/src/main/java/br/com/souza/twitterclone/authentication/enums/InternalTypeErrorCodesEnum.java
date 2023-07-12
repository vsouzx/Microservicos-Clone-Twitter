package br.com.souza.twitterclone.authentication.enums;

public enum InternalTypeErrorCodesEnum {

    E500000("500.000", "Internal error without mapped cause."),
    E400001("400.001", "Invalid credentials."),
    E400002("400.002","Your email isn´t confirmed yet. Please, check the security code sent to your email."),
    E400003("400.003","Invalid confirmation code."),
    E400004("400.004","Your email is already confirmed.");

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

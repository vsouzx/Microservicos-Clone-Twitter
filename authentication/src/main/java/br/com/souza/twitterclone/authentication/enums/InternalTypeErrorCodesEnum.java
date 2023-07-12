package br.com.souza.twitterclone.authentication.enums;

public enum InternalTypeErrorCodesEnum {

    E500000("Internal error without mapped cause."),
    E400001("Invalid credentials."),
    E400002("Your email isn´t confirmed yet. Please, check the security code sent to your email."),
    E400003("Invalid confirmation code."),
    E400004("Your email is already confirmed.");

    private final String message;

    InternalTypeErrorCodesEnum( String message) {
        this.message = message;
    }

    public String getValue() {
        return this.name();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("Fault code: %s = %s.", getMessage());
    }
}

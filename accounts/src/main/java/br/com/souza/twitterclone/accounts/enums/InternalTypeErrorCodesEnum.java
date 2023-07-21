package br.com.souza.twitterclone.accounts.enums;

public enum InternalTypeErrorCodesEnum {

    E500000("500.000", "Internal error without mapped cause."),
    E410000("410.000", "User not found."),
    E410001("410.001", "Username already exists."),
    E410002("410.002", "Email already exists."),
    E410003("410.003", "Please write actual password."),
    E410004("410.004", "Password does not matches with your actual password."),
    E410005("410.005", "Please write new password confirmation."),
    E410006("410.006", "New password confirmation does not matches with your new password."),
    E410007("410.007", "Username can only contains letters, numbers and underscore"),
    E410008("410.008", "Your password must have at least 8 characters"),
    E410009("410.009", "%s has blocked you."),
    E410010("410.010", "You can't follow this user, because he/she has blocked you OR you have blocked him/her."),
    E410011("410.011", "There is not an pending follow request from this user to you."),
    E410012("410.012", "You can't silence this user, because he/she has blocked you OR you have blocked him/her.");

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

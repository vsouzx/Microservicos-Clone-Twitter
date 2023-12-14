package br.comsouza.twitterclone.feed.enums;

public enum InternalTypeErrorCodesEnum {

    E500000("500.000", "Error while calling another microservice."),
    E420000("420.000", "Message or attachment need to have value."),
    E420001("420.001", "This tweet doesn't exists."),
    E420002("420.002", "You are not able to retweet this tweet."),
    E420003("420.003", "You are not able to reply this tweet."),
    E420004("420.004", "You are not able to like this tweet."),
    E420005("420.005", "You cannot send more than 4 attachments."),
    E410016("401.000", "Unauthorized request.");


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

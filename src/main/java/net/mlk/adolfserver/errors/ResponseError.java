package net.mlk.adolfserver.errors;

import net.mlk.jmson.Json;

public class ResponseError {
    private final Json errorMessage = new Json();

    public ResponseError(String errorMessage) {
        this(errorMessage, new Json());
    }

    public ResponseError(String errorMessage, Json data) {
        this.errorMessage.append("type", "error")
                .append("message", errorMessage)
                .putAll(data);
    }

    public Json getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public String toString() {
        return this.errorMessage.toString();
    }

}

package org.ripoll.exception;

public record ApiError(int status, String message) {
}

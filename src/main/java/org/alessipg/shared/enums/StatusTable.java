package org.alessipg.shared.enums;

import lombok.Getter;

@Getter
public enum StatusTable {
    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    ALREADY_EXISTS(409, "Conflict - Already Exists"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    IM_TEAPOT(418, "I'm a teapot"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;
    StatusTable(int code, String message) {
        this.code = code;
        this.message = message;
    }
    

}

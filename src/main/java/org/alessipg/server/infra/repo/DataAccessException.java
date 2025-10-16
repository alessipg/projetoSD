package org.alessipg.server.infra.repo;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) { super(message, cause); }
}
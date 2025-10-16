package org.alessipg.shared.util;

import org.alessipg.shared.enums.StatusTable;

public sealed interface Result<T> permits Result.Success, Result.Failure {

    record Success<T>(T data) implements Result<T> { }

    record Failure<T>(StatusTable code, String message, boolean transport) implements Result<T> { }
}



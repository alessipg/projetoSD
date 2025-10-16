package org.alessipg.client.util;

import com.google.gson.JsonObject;
import org.alessipg.shared.enums.StatusTable;
import org.alessipg.shared.util.Result;
import java.util.function.Function;

public final class StatusMapper {

    private StatusMapper() { }

    public static <T> Result<T> map(JsonObject jsonObject,
                             Function<JsonObject, T> successAdapter,
                             Function<StatusTable, String> errorMessageProvider) {
        String statusStr = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";

        switch (statusStr) {
            case "200":
            case "201": {
                T data = successAdapter.apply(jsonObject);
                return new Result.Success<>(data);
            }
            case "400":
                return failure(StatusTable.BAD, errorMessageProvider.apply(StatusTable.BAD));
            case "401":
                return failure(StatusTable.UNAUTHORIZED, errorMessageProvider.apply(StatusTable.UNAUTHORIZED));
            case "403":
                return failure(StatusTable.FORBIDDEN, errorMessageProvider.apply(StatusTable.FORBIDDEN));
            case "404":
                return failure(StatusTable.NOT_FOUND, errorMessageProvider.apply(StatusTable.NOT_FOUND));
            case "409":
                return failure(StatusTable.ALREADY_EXISTS, errorMessageProvider.apply(StatusTable.ALREADY_EXISTS));
            case "422":
                return failure(StatusTable.UNPROCESSABLE_ENTITY, errorMessageProvider.apply(StatusTable.UNPROCESSABLE_ENTITY));
            default:
                return failure(StatusTable.INTERNAL_SERVER_ERROR, errorMessageProvider.apply(StatusTable.INTERNAL_SERVER_ERROR));
        }
    }

    private static <T> Result<T> failure(StatusTable code, String message) {
        return new Result.Failure<>(code, message, false);
    }
}

package org.alessipg.shared.dto.response;

import org.alessipg.shared.dto.util.UserView;
import org.alessipg.shared.enums.StatusTable;

import java.util.List;

public record UserGetAllResponse(String status, String mensagem, List<UserView> usuarios) {
    public UserGetAllResponse(StatusTable status, String mensagem, List<UserView> usuarios) {
        this(String.valueOf(status.getStatus()), mensagem, usuarios);
    }
}

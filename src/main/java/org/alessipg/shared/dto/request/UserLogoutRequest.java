package org.alessipg.shared.dto.request;

public record UserLogoutRequest(String operacao, String token) {
    public UserLogoutRequest(String token){
        this("LOGOUT",token);
    }
}

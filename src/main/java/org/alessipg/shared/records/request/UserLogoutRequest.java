package org.alessipg.shared.records.request;

public record UserLogoutRequest(String operacao, String token) {
    public UserLogoutRequest(String token){
        this("LOGOUT",token);
    }
}

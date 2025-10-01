package org.alessipg.shared.records;

public record UserLogoutRequest(String operacao, String token) {
    public UserLogoutRequest(String token){
        this("LOGOUT",token);
    }
}

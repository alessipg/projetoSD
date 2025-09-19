package org.alessipg.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {
    private static final String SECRET = "supersecretkey";

    public static String gerarToken(int id, String usuario, String funcao) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
            .withClaim("id", id)
            .withClaim("usuario", usuario)
            .withClaim("funcao", funcao)
            .sign(algorithm);
    }

    public static DecodedJWT validarToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
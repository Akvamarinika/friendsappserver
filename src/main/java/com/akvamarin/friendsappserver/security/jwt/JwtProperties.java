package com.akvamarin.friendsappserver.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.security.Key;

@ConstructorBinding
@ConfigurationProperties("jwt.token.properties") // значения присвоятся из конфига
@Validated // для проверки на @NotNull
@Getter
@RequiredArgsConstructor
public class JwtProperties {
    public static final String BEARER = "Bearer ";
    public static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    @NotNull
    private final String secretKey; //Токен подписывается секретным ключом

    private final long tokenExpireDurationSec; // Время годности токена

    @NotNull
    private final String issuer; //наименование стороны, которая «создала» токен и подписала его своим закрытым ключом

    public Key getSecretKeyDecode() {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return key;
    }

}

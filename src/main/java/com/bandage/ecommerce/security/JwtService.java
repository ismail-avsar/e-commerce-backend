package com.bandage.ecommerce.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final byte[] secret;
    private final long expirationMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(String email) {
        long now = Instant.now().getEpochSecond();
        String header = json(Map.of("alg", "HS256", "typ", "JWT"));
        String payload = json(Map.of(
                "sub", email,
                "iat", now,
                "exp", now + expirationMinutes * 60
        ));
        String unsignedToken = base64Url(header) + "." + base64Url(payload);
        return unsignedToken + "." + sign(unsignedToken);
    }

    public String getEmail(String token) {
        return payload(token).get("sub").toString();
    }

    public boolean isValid(String token) {
        try {
            String[] parts = parts(token);
            String expectedSignature = sign(parts[0] + "." + parts[1]);
            Number expiration = (Number) payload(token).get("exp");
            return expectedSignature.equals(parts[2]) && expiration.longValue() > Instant.now().getEpochSecond();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    private Map<String, Object> payload(String token) {
        try {
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts(token)[1]), StandardCharsets.UTF_8);
            return objectMapper.readValue(payloadJson, new TypeReference<>() {
            });
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid token.", exception);
        }
    }

    private String[] parts(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token.");
        }
        return parts;
    }

    private String json(Map<String, Object> value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception exception) {
            throw new IllegalStateException("Token could not be created.", exception);
        }
    }

    private String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Token could not be signed.", exception);
        }
    }
}

package project.service;

import project.config.CurrentUser;
import project.entity.User;
import project.entity.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
public class JwtService
{
    @Value("${app.jwt-secret}")
    private String secret;
    @Value("${app.jwt-expire-hours}")
    private long expireHours;
    public String create(User user) {
        long exp = Instant.now().plusSeconds(expireHours * 3600).getEpochSecond();
        String payload = user.getId() + ":" + user.getRole() + ":" + user.getAccount() + ":" + exp;
        String body = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        return body + "." + sign(body);
    }

    public Optional<CurrentUser> parse(String token) {
        try {
            String[] parts = token == null ? new String[0] : token.replace("Bearer ", "").split("\\.");
            if (parts.length != 2 || !sign(parts[0]).equals(parts[1])) {
                return Optional.empty();
            }
            String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
            String[] fields = payload.split(":", 4);
            long exp = Long.parseLong(fields[3]);
            if (Instant.now().getEpochSecond() > exp) {
                return Optional.empty();
            }
            return Optional.of(new CurrentUser(Long.parseLong(fields[0]), UserRole.valueOf(fields[1]), fields[2]));
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    private String sign(String body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(body.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("JWT签名失败", ex);
        }
    }
}

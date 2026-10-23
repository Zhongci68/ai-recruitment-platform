package project.service;

import project.entity.User;
import project.entity.UserRole;
import project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public User register(Map<String, String> body) {
        String account = body.getOrDefault("account", "").trim();
        String password = body.getOrDefault("password", "");
        String confirm = body.getOrDefault("confirmPassword", password);
        if (account.isBlank() || password.length() < 4) {
            throw new IllegalArgumentException("账号不能为空，密码至少4位");
        }
        if (!password.equals(confirm)) {
            throw new IllegalArgumentException("两次密码不一致");
        }
        if (userRepository.existsByAccount(account)) {
            throw new IllegalArgumentException("该账号已注册");
        }
        User user = new User();
        user.setAccount(account);
        user.setName(body.getOrDefault("name", account));
        user.setPhone(body.get("phone"));
        user.setPassword(hash(password));
        user.setRole(UserRole.valueOf(body.getOrDefault("role", "CANDIDATE")));
        return userRepository.save(user);
    }

    public Map<String, Object> login(String account, String password) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("账号或密码错误"));
        if (!user.getPassword().equals(hash(password))) {
            throw new IllegalArgumentException("账号或密码错误");
        }
        if (!"启用".equals(user.getStatus())) {
            throw new IllegalArgumentException("账号已被停用");
        }
        return Map.of("token", jwtService.create(user), "user", user);
    }

    public String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(text.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("密码加密失败", ex);
        }
    }
}

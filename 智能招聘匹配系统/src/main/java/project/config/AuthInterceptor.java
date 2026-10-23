package project.config;

import project.entity.UserRole;
import project.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor
{
    private final JwtService jwtService;
    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }
        RequireRole required = method.getMethodAnnotation(RequireRole.class);
        if (required == null) {
            required = method.getBeanType().getAnnotation(RequireRole.class);
        }
        String token = request.getHeader("Authorization");
        jwtService.parse(token).ifPresent(AuthContext::set);
        if (required == null) {
            return true;
        }
        CurrentUser current = AuthContext.get();
        if (current == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
            return false;
        }
        UserRole role = current.role();
        boolean allowed = Arrays.asList(required.value()).contains(role);
        if (!allowed) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "没有权限访问该功能");
        }
        return allowed;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }
}

package project.config;

public final class AuthContext {
    private static final ThreadLocal<CurrentUser> CURRENT = new ThreadLocal<>();
    private AuthContext() {
    }
    public static void set(CurrentUser user) {
        CURRENT.set(user);
    }

    public static CurrentUser get() {
        return CURRENT.get();
    }

    public static Long userId() {
        CurrentUser user = CURRENT.get();
        return user == null ? null : user.id();
    }

    public static void clear() {
        CURRENT.remove();
    }
}

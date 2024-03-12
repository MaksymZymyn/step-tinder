package utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class Auth {

    private static String cookieName;

    public Auth(String cookieName) {
        this.cookieName = cookieName;
    }

    private static final Cookie[] empty = new Cookie[]{};

    private static Cookie[] ensureNotNull(Cookie[] cs) {
        return cs == null ? empty : cs;
    }

    public static Optional<String> getCookieValue(HttpServletRequest rq) {
        return Arrays.stream(ensureNotNull(rq.getCookies()))
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue);
    }

    public static void setCookieValue(String cookieValue, HttpServletResponse rs) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        rs.addCookie(cookie);
    }

    public static void clearCookie(HttpServletResponse rs) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        rs.addCookie(cookie);
    }

    public static void renderUnregistered(HttpServletResponse resp) {
        resp.setStatus(401);
    }
}

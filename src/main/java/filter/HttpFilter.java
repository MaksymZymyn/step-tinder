package filter;

import utils.Auth;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpFilter implements Filter {

  public interface MyCheckFunction {
    boolean check(HttpServletRequest rq);
  }

  public interface MyUnauthorizedHandler {
    void handle(HttpServletResponse rs) throws IOException;
  }

  private final MyCheckFunction checkFn;
  private final MyUnauthorizedHandler onUnauthorized;

  public HttpFilter(MyCheckFunction checkFn, MyUnauthorizedHandler onUnauthorized) {
    this.checkFn = checkFn;
    this.onUnauthorized = onUnauthorized;
  }

  public HttpFilter(MyCheckFunction checkFn) {
    this(
      checkFn,
      rs -> rs.sendRedirect("/login")
    );
  }

  public HttpFilter() {
    this(
      rq -> Auth.getCookieValue(rq).isPresent()
    );
  }

  @Override
  public void init(FilterConfig filterConfig) {

  }

  private boolean isHttp(ServletRequest rq0, ServletResponse rs0) {
    return rq0 instanceof HttpServletRequest && rs0 instanceof HttpServletResponse;
  }

  @Override
  public void doFilter(ServletRequest rq0, ServletResponse rs0, FilterChain chain) throws IOException, ServletException {
    if (isHttp(rq0, rs0)) {
      var rq = (HttpServletRequest) rq0;
      var rs = (HttpServletResponse) rs0;
      if (checkFn.check(rq)) chain.doFilter(rq, rs);
      else onUnauthorized.handle(rs);
    } else chain.doFilter(rq0, rs0);
  }

  @Override
  public void destroy() {

  }
}

package web.filter;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String LOGIN_PAGE_URL = "login";
    private static final String CSS_JSP_PNG_GIF_JS_INPUT_REGEX = ".*(css|jpg|png|gif|js|jspf)";
    private static final String USER_ATTRIBUTE_NAME = "user";

    private String loginExcludePattern;
    private String registrationExcludePattern;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.loginExcludePattern = filterConfig.getInitParameter("loginExcludePattern");
        this.registrationExcludePattern = filterConfig.getInitParameter("registrationExcludePattern");
        LOGGER.debug("LoginFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        if(isValidUrl(servletRequest)){
            chain.doFilter(request, response);
        } else {
            LOGGER.debug("No user logged in.");
            servletResponse.sendRedirect(LOGIN_PAGE_URL);
        }
    }

    @Override
    public void destroy() {
        LOGGER.debug("LoginFilter destroyed.");
    }

    private boolean isValidUrl(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        String requestURL = servletRequest.getRequestURL().toString();
        return isValidURL(requestURL) || session.getAttribute(USER_ATTRIBUTE_NAME) != null;
    }

    private boolean isValidURL(String requestURL) {
        return requestURL.contains(loginExcludePattern) ||
                requestURL.contains(registrationExcludePattern) ||
                requestURL.matches(CSS_JSP_PNG_GIF_JS_INPUT_REGEX);
    }
}

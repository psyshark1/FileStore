package servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;
import java.util.List;

public class UserRoleRequestWrapper extends HttpServletRequestWrapper {

    private String user;
    private List<String> roles;
    private HttpServletRequest realRequest;

    public UserRoleRequestWrapper(String user, List<String> roles, HttpServletRequest request) {
        super(request);
        this.user = user;
        this.roles = roles;
        this.realRequest = request;
    }

    public boolean isUserInRole(String role) {
        if (role == null) return this.realRequest.isUserInRole(role);
        return this.roles.contains(role.toLowerCase());
    }

    public Principal getUserPrincipal() {
        if (this.user == null) {
            return realRequest.getUserPrincipal();
        }
        return new Principal() {
            public String getName() {
                return user;
            }
        };
    }
}

package sample.studentmanagement;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.GenericFilterBean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterAfter(new AuthenticationFilter(), AnonymousAuthenticationFilter.class);
        http.authorizeRequests().anyRequest().authenticated();
    }
}

class AuthenticationFilter extends GenericFilterBean implements InitializingBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authorizationHeader = ((HttpServletRequest) request).getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Hash = authorizationHeader.replace("Basic ", "");
            String userpassword = new String(Base64Utils.decode(base64Hash.getBytes()));
            String[] split = userpassword.split(":");
            String username = split[0], password = split[1];
            Authentication auth = new PreAuthenticatedAuthenticationToken(username, userpassword);
            auth.setAuthenticated(username.equalsIgnoreCase(password));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}

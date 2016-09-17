package sample.studentmanagement;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
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
    
    @Autowired
    private AuthenticationFilter authenticationFilter;
    
    @Autowired
    private AuthClearFilter authClearFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.addFilterBefore(authClearFilter, AnonymousAuthenticationFilter.class);
        http.addFilterAfter(authenticationFilter, AnonymousAuthenticationFilter.class);
        http.authorizeRequests().anyRequest().hasAnyAuthority("USER");
    }
}

@Component
class AuthClearFilter extends GenericFilterBean implements InitializingBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(null);
        chain.doFilter(request, response);
    }
}

@Component
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
            Authentication auth = new PreAuthenticatedAuthenticationToken(username, userpassword, Arrays.asList(new SimpleGrantedAuthority("USER")));
            auth.setAuthenticated(username.equalsIgnoreCase(password));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}

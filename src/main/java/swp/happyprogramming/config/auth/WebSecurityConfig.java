package swp.happyprogramming.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import swp.happyprogramming.sercurity.SimpleAuthenticationSuccessHandler;
import swp.happyprogramming.services.IUserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private SimpleAuthenticationSuccessHandler successHandler;

    private IUserService userService;

    @Autowired
    public WebSecurityConfig(@Lazy IUserService userService, @Lazy SimpleAuthenticationSuccessHandler successHandler) {
        super();
        this.successHandler = successHandler;
        this.userService = userService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/home").permitAll()
                    .antMatchers("/admin**").hasAnyAuthority("ROME_ADMIN")
                    .antMatchers("/upload/static/**").permitAll()
                    .antMatchers("/").hasAnyAuthority("ROLE_MENTEE", "ROLE_MENTOR")
                    .antMatchers("/signup", "/login", "/*").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login").loginProcessingUrl("/login")
                    .usernameParameter("username").passwordParameter("password")
                    .successHandler(successHandler)
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                    .permitAll()
                    .and()
                .exceptionHandling()
                .accessDeniedPage("/login")
        ;
    }
}
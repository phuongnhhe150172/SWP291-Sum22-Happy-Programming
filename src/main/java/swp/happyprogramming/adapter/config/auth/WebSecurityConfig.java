package swp.happyprogramming.adapter.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import swp.happyprogramming.adapter.sercurity.SimpleAuthenticationSuccessHandler;
import swp.happyprogramming.application.port.usecase.IUserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

  private SimpleAuthenticationSuccessHandler successHandler;

  private IUserService userService;

  @Autowired
  public WebSecurityConfig(@Lazy IUserService userService,
    @Lazy SimpleAuthenticationSuccessHandler successHandler) {
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

  //@Override
  //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //  auth.authenticationProvider(authenticationProvider());
  //}

  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  //@Override
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeHttpRequests()
      .requestMatchers("/home").permitAll()
      .requestMatchers("/admin**").hasAnyAuthority("ROME_ADMIN")
      .requestMatchers("/upload/static/**").permitAll()
      .requestMatchers("/").hasAnyAuthority("ROLE_MENTEE", "ROLE_MENTOR")
      .requestMatchers("/signup", "/login", "/*").permitAll()
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
      .accessDeniedPage("/login");
    return http.build();
  }
}
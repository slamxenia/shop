package com.wjz.shop.security.core.config;

import com.wjz.shop.security.handler.CustomAccessDeniedHandler;
import com.wjz.shop.security.handler.CustomAuthenticationFailureHandler;
import com.wjz.shop.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.sql.DataSource;
import java.beans.BeanProperty;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//              .addFilterBefore()

                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)

                .and()
                .rememberMe()
                .key("rem-me-key")
                .rememberMeCookieName("remember-me-cookie")
                .rememberMeParameter("remember-me")  // remember-me field name in form.
                .tokenRepository(this.persistentTokenRepository())
                .tokenValiditySeconds(1*24*60*60)

                .and()
                .exceptionHandling()
//              .authenticationEntryPoint()
                .accessDeniedHandler(accessDeniedHandler)

//              .and()
//              .headers()
//              .frameOptions()
//              .sameOrigin()

                .and()
                .authorizeRequests()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/admin/**").hasAnyAuthority("admin")
                .antMatchers("/user/**").hasAnyAuthority("user")
                .anyRequest().authenticated()

//                .and()
//                .apply()

                .and()
                .logout().logoutUrl("/logout")
                .deleteCookies("JSESSIONID")

                .and()
                .sessionManagement()
                .invalidSessionUrl("/session/invalid")
//              .expiredSessionStrategy()
                .maximumSessions(2)
                ;
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }

    @Bean
    public PersistentTokenRepository persistentTokenInMemoryRepository() {
        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
        return memory;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

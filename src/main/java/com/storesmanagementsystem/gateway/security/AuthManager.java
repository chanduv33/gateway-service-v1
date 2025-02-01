package com.storesmanagementsystem.gateway.security;

import com.storesmanagementsystem.gateway.contracts.UserInfoBean;
import com.storesmanagementsystem.gateway.service.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthManager implements ReactiveAuthenticationManager {

    @Autowired
    ReactiveUserDetailsService reactiveUserDetailsService;

    @Autowired
    private UserService userDao;

    @Autowired
    private Environment environment;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class).flatMap(auth -> {
                    String userName = getUserName(auth.getCredentials());
                    return reactiveUserDetailsService.findByUsername(userName).defaultIfEmpty(new UserDetails() {
                        @Override
                        public Collection<? extends GrantedAuthority> getAuthorities() {
                            return null;
                        }

                        @Override
                        public String getPassword() {
                            return null;
                        }

                        @Override
                        public String getUsername() {
                            return null;
                        }

                        @Override
                        public boolean isAccountNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isAccountNonLocked() {
                            return false;
                        }

                        @Override
                        public boolean isCredentialsNonExpired() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled() {
                            return false;
                        }
                    }).flatMap(
                            user -> {
                                if (user.getUsername() == null) {
                                    Mono.error(new IllegalArgumentException("User Not found"));
                                }
                                if (validateToken(auth.getPrincipal())) {
                                    return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));
                                }
                                Mono.error(new IllegalArgumentException("Invalid Token"));
                                return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()));
                            }
                    );
                });
    }

    private String getUserName(String token) {
        String userId = null;
        try {
            userId = Jwts.parser().setSigningKey(environment.getProperty("auth.header.token")).parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (Exception e) {
            throw new AuthenticationException("Invalid Token") {
            };
        }
        if (userId == null) {
            return null;
        }
        UserInfoBean user = userDao.getUserByUserId(Integer.parseInt(userId));
        SimpleGrantedAuthority authority1 = new SimpleGrantedAuthority(user.getRole());

        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(authority1);
        return user.getUsername();
    }

    private boolean validateToken(String token) {
        String userId = Jwts.parser().setSigningKey(environment.getProperty("auth.header.token")).parseClaimsJws(token)
                .getBody().getSubject();
        if (userId == null) {
            return false;
        }
        UserInfoBean user = userDao.getUserByUserId(Integer.parseInt(userId));
        if (user != null) {
            return true;
        }
        return false;
    }
}

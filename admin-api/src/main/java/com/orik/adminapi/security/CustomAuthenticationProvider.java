package com.orik.adminapi.security;


import com.orik.adminapi.exception.CustomAuthenticationException;
import com.orik.adminapi.exception.NoUserFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private CustomUserDetailsService userDetailsService;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws CustomAuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();


        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            checkPassword(password,userDetails);
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } catch (NoUserFoundException e) {
            throw new CustomAuthenticationException("Authentication failed: " + e.getMessage());
        }
    }
    private void checkPassword(String password,UserDetails userDetails){
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new CustomAuthenticationException("Authentication failed: Invalid password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

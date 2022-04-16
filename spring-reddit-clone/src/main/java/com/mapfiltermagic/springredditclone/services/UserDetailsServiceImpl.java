package com.mapfiltermagic.springredditclone.services;

import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.Optional;

import com.mapfiltermagic.springredditclone.models.User;
import com.mapfiltermagic.springredditclone.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "No user exists with the username";

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
            .orElseThrow(() -> {
                log.error(USER_NOT_FOUND_MSG + " {}", username);

                throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG + " " + username);
            }
        );

        return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true,
                true, getAuthorities("USER"));
    }

    /**
     *
     * @param role
     * @return
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
    

}

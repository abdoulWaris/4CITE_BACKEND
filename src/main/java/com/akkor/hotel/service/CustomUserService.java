package com.akkor.hotel.service;

import com.akkor.hotel.model.User;
import com.akkor.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.akkor.hotel.model.User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        if(user == null){
            throw new UsernameNotFoundException("User not found with email :"+username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRoles().toString())));
    }
}

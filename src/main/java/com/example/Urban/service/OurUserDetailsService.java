package com.example.Urban.service;


import com.example.Urban.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OurUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository usersRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepo.findByUsername(username).orElseThrow();
    }
}
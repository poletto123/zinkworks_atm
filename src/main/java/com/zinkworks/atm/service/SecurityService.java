package com.zinkworks.atm.service;

import com.zinkworks.atm.model.Account;
import com.zinkworks.atm.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findById(accountNumber);
        if (!account.isPresent()) {
            throw new UsernameNotFoundException("No account found for given account number");
        }
        return account.get();
    }
}

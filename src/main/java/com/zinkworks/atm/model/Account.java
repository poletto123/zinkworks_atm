package com.zinkworks.atm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@ApiModel
@JsonIgnoreProperties({"pin", "enabled", "password", "username", "authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
public class Account implements UserDetails {

    @Id
    @ApiModelProperty(value = "account number", example = "123456789")
    private String accountNumber;

    @ApiModelProperty(value = "account pin", example = "1234")
    private String pin;

    @ApiModelProperty(value = "account balance, can be negative if overdraft is used", example = "800")
    private BigDecimal balance;

    @ApiModelProperty(value = "overdraft limit, allows for account to be negative up to limit", example = "200")
    private BigDecimal overdraft;

    public Account(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account(String accountNumber, BigDecimal balance, BigDecimal overdraft) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.overdraft = overdraft;
    }

    public Account() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.overdraft = overdraft;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public String getPassword() {
        return pin;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public String getUsername() {
        return accountNumber;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @ApiModelProperty(hidden = true)
    public boolean isEnabled() {
        return true;
    }
}

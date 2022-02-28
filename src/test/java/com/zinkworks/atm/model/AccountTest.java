package com.zinkworks.atm.model;

import com.zinkworks.atm.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class AccountTest {

    private Account account;

    @Before
    public void setup() {
        account = new Account();
        account.setAccountNumber("1234");
        account.setBalance(new BigDecimal("200"));
        account.setPin("1234");
        account.setOverdraft(new BigDecimal("200"));
    }

    @Test
    public void testGettersSetters() {
        Assert.assertEquals("1234", account.getAccountNumber());
        Assert.assertEquals(new BigDecimal("200"), account.getBalance());
        Assert.assertEquals("1234", account.getPin());
        Assert.assertEquals(new BigDecimal("200"), account.getOverdraft());
        Assert.assertNull(account.getAuthorities());
        Assert.assertEquals("1234", account.getPassword());
        Assert.assertEquals("1234", account.getUsername());
        Assert.assertTrue(account.isAccountNonExpired());
        Assert.assertTrue(account.isAccountNonLocked());
        Assert.assertTrue(account.isEnabled());
        Assert.assertTrue(account.isCredentialsNonExpired());
    }
}

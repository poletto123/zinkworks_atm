package com.zinkworks.atm.controller;

import com.zinkworks.atm.constants.ErrorMessages;
import com.zinkworks.atm.model.Account;
import com.zinkworks.atm.model.Bill;
import com.zinkworks.atm.service.AccountService;
import com.zinkworks.atm.service.BillService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private BillService billService;

    @InjectMocks
    private AccountController accountController;

    @Before
    public void setup() {
        when(accountService.getAccountInfo("123456789")).thenReturn(new Account("123456789", new BigDecimal(800)));
    }

    @Test
    @WithMockUser("123456789")
    public void getAccountBalanceTestSuccess() throws Exception {
        mockMvc.perform(get("/api/account/balance")
                .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800));
    }

    @Test
    public void getAccountBalanceTestFailureNotSignedIn() throws Exception {
        mockMvc.perform(get("/api/account/balance")
                        .accept(MediaType.ALL))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser("123456789")
    public void withdrawTestSuccess() throws Exception {
        List<Bill> bills = billService.createEmptyBillList();
        bills.get(0).setQuantity(2);
        when(accountService.withdraw("123456789", 100, false)).thenReturn(bills);
        mockMvc.perform(get("/api/account/withdraw?amount=100&useOverdraft=false")
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.billType == 50)]['quantity']").value(2));
    }

    @Test
    public void withdrawTestFailureNotLoggedIn() throws Exception {
        mockMvc.perform(get("/api/account/withdraw?amount=100&useOverdraft=false")
                        .accept(MediaType.ALL))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser("123456789")
    public void withdrawTestFailureNoBalance() throws Exception {
        when(accountService.withdraw("123456789", 100, false))
                .thenThrow(new IllegalArgumentException(ErrorMessages.AMOUNT_NOT_AVAILABLE_ACCOUNT.getDescription()));
        mockMvc.perform(get("/api/account/withdraw?amount=100&useOverdraft=false")
                        .accept(MediaType.ALL))
                .andExpect(jsonPath("$").value(ErrorMessages.AMOUNT_NOT_AVAILABLE_ACCOUNT.getDescription()));
    }
}

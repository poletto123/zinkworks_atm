package com.zinkworks.atm.service;

import com.zinkworks.atm.constants.ErrorMessages;
import com.zinkworks.atm.model.Account;
import com.zinkworks.atm.model.Bill;
import com.zinkworks.atm.repository.AccountRepository;
import com.zinkworks.atm.repository.BillRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountBillServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService = Mockito.spy(new BillService());

    @InjectMocks
    private AccountService accountService;

    @Before
    public void setup() {
        List<Bill> billsFromDispenser = new ArrayList<>();
        billsFromDispenser.add(new Bill(50, 10));
        billsFromDispenser.add(new Bill(20, 30));
        billsFromDispenser.add(new Bill(10, 30));
        billsFromDispenser.add(new Bill(5, 20));
        when(billRepository.findAll()).thenReturn(billsFromDispenser);
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("1234", new BigDecimal(800))));
    }

    @Test
    public void getAccountInfoTestSuccess() {
        Account account = new Account("1234", new BigDecimal(800));
        assertEquals(account.getAccountNumber(), accountService.getAccountInfo("validAccount").getAccountNumber());
        assertEquals(account.getBalance(), accountService.getAccountInfo("validAccount").getBalance());
    }

    @Test
    public void getAccountInfoTestFailure() {
        when(accountRepository.findById("invalidAccount")).thenReturn(Optional.empty());
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.getAccountInfo("invalidAccount"));
        assertEquals(ErrorMessages.INVALID_ACCOUNT.getDescription(), exception.getMessage());
    }

    @Test
    public void updateAccountBalanceTest() {
        assertDoesNotThrow(() -> accountService.updateAccountBalance("validAccount", new BigDecimal("100")));
    }

    @Test
    public void withdrawTestFailureInvalidAccount() {
        when(accountRepository.findById("invalidAccount")).thenReturn(Optional.empty());
        when(billRepository.findAll()).thenReturn(new ArrayList<>());
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("invalidAccount", 100, true));
        assertEquals(ErrorMessages.INVALID_ACCOUNT.getDescription(), exception.getMessage());
    }

    @Test
    public void withdrawTestFailureNotEnoughFundsWithoutOverdraft() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("validAccount", 801, false));
        assertEquals(ErrorMessages.AMOUNT_NOT_AVAILABLE_ACCOUNT.getDescription(), exception.getMessage());
    }

    @Test
    public void withdrawTestFailureNotEnoughFundsWithOverdraft() {
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("1234", new BigDecimal(800), new BigDecimal(100))));
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("validAccount", 901, true));
        assertEquals(ErrorMessages.AMOUNT_NOT_AVAILABLE_ACCOUNT.getDescription(), exception.getMessage());
    }

    @Test
    public void withdrawTestFailureAmountNotMultipleOfFive() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("validAccount", 101, false));
        assertEquals(ErrorMessages.SMALLER_THAN_5.getDescription(), exception.getMessage());
    }

    @Test
    public void withdrawTestFailureNoBillsLeftInDispenser() {
        List<Bill> noBillsList = billService.createEmptyBillList();
        when(billRepository.findAll()).thenReturn(noBillsList);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("validAccount", 105, false));
        assertEquals(ErrorMessages.ATM_OUT_OF_BILLS.getDescription(), exception.getMessage());
    }

    @Test
    public void withdrawTestFailureAmountGreaterThanSumOfBills() {
        List<Bill> noBillsList = billService.createEmptyBillList();
        noBillsList.get(0).setQuantity(1);
        when(billRepository.findAll()).thenReturn(noBillsList);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("validAccount", 105, false));
        assertEquals(ErrorMessages.NOT_ENOUGH_BILLS_AVAILABLE.getDescription(), exception.getMessage());
    }

    @Test
    public void withdrawTestSuccessWithoutOverdraft() {
        List<Bill> billsToDispense = accountService.withdraw("validAccount", 100, false);
        assertEquals(billsToDispense.get(0).getQuantity(), 2);
    }

    @Test
    public void withdrawTestSuccessWithOverdraft() {
        when(accountRepository.findById("validAccount")).thenReturn(
                Optional.of(new Account("1234", new BigDecimal(0), new BigDecimal(100))));
        List<Bill> billsToDispense = accountService.withdraw("validAccount", 100, true);
        assertEquals(billsToDispense.get(0).getQuantity(), 2);
    }

    @Test
    public void withdrawTestFailureNegativeAmount() {
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("validAccount", -5, false));
        assertEquals(ErrorMessages.NEGATIVE_AMOUNT.getDescription(), exception.getMessage());
    }

}

package com.zinkworks.atm.service;

import com.zinkworks.atm.constants.ErrorMessages;
import com.zinkworks.atm.model.Account;
import com.zinkworks.atm.model.Bill;
import com.zinkworks.atm.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BillService billService;

    public Account getAccountInfo(String accountNumber) {
        Optional<Account> account = accountRepository.findById(accountNumber);
        if (!account.isPresent()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_ACCOUNT.getDescription());
        } else {
            return account.get();
        }
    }

    public void updateAccountBalance(String accountNumber, BigDecimal balance) {
        accountRepository.updateAccountBalance(accountNumber, balance);
    }

    public List<Bill> withdraw(String accountNumber, int amount, boolean useOverdraft) {
        List<Bill> billsForDelivery = billService.createEmptyBillList();
        List<Bill> billsFromDispenser = billService.getAllAvailableBills();
        Optional<Account> account = accountRepository.findById(accountNumber);
        validate(amount, account, billsFromDispenser, useOverdraft);
        tryToDoWithdraw(amount, billsFromDispenser, billsForDelivery);
        updateAccountBalance(accountNumber, account.get().getBalance().subtract(BigDecimal.valueOf(amount)));
        return billsForDelivery;
    }

    private void tryToDoWithdraw(int amount, List<Bill> billsFromDispenser, List<Bill> billsForDelivery) {
        checkForAvailabilityOfBills(amount, billsFromDispenser, billsForDelivery);
        updateDispenserBillCount(billsFromDispenser, billsForDelivery);
    }

    private void updateDispenserBillCount(List<Bill> billsFromDispenser, List<Bill> billsForDelivery) {
        for (int i = 0; i < billsFromDispenser.size(); i++) {
            billsFromDispenser.get(i).setQuantity(
                    billsFromDispenser.get(i).getQuantity() -
                    billsForDelivery.get(i).getQuantity()
            );
        }
    }

    private List<Bill> checkForAvailabilityOfBills(int amount, List<Bill> billsFromDispenser, List<Bill> billsForDelivery) {
        int tempAmount = amount;
        for (int i = 0; i < billsFromDispenser.size(); i++) {
            if (tempAmount == 0) {
                break;
            }

            Bill billFromDispenser = billsFromDispenser.get(i);
            int quantityAvailable = billFromDispenser.getQuantity();
            int amountForComparison = billFromDispenser.getBillType();
            while (tempAmount >= amountForComparison && quantityAvailable > 0) {
                Bill billToDeliver = billsForDelivery.get(i);
                billToDeliver.setQuantity(billToDeliver.getQuantity() + 1);
                quantityAvailable--;
                tempAmount -= amountForComparison;
            }
        }
        return billsForDelivery;
    }

    private void validate(int amount, Optional<Account> account, List<Bill> billsFromDispenser, boolean useOverdraft) {
        if (amount < 0) {
            throw new IllegalArgumentException(ErrorMessages.NEGATIVE_AMOUNT.getDescription());
        }
        if (!account.isPresent()) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_ACCOUNT.getDescription());
        }
        if (!verifyBalanceForWithdraw(account.get(), amount, useOverdraft)) {
            throw new IllegalArgumentException(ErrorMessages.AMOUNT_NOT_AVAILABLE_ACCOUNT.getDescription());
        }
        if (amount % 10 != 5 && amount % 10 != 0) {
            throw new IllegalArgumentException(ErrorMessages.SMALLER_THAN_5.getDescription());
        }
        int sum = 0;
        for (Bill bill : billsFromDispenser) {
            sum += bill.getQuantity() * bill.getBillType();
        }
        if (sum == 0) {
            throw new IllegalArgumentException(ErrorMessages.ATM_OUT_OF_BILLS.getDescription());
        }
        if (amount > sum) {
            throw new IllegalArgumentException(ErrorMessages.NOT_ENOUGH_BILLS_AVAILABLE.getDescription());
        }
    }

    private boolean verifyBalanceForWithdraw(Account account, int amount, boolean useOverdraft) {
        BigDecimal theoricalBalanceAfterWithdraw = account.getBalance().subtract(BigDecimal.valueOf(amount));
        if (useOverdraft) {
            if (theoricalBalanceAfterWithdraw.compareTo(account.getOverdraft().negate()) < 0) {
                return false;
            }
        } else {
            if (theoricalBalanceAfterWithdraw.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }
        return true;
    }

}

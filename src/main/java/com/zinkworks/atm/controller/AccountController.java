package com.zinkworks.atm.controller;

import com.zinkworks.atm.model.Account;
import com.zinkworks.atm.model.Bill;
import com.zinkworks.atm.service.AccountService;
import com.zinkworks.atm.service.BillService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags= "Account Controller", description="API for interacting with accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BillService billService;

    @GetMapping(value = "/account/balance")
    @ApiOperation(value = "Get account balance for current logged in user", notes = "Returns the amount in BigDecimal." +
            "It uses the account number (variable 'name') that is fetched from current logged in user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "No account found for given account number")
    })
    public Account getBalance(Principal principal) {
        return accountService.getAccountInfo(principal.getName());
    }

    @ApiOperation(value = "Attempt withdraw from current logged in user account", notes = "Returns a list of bills that were dispensed. " +
            "It uses the account number (variable 'name') that is fetched from current logged in user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = ""),
            @ApiResponse(code = 400, message = "The requested amount is not available in the account || " +
                    "The request can't be completed since this ATM doesn't hold bills smaller than €5 || " +
                    "This ATM is currently depleted of bills || " +
                    "The requested amount can't be dispensed by this ATM")
    })
    @GetMapping(value = "/account/withdraw")
    public List<Bill> withdraw(
            Principal principal,
           @ApiParam(
                   name = "amount",
                   type = "int",
                   value = "amount to be withdrawn from account",
                   example = "800",
                   required = true
           )
           @RequestParam int amount,
           @ApiParam(
                   name = "useOverdraft",
                   type = "boolean",
                   value = "if true and withdraw goes below zero, limit will be overdraft amount set in account",
                   example = "true/false",
                   required = true
           )
           @RequestParam boolean useOverdraft) {
        return accountService.withdraw(principal.getName(), amount, useOverdraft);
    }

    @ApiOperation(value = "Get list of all bills in ATM ", notes = "Returns a list of the ATM bills for debugging purposes ")
    @GetMapping(value = "/getAvailableBills")
    public List<Bill> getAvailableBills() {
        return billService.getAllAvailableBills();
    }

}
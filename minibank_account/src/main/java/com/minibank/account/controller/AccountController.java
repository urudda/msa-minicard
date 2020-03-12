package com.minibank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minibank.account.domain.entity.Account;
import com.minibank.account.domain.entity.TransactionHistory;
import com.minibank.account.domain.entity.TransactionResult;
import com.minibank.account.service.AccountService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

	@Autowired
    private AccountService accountService;
    
    @ApiOperation(value = "계좌조회", httpMethod = "GET", notes = "계좌조회")
    @GetMapping("/rest/v0.8/{acntNo}")
    public Account retrieveAccount(@PathVariable(name = "acntNo") String acntNo) throws Exception{
        return accountService.retrieveAccount(acntNo);
    }

    @ApiOperation(value = "계좌등록", httpMethod = "POST", notes = "계좌등록")
    @PostMapping("/rest/v0.8")
    public Integer createAccount(@RequestBody Account account) throws Exception{
    	return accountService.createAccount(account);
    }

    @ApiOperation(value = "계좌목록조회", httpMethod = "GET", notes = "계좌목록조회")
    @GetMapping("/list/rest/v0.8/{cstmId}")
    public List<Account> retrieveAccountList(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        List<Account> accountList =  accountService.retrieveAccountList(cstmId);
        return accountList;
    }

    @ApiOperation(value = "계좌잔액조회", httpMethod = "GET", notes = "계좌잔액조회")
    @GetMapping("/balance/rest/v0.8/{acntNo}")
    public Long retrieveAccountBalance(@PathVariable(name = "acntNo") String acntNo) throws Exception{
        return accountService.retrieveAccountBalance(acntNo);
    }

    @ApiOperation(value = "입금", httpMethod = "POST", notes = "입금")
    @PostMapping("/deposit/rest/v0.8")
    public TransactionResult deposit(@RequestBody TransactionHistory input) throws Exception{
        return accountService.deposit(input);
    }

    @ApiOperation(value = "출금", httpMethod = "POST", notes = "출금")
    @PostMapping("/withdraw/rest/v0.8")
    public TransactionResult withdraw(@RequestBody TransactionHistory input) throws Exception{
        return  accountService.withdraw(input);
    }

    @ApiOperation(value = "출금취소", httpMethod = "POST", notes = "출금취소")
    @PostMapping("/withdraw/cancel/rest/v0.8")
    public Integer cancelWithdraw(@RequestBody TransactionHistory input) throws Exception{
        return  accountService.cancelWithdraw(input);
    }
    
    @ApiOperation(value = "거래내역조회", httpMethod = "GET", notes = "거래내역조회")
    @GetMapping("/transaction-history/rest/v0.8/{acntNo}")
    public List<TransactionHistory> retrieveTransactionHistory(@PathVariable(name = "acntNo") String acntNo) throws Exception{
        List<TransactionHistory> transactionHistory = accountService.retrieveTransactionHistoryList(acntNo);
        return transactionHistory;
    }
}

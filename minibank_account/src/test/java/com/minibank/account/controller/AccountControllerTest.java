package com.minibank.account.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.minibank.account.domain.entity.Account;
import com.minibank.account.service.AccountService;

@WebMvcTest(controllers = AccountController.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AccountControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
		
	@MockBean(name = "accountService")
	private AccountService accountService;
	
	@Test
	void 계좌조회() throws Exception {
		//given
		String acntNo = "1111";
		
		Account account = Account.builder()
                .acntNo(acntNo)
                .cstmId("111101")
                .cstmNm("홍길동")
                .acntNm("샘플계좌").build();
		
		given(accountService.retrieveAccount(acntNo)).willReturn(account);
		
		//when then
		mockMvc.perform(get("/account/rest/v0.8/" + acntNo)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.acntNo", equalTo(account.getAcntNo())))
				.andExpect(jsonPath("$.cstmId", equalTo(account.getCstmId())))
				.andExpect(jsonPath("$.cstmNm", equalTo(account.getCstmNm())))
				.andExpect(jsonPath("$.acntNm", equalTo(account.getAcntNm())));
		
		//then
		verify(accountService).retrieveAccount(acntNo);
	}

}

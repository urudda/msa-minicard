package com.minibank.customer.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.minibank.customer.rest.account.entity.Account;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Data
public class Customer {

	private String cstmId;
	private String cstmNm;
	private String cstmAge;
	private String cstmGnd;
	private String cstmPn;
	private String cstmAdr;

	/* 이체 한도 정보 */
	private Long oneTmTrnfLmt;
	private Long oneDyTrnfLmt;

	/* 계좌 목록 */

	private List<Account> accounts = new ArrayList<>();

	public void addAllAccounts(List<Account> accounts) {
		this.accounts.addAll(accounts);
	}

	@Builder
	public Customer(String cstmId, String cstmNm, String cstmAge, String cstmGnd, String cstmPn, String cstmAdr,
			Long oneTmTrnfLmt, Long oneDyTrnfLmt) {
		super();
		this.cstmId = cstmId;
		this.cstmNm = cstmNm;
		this.cstmAge = cstmAge;
		this.cstmGnd = cstmGnd;
		this.cstmPn = cstmPn;
		this.cstmAdr = cstmAdr;
		this.oneTmTrnfLmt = oneTmTrnfLmt;
		this.oneDyTrnfLmt = oneDyTrnfLmt;
	}
}

package com.minibank.inquiry.domain.entity;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TransactionHistory implements Serializable {
    private String acntNo;
    private int seq;
    private String divCd;
    private String stsCd;
    private Long trnsAmt;
    private Long acntBlnc;
    private String trnsBrnch;
    private String trnsDtm;
    
    @Builder
	public TransactionHistory(String acntNo, int seq, String divCd, String stsCd, Long trnsAmt, Long acntBlnc,
			String trnsBrnch, String trnsDtm) {
		super();
		this.acntNo = acntNo;
		this.seq = seq;
		this.divCd = divCd;
		this.stsCd = stsCd;
		this.trnsAmt = trnsAmt;
		this.acntBlnc = acntBlnc;
		this.trnsBrnch = trnsBrnch;
		this.trnsDtm = trnsDtm;
	}
}

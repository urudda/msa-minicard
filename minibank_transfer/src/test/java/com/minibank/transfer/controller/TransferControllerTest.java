package com.minibank.transfer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.minibank.transfer.domain.entity.TransferHistory;
import com.minibank.transfer.domain.entity.TransferLimit;
import com.minibank.transfer.service.TransferService;
@WebMvcTest(controllers = TransferController.class)
public class TransferControllerTest {

	@Autowired
    private MockMvc mockMvc;
    
	@MockBean(name = "transferService")
	private TransferService transferService;
	
	@Test
    void transfer_withTransferHistory_ReturnTransferHistory() throws Exception {
    	//given (테스트 준비사항)
    	TransferHistory transferHistory = TransferHistory.builder()
    			.cstmId("1111")
    			.seq(1)
    		    .divCd("1")
    		    .stsCd("3")
    		    .dpstAcntNo("222201")
    		    .wthdAcntNo("111101")
    		    .wthdAcntSeq(1)
    		    .sndMm("11")
    		    .rcvMm("홍길동")
    		    .rcvCstmNm("홍길순")
    		    .trnfAmt(100L)
    		    .trnfDtm("2020-03-09 16:59:58").build();
    		
    	// BDD -> transfer service 를 정의하기 위해서, stub
    	BDDMockito.given(transferService.transfer(transferHistory)).willReturn(transferHistory);
    	
    	// when (테스트 수행)
		mockMvc.perform(post("/rest/v0.8")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content("{\"cstmId\":\"1111\","
    					+ "\"seq\":\"1\","
    					+ "\"divCd\":\"1\","
    					+ "\"stsCd\":\"3\","
    					+ "\"dpstAcntNo\":\"222201\","
    					+ "\"wthdAcntNo\":\"111101\","
    					+ "\"wthdAcntSeq\":\"1\","
    					+ "\"sndMm\":\"11\","
    					+ "\"rcvMm\":\"홍길동\","
    					+ "\"rcvCstmNm\":\"홍길순\","
    					+ "\"trnfAmt\":\"100\","
    					+ "\"trnfDtm\":\"2020-03-09 16:59:58\"}"))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			// Transfer Service 를 Mock 으로 만들어 놨기 때문에 현재 return 값이 null이다. 따라서 cstmId 값이 null 이므로 1111 값과 달라서 error
    			.andExpect(MockMvcResultMatchers.jsonPath("$.cstmId", Matchers.equalTo("1111")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.seq", Matchers.equalTo(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.divCd", Matchers.equalTo("1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.stsCd", Matchers.equalTo("3")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.dpstAcntNo", Matchers.equalTo("222201")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.wthdAcntNo", Matchers.equalTo("111101")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.wthdAcntSeq", Matchers.equalTo(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.sndMm", Matchers.equalTo("11")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rcvMm", Matchers.equalTo("홍길동")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.rcvCstmNm", Matchers.equalTo("홍길순")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trnfAmt", Matchers.equalTo(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.trnfDtm", Matchers.equalTo("2020-03-09 16:59:58")));
				
		// then (테스트 검증)
		BDDMockito.verify(transferService).transfer(transferHistory);
		
    }
	
	@Test
    void btobTransfer_withTransferHistory_ReturnBoolean() throws Exception {
    	//given (테스트 준비사항)
    	TransferHistory transferHistory = TransferHistory.builder()
    			.cstmId("1111")
    			.seq(1)
    		    .divCd("2")
    		    .stsCd("3")
    		    .dpstAcntNo("222201")
    		    .wthdAcntNo("111101")
    		    .wthdAcntSeq(1)
    		    .sndMm("11")
    		    .rcvMm("홍길동")
    		    .rcvCstmNm("홍길순")
    		    .trnfAmt(100L)
    		    .trnfDtm("2020-03-09 16:59:58").build();
    		
    	// BDD -> transfer service 를 정의하기 위해서, stub
    	BDDMockito.given(transferService.btobTransfer(transferHistory)).willReturn(true);
    	
    	// when (테스트 수행)
		mockMvc.perform(post("/b2b/rest/v0.8")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content("{\"cstmId\":\"1111\","
    					+ "\"seq\":\"1\","
    					+ "\"divCd\":\"2\","
    					+ "\"stsCd\":\"3\","
    					+ "\"dpstAcntNo\":\"222201\","
    					+ "\"wthdAcntNo\":\"111101\","
    					+ "\"wthdAcntSeq\":\"1\","
    					+ "\"sndMm\":\"11\","
    					+ "\"rcvMm\":\"홍길동\","
    					+ "\"rcvCstmNm\":\"홍길순\","
    					+ "\"trnfAmt\":\"100\","
    					+ "\"trnfDtm\":\"2020-03-09 16:59:58\"}"))
    			.andExpect(MockMvcResultMatchers.status().isOk());
				
		// then (테스트 검증)
		BDDMockito.verify(transferService).btobTransfer(transferHistory);
		
    }
	
    @Test
    void retrieveTransferHistoryList_withCstmId_RetrurnListTransferHistory() throws Exception {
    	//given (테스트 준비사항)
    	String cstmId = "1111";
    	
    	TransferHistory transferHistory = TransferHistory.builder()
    			.cstmId(cstmId)
    			.seq(1)
    		    .divCd("1")
    		    .stsCd("3")
    		    .dpstAcntNo("222201")
    		    .wthdAcntNo("111101")
    		    .wthdAcntSeq(1)
    		    .sndMm("11")
    		    .rcvMm("홍길동")
    		    .rcvCstmNm("홍길순")
    		    .trnfAmt(100L)
    		    .trnfDtm("2020-03-09 16:59:58").build();
    		
    	
    	//List<TransferHistory> historys = Collections.singletonList(transferHistory);
    	List<TransferHistory> historyList = Arrays.asList(transferHistory);
    	
    	// BDD -> transfer service 를 정의하기 위해서, stub
    	BDDMockito.given(transferService.retrieveTransferHistoryList(cstmId)).willReturn(historyList);
    	
    	// when (테스트 수행)
		mockMvc.perform(get("/transfer-history/rest/v0.8/" + cstmId)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			// Transfer Service 를 Mock 으로 만들어 놨기 때문에 현재 return 값이 null이다. 따라서 cstmId 값이 null 이므로 1111 값과 달라서 error
    			.andExpect(MockMvcResultMatchers.jsonPath("$[0].cstmId", Matchers.equalTo(cstmId)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].seq", Matchers.equalTo(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].divCd", Matchers.equalTo("1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].stsCd", Matchers.equalTo("3")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].dpstAcntNo", Matchers.equalTo("222201")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].wthdAcntNo", Matchers.equalTo("111101")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].wthdAcntSeq", Matchers.equalTo(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].sndMm", Matchers.equalTo("11")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].rcvMm", Matchers.equalTo("홍길동")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].rcvCstmNm", Matchers.equalTo("홍길순")))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].trnfAmt", Matchers.equalTo(100)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].trnfDtm", Matchers.equalTo("2020-03-09 16:59:58")));
				
		
		// then (테스트 검증)
		BDDMockito.verify(transferService).retrieveTransferHistoryList(cstmId);
		
    }
    
    @Test
    void retrieveTransferLimit_withCstmId_RetrurnTransferLimit() throws Exception {
    	//given (테스트 준비사항)
    	String cstmId = "1111";
    	
    	TransferLimit transferLimit = TransferLimit.builder()
    			.cstmId(cstmId)
    			.oneTmTrnfLmt(50000000L)
    		    .oneDyTrnfLmt(500000000L).build();
    		
    	// BDD -> transfer service 를 정의하기 위해서, stub
    	BDDMockito.given(transferService.retrieveTransferLimit(cstmId)).willReturn(transferLimit);
    	
    	// when (테스트 수행)
		mockMvc.perform(get("/transfer-limit/rest/v0.8/" + cstmId)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			// Transfer Service 를 Mock 으로 만들어 놨기 때문에 현재 return 값이 null이다. 따라서 cstmId 값이 null 이므로 1111 값과 달라서 error
    			.andExpect(MockMvcResultMatchers.jsonPath("$.cstmId", Matchers.equalTo(cstmId)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oneTmTrnfLmt", Matchers.equalTo(50000000)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oneDyTrnfLmt", Matchers.equalTo(500000000)));
		
		// then (테스트 검증)
		BDDMockito.verify(transferService).retrieveTransferLimit(cstmId);
		
    }
    
    @Test
    void retrieveEnableTransferLimit_withCstmId_RetrurnTransferLimit() throws Exception {
    	//given (테스트 준비사항)
    	String cstmId = "1111";
    	
    	TransferLimit transferLimit = TransferLimit.builder()
    			.cstmId(cstmId)
    			.oneTmTrnfLmt(50000000L)
    		    .oneDyTrnfLmt(500000000L).build();
    		
    	// BDD -> transfer service 를 정의하기 위해서, stub
    	BDDMockito.given(transferService.retrieveEnableTransferLimit(cstmId)).willReturn(transferLimit);
    	
    	// when (테스트 수행)
		mockMvc.perform(get("/transfer-limit/enable/rest/v0.8/" + cstmId)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			// Transfer Service 를 Mock 으로 만들어 놨기 때문에 현재 return 값이 null이다. 따라서 cstmId 값이 null 이므로 1111 값과 달라서 error
    			.andExpect(MockMvcResultMatchers.jsonPath("$.cstmId", Matchers.equalTo(cstmId)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oneTmTrnfLmt", Matchers.equalTo(50000000)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oneDyTrnfLmt", Matchers.equalTo(500000000)));
		
		// then (테스트 검증)
		BDDMockito.verify(transferService).retrieveEnableTransferLimit(cstmId);
		
    }
    
}
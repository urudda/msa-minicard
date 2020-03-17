package com.minibank.transfer.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.minibank.transfer.domain.entity.TransferHistory;
import com.minibank.transfer.domain.entity.TransferLimit;
import com.minibank.transfer.service.TransferService;

import io.swagger.annotations.ApiOperation;

@RestController
public class TransferController {

    @Resource(name = "transferService")
    private TransferService transferService;
    
    @ApiOperation(value = "당행이체", httpMethod = "POST", notes = "당행이체")
    @RequestMapping(method = RequestMethod.POST, path = "/rest/v0.8")
    public TransferHistory transfer(@RequestBody TransferHistory input) throws Exception{
        return transferService.transfer(input);
    }
    
    /*타행 이체*/
    @ApiOperation(value = "타행이체", httpMethod = "POST", notes = "타행이체")
    @RequestMapping(method = RequestMethod.POST, path = "/b2b/rest/v0.8")
    public Boolean btobTransfer(@RequestBody TransferHistory input) throws Exception{
    	return transferService.btobTransfer(input);
    }

    @ApiOperation(value = "이체이력조회", httpMethod = "GET", notes = "이체이력조회")
    @RequestMapping(method = RequestMethod.GET, path = "/transfer-history/rest/v0.8/{cstmId}")
    public List<TransferHistory> retrieveTransferHistoryList(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        List<TransferHistory> transferHistory = transferService.retrieveTransferHistoryList(cstmId);
        return transferHistory;
    }

    @ApiOperation(value = "이체한도등록", httpMethod = "POST", notes = "이체한도등록")
    @RequestMapping(method = RequestMethod.POST, path = "/transfer-limit/rest/v0.8")
    public Integer createTransferLimit(@RequestBody TransferLimit input) throws Exception{
        return  transferService.createTransferLimit(input);
    }

    @ApiOperation(value = "이체한도조회", httpMethod = "GET", notes = "이체한도조회")
    @RequestMapping(method = RequestMethod.GET, path = "/transfer-limit/rest/v0.8/{cstmId}")
    public TransferLimit retrieveTransferLimit(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return transferService.retrieveTransferLimit(cstmId);
    }

    @ApiOperation(value = "이체가능한도조회", httpMethod = "GET", notes = "이체가능한도조회")
    @RequestMapping(method = RequestMethod.GET, path = "/transfer-limit/enable/rest/v0.8/{cstmId}")
    public TransferLimit retrieveEnableTransferLimit(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return  transferService.retrieveEnableTransferLimit(cstmId);
    }
}

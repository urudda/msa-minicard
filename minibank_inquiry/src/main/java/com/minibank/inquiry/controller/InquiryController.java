package com.minibank.inquiry.controller;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.minibank.inquiry.domain.entity.Customer;
import com.minibank.inquiry.domain.entity.RecentWork;
import com.minibank.inquiry.service.InquiryService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(path = "/cqrs")
public class InquiryController {

    @Resource(name = "inquiryService")
    private InquiryService inquiryService;
    
    @ApiOperation(value = "고객상세조회", httpMethod = "GET", notes = "고객상세조회")
    @RequestMapping(method = RequestMethod.GET, path = "/detail/rest/v0.8/{cstmId}")
    public Customer retrieveCustomerDetail(@PathVariable(name = "cstmId") String cstmId) throws Exception{
        return inquiryService.retrieveCustomerDetail(cstmId);
    }
    
    @ApiOperation(value = "휴면고객목록조회", httpMethod = "GET", notes = "휴면고객목록조회")
    @RequestMapping(method = RequestMethod.GET, path = "/dormat-customer/list/rest/v0.8/")
    public List<RecentWork> retrieveCustomerDetail() throws Exception{
        return inquiryService.retrieveDormantCustomerList();
    }
    
}

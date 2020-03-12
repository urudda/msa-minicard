package com.minibank.account.rest.customer;

import com.minibank.account.rest.customer.entity.Customer;

public interface CustomerComposite {
    Customer retrieveCustomer(String cstmId) throws Exception;
}

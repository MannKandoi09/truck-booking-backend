
package com.truckbooking.service;

import com.truckbooking.dto.request.CustomerRequest;
import com.truckbooking.dto.response.CustomerResponse;
import com.truckbooking.response.ApiResponse;

import java.util.List;

public interface CustomerService {

    ApiResponse<CustomerResponse> addCustomer(CustomerRequest request);

    ApiResponse<List<CustomerResponse>> getAllCustomers();

    ApiResponse<CustomerResponse> getCustomerById(Long id);

    ApiResponse<CustomerResponse> updateCustomer(
            Long id,
            CustomerRequest request
    );

    ApiResponse<Void> deleteCustomer(Long id);
}


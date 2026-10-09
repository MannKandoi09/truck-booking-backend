
package com.truckbooking.controller.admin;

import com.truckbooking.dto.request.CustomerRequest;
import com.truckbooking.dto.response.CustomerResponse;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerResponse> addCustomer(
            @Valid @RequestBody CustomerRequest request) {
        return customerService.addCustomer(request);
    }

    @GetMapping
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerResponse> getCustomerById(
            @PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public ApiResponse<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }
}


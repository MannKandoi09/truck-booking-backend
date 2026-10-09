
package com.truckbooking.service.impl;

import com.truckbooking.dto.request.CustomerRequest;
import com.truckbooking.dto.response.CustomerResponse;
import com.truckbooking.entity.Customer;
import com.truckbooking.enums.CustomerStatus;
import com.truckbooking.repository.CustomerRepository;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.CustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public ApiResponse<CustomerResponse> addCustomer(CustomerRequest request) {

        if (customerRepository.existsByPhone(request.getPhone())) {
            return ApiResponse.<CustomerResponse>builder()
                    .success(false)
                    .message("Phone number already exists")
                    .build();
        }

        if (customerRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.<CustomerResponse>builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }

        Customer customer = Customer.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .companyName(request.getCompanyName())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .status(request.getStatus() != null
                        ? request.getStatus()
                        : CustomerStatus.ACTIVE)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer added successfully")
                .data(toResponse(savedCustomer))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {

        List<CustomerResponse> customers = customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ApiResponse.<List<CustomerResponse>>builder()
                .success(true)
                .message("Customers fetched successfully")
                .data(customers)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<CustomerResponse> getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            return ApiResponse.<CustomerResponse>builder()
                    .success(false)
                    .message("Customer not found with ID: " + id)
                    .build();
        }

        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer fetched successfully")
                .data(toResponse(customer))
                .build();
    }

    @Override
    public ApiResponse<CustomerResponse> updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            return ApiResponse.<CustomerResponse>builder()
                    .success(false)
                    .message("Customer not found with ID: " + id)
                    .build();
        }

        if (customerRepository.existsByPhone(request.getPhone())
                && !customer.getPhone().equals(request.getPhone())) {
            return ApiResponse.<CustomerResponse>builder()
                    .success(false)
                    .message("Phone number already exists")
                    .build();
        }

        if (customerRepository.existsByEmail(request.getEmail())
                && !customer.getEmail().equalsIgnoreCase(request.getEmail())) {
            return ApiResponse.<CustomerResponse>builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }

        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setCompanyName(request.getCompanyName());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setPincode(request.getPincode());

        if (request.getStatus() != null) {
            customer.setStatus(request.getStatus());
        }

        Customer updatedCustomer = customerRepository.save(customer);

        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer updated successfully")
                .data(toResponse(updatedCustomer))
                .build();
    }

    @Override
    public ApiResponse<Void> deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            return ApiResponse.<Void>builder()
                    .success(false)
                    .message("Customer not found with ID: " + id)
                    .build();
        }

        customer.setStatus(CustomerStatus.INACTIVE);
        customerRepository.save(customer);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Customer deactivated successfully")
                .build();
    }

    private CustomerResponse toResponse(Customer customer) {

        return CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .companyName(customer.getCompanyName())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .pincode(customer.getPincode())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}


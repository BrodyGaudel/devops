package com.mounanga.customerservice.web;

import com.mounanga.customerservice.dto.CustomerRequestDTO;
import com.mounanga.customerservice.dto.CustomerResponseDTO;
import com.mounanga.customerservice.dto.PageModel;
import com.mounanga.customerservice.exception.CinAlreadyExistException;
import com.mounanga.customerservice.exception.CustomerNotAdultException;
import com.mounanga.customerservice.exception.CustomerNotFoundException;
import com.mounanga.customerservice.exception.EmailAlreadyExistException;
import com.mounanga.customerservice.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public CustomerResponseDTO createCustomer(@RequestBody CustomerRequestDTO dto) throws CinAlreadyExistException, EmailAlreadyExistException, CustomerNotAdultException {
        return customerService.createCustomer(dto);
    }

    @PutMapping("/update/{id}")
    public CustomerResponseDTO updateCustomer(@PathVariable String id, @RequestBody CustomerRequestDTO dto) throws CustomerNotFoundException, CinAlreadyExistException, EmailAlreadyExistException, CustomerNotAdultException {
        return customerService.updateCustomer(id, dto);
    }

    @GetMapping("/get/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/list")
    public PageModel<CustomerResponseDTO> getAllCustomers(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        return customerService.getAllCustomers(page, size);
    }

    @GetMapping("/search")
    public PageModel<CustomerResponseDTO> searchCustomers(@RequestParam(defaultValue = "") String keyword,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        return customerService.searchCustomers(keyword, page, size);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomerById(@PathVariable String id) {
        customerService.deleteCustomerById(id);
    }

}


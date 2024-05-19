package com.mounanga.customerservice.service.implementation;

import com.mounanga.customerservice.dto.CustomerPageResponseDTO;
import com.mounanga.customerservice.dto.CustomerRequestDTO;
import com.mounanga.customerservice.dto.CustomerResponseDTO;
import com.mounanga.customerservice.dto.PageModel;
import com.mounanga.customerservice.entity.Customer;
import com.mounanga.customerservice.exception.CinAlreadyExistException;
import com.mounanga.customerservice.exception.CustomerNotAdultException;
import com.mounanga.customerservice.exception.CustomerNotFoundException;
import com.mounanga.customerservice.exception.EmailAlreadyExistException;
import com.mounanga.customerservice.repository.CustomerRepository;
import com.mounanga.customerservice.service.CustomerService;
import com.mounanga.customerservice.util.Mappers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final Mappers mappers;

    public CustomerServiceImpl(CustomerRepository customerRepository, Mappers mappers) {
        this.customerRepository = customerRepository;
        this.mappers = mappers;
    }

    @Override
    public CustomerResponseDTO createCustomer(@NotNull CustomerRequestDTO dto){
        log.info("In createCustomer() : ");
        validateDataBeforeSave(dto);
        Customer customer = mappers.fromCustomerRequestDTO(dto);
        Customer customerSaved = customerRepository.save(customer);
        log.info("customer created successfully with id '{}' at '{}'", customerSaved.getId(), customerSaved.getCreation());
        return mappers.fromCustomer(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(String id, @NotNull CustomerRequestDTO dto) {
        log.info("In updateCustomer()");
        Customer customer = customerRepository.findById(id).orElseThrow( () -> new CustomerNotFoundException("customer with id '"+id+"' not found"));
        validateDataBeforeUpdate(customer, dto);
        updateCustomerWithNewData(customer, dto);
        Customer customerUpdated = customerRepository.save(customer);
        log.info("customer with id '{}' updated at '{}'", customerUpdated.getId(),customerUpdated.getLastUpdated());
        return mappers.fromCustomer(customerUpdated);
    }

    @Override
    public CustomerResponseDTO getCustomerById(String id)  {
        log.info("In getCustomerById()");
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("customer not found"));
        log.info("customer found");
        return mappers.fromCustomer(customer);
    }

    @Override
    public PageModel<CustomerResponseDTO> getAllCustomers(int page, int size) {
        log.info("In getAllCustomers()");
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));
        log.info("'{}' customer(s) found", customerPage.getTotalElements());
        return fromPage(customerPage, page);
    }

    @Override
    public PageModel<CustomerResponseDTO> searchCustomers(String keyword, int page, int size) {
        log.info("In searchCustomers()");
        Page<Customer> customerPage = customerRepository.search("%"+keyword+"%", PageRequest.of(page, size));
        log.info("customer(s) found");
        return fromPage(customerPage, page);
    }

    @Override
    public void deleteCustomerById(String id) {
        log.info("In deleteCustomerById()");
        customerRepository.deleteById(id);
    }



    /**
     * Checks if a given date corresponds to an adult (18 years or older).
     *
     * @param date The birthdate to be checked.
     * @return {@code true} if the person is an adult, {@code false} otherwise.
     */
    private boolean isAdult(LocalDate date){
        LocalDate now = LocalDate.now();
        Period age = Period.between(date, now);
        return age.getYears() <= 18 && (age.getYears() != 18 || (age.getMonths() <= 0 && age.getDays() <= 0));
    }

    /**
     * Updates the given customer with the data from the provided CustomerRequestDTO.
     *
     * @param customer The customer to be updated.
     * @param dto      The CustomerRequestDTO containing the new data.
     */
    private void updateCustomerWithNewData(@NotNull Customer customer, @NotNull CustomerRequestDTO dto){
        customer.setCin(dto.cin());
        customer.setEmail(dto.email());
        customer.setFirstname(dto.firstname());
        customer.setName(dto.name());
        customer.setDateOfBirth(dto.dateOfBirth());
        customer.setPlaceOfBirth(dto.placeOfBirth());
        customer.setNationality(dto.nationality());
        customer.setSex(dto.sex());
        customer.setLastUpdated(LocalDateTime.now());
    }

    /**
     * Validates the provided customer data against existing records and business rules.
     * Throws exceptions if validation fails.
     *
     * @param customer           The customer object to be validated.
     * @param dto                The CustomerRequestDTO containing the new data.
     * @throws CustomerNotAdultException    If the customer is not an adult.
     * @throws EmailAlreadyExistException   If there is already a customer with the new email.
     * @throws CinAlreadyExistException     If there is already a customer with the new cin.
     */
    private void validateDataBeforeUpdate(@NotNull Customer customer, @NotNull CustomerRequestDTO dto) throws CustomerNotAdultException, EmailAlreadyExistException, CinAlreadyExistException {
        if (isAdult(dto.dateOfBirth())) {
            throw new CustomerNotAdultException("Customer is not an adult");
        }

        if (!customer.getEmail().equals(dto.email()) && customerRepository.emailExists(dto.email())) {
            throw new EmailAlreadyExistException("There is already a customer with this new email");
        }

        if (!customer.getCin().equals(dto.cin()) && customerRepository.cinExists(dto.cin())) {
            throw new CinAlreadyExistException("There is already a customer with this new cin");
        }
    }

    /**
     * Validates the provided customer data against existing records and business rules.
     * Throws exceptions if validation fails.
     *
     * @param dto                The CustomerRequestDTO containing the new data.
     * @throws CustomerNotAdultException    If the customer is not an adult.
     * @throws CinAlreadyExistException     If there is already a customer with the new cin.
     * @throws EmailAlreadyExistException   If there is already a customer with the new email.
     */
    private void validateDataBeforeSave(@NotNull CustomerRequestDTO dto) throws CustomerNotAdultException, CinAlreadyExistException, EmailAlreadyExistException {
        if (isAdult(dto.dateOfBirth())) {
            throw new CustomerNotAdultException("Customer is not an adult");
        }

        if (customerRepository.cinExists(dto.cin())) {
            throw new CinAlreadyExistException("There is already a customer with this cin");
        }

        if (customerRepository.emailExists(dto.email())) {
            throw new EmailAlreadyExistException("There is already a customer with this email");
        }
    }

    private @NotNull PageModel<CustomerResponseDTO> fromPage(@NotNull Page<Customer> customerPage, int page) {
        PageModel<CustomerResponseDTO> pageModel = new PageModel<>();
        pageModel.setTotalElements(customerPage.getTotalElements());
        pageModel.setTotalPages(customerPage.getTotalPages());
        pageModel.setFirst(customerPage.isFirst());
        pageModel.setLast(customerPage.isLast());
        pageModel.setHasNext(customerPage.hasNext());
        pageModel.setHasPrevious(customerPage.hasPrevious());
        pageModel.setHasContent(customerPage.hasContent());
        pageModel.setSize(customerPage.getSize());
        pageModel.setNumber(customerPage.getNumber());
        pageModel.setPage(page);
        pageModel.setContent(mappers.fromListOfCustomers(customerPage.getContent()));
        return pageModel;
    }


}

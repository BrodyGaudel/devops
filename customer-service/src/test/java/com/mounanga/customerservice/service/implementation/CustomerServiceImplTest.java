package com.mounanga.customerservice.service.implementation;

import com.mounanga.customerservice.dto.CustomerRequestDTO;
import com.mounanga.customerservice.dto.CustomerResponseDTO;
import com.mounanga.customerservice.dto.PageModel;
import com.mounanga.customerservice.entity.Customer;
import com.mounanga.customerservice.enums.Sex;
import com.mounanga.customerservice.exception.CinAlreadyExistException;
import com.mounanga.customerservice.exception.CustomerNotAdultException;
import com.mounanga.customerservice.exception.CustomerNotFoundException;
import com.mounanga.customerservice.exception.EmailAlreadyExistException;
import com.mounanga.customerservice.repository.CustomerRepository;
import com.mounanga.customerservice.util.Mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Mappers mappers;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository, mappers);
    }

    @Test
    void testCreateCustomerShouldCreate() {
        //request
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.of(1990, 5, 15), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality()).dateOfBirth(dto.dateOfBirth())
                .placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email()).creation(LocalDateTime.now()).lastUpdated(null).id(UUID.randomUUID().toString())
                .build();
        when(customerRepository.cinExists(anyString())).thenReturn(false);
        when(customerRepository.emailExists(anyString())).thenReturn(false);
        when(mappers.fromCustomerRequestDTO(dto)).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(any())).thenReturn(
                new CustomerResponseDTO(
                        customer.getId(), customer.getCin(), customer.getFirstname(), customer.getName(),
                        customer.getDateOfBirth(), customer.getPlaceOfBirth(), customer.getNationality(),
                        customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated()
                )
        );
        CustomerResponseDTO response = customerService.createCustomer(dto);
        assertNotNull(response);
        assertNotNull(response.id());
        assertNotNull(response.creation());
        assertNull(response.lastUpdated());
    }

    @Test
    void testCreateCustomerThrowCustomerNotAdultException() {
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.now(), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality()).dateOfBirth(dto.dateOfBirth())
                .placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email()).creation(LocalDateTime.now()).lastUpdated(null).id(UUID.randomUUID().toString())
                .build();
        when(customerRepository.cinExists(anyString())).thenReturn(false);
        when(customerRepository.emailExists(anyString())).thenReturn(false);
        when(mappers.fromCustomerRequestDTO(dto)).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(any())).thenReturn(
                new CustomerResponseDTO(
                        customer.getId(), customer.getCin(), customer.getFirstname(), customer.getName(),
                        customer.getDateOfBirth(), customer.getPlaceOfBirth(), customer.getNationality(),
                        customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated()
                )
        );
        assertThrows(CustomerNotAdultException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomerThrowCinAlreadyExistException() {
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.of(1990,5,15), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality()).dateOfBirth(dto.dateOfBirth())
                .placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email()).creation(LocalDateTime.now()).lastUpdated(null).id(UUID.randomUUID().toString())
                .build();
        when(customerRepository.cinExists(anyString())).thenReturn(true);
        when(customerRepository.emailExists(anyString())).thenReturn(false);
        when(mappers.fromCustomerRequestDTO(dto)).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(any())).thenReturn(
                new CustomerResponseDTO(
                        customer.getId(), customer.getCin(), customer.getFirstname(), customer.getName(),
                        customer.getDateOfBirth(), customer.getPlaceOfBirth(), customer.getNationality(),
                        customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated()
                )
        );
        assertThrows(CinAlreadyExistException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testCreateCustomerThrowEmailAlreadyExistException() {
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.of(1990,5,15), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality()).dateOfBirth(dto.dateOfBirth())
                .placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email()).creation(LocalDateTime.now()).lastUpdated(null).id(UUID.randomUUID().toString())
                .build();
        when(customerRepository.cinExists(anyString())).thenReturn(false);
        when(customerRepository.emailExists(anyString())).thenReturn(true);
        when(mappers.fromCustomerRequestDTO(dto)).thenReturn(customer);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(any())).thenReturn(
                new CustomerResponseDTO(
                        customer.getId(), customer.getCin(), customer.getFirstname(), customer.getName(),
                        customer.getDateOfBirth(), customer.getPlaceOfBirth(), customer.getNationality(),
                        customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated()
                )
        );
        assertThrows(EmailAlreadyExistException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testUpdateCustomerShouldUpdate() {
        String id = UUID.randomUUID().toString();
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.of(1990,5,15), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customerFound = Customer.builder().name("old_name").firstname("old_firstname").nationality("old_nationality")
                .dateOfBirth(LocalDate.of(1995,5,15)).placeOfBirth("old_place").sex(Sex.F)
                .email("old_mail@spring.io").creation(LocalDateTime.now()).lastUpdated(null).id(id).cin("old_cin")
                .build();

        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality())
                .dateOfBirth(dto.dateOfBirth()).placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email())
                .creation(LocalDateTime.now()).lastUpdated(LocalDateTime.now()).id(id).cin(dto.cin())
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerFound));
        when(customerRepository.cinExists(anyString())).thenReturn(false);
        when(customerRepository.emailExists(anyString())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(customer)).thenReturn(new CustomerResponseDTO(customer.getId(), customer.getCin(),
                customer.getFirstname(), customer.getName(), customer.getDateOfBirth(), customer.getPlaceOfBirth(),
                customer.getNationality(), customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated())
        );

        CustomerResponseDTO response = customerService.updateCustomer(id, dto);
        assertNotNull(response);
        assertNotNull(response.lastUpdated());
        assertEquals(id,response.id());
        assertEquals(customer.getCreation(), response.creation());
        assertEquals(dto.firstname(), response.firstname());
        assertEquals(dto.name(), response.name());
        assertEquals(dto.cin(), response.cin());
        assertEquals(dto.dateOfBirth(), response.dateOfBirth());
        assertEquals(dto.placeOfBirth(), response.placeOfBirth());
        assertEquals(dto.email(), response.email());
        assertEquals(dto.sex(), response.sex());
    }

    @Test
    void testUpdateCustomerThrowCustomerNotAdultException() {
        String id = UUID.randomUUID().toString();
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.now(), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customerFound = Customer.builder().name("old_name").firstname("old_firstname").nationality("old_nationality")
                .dateOfBirth(LocalDate.of(1995,5,15)).placeOfBirth("old_place").sex(Sex.F)
                .email("old_mail@spring.io").creation(LocalDateTime.now()).lastUpdated(null).id(id).cin("old_cin")
                .build();

        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality())
                .dateOfBirth(dto.dateOfBirth()).placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email())
                .creation(LocalDateTime.now()).lastUpdated(LocalDateTime.now()).id(id).cin(dto.cin())
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerFound));
        when(customerRepository.cinExists(anyString())).thenReturn(false);
        when(customerRepository.emailExists(anyString())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(customer)).thenReturn(new CustomerResponseDTO(customer.getId(), customer.getCin(),
                customer.getFirstname(), customer.getName(), customer.getDateOfBirth(), customer.getPlaceOfBirth(),
                customer.getNationality(), customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated())
        );
        assertThrows(CustomerNotAdultException.class, () -> customerService.updateCustomer(id, dto));
    }

    @Test
    void testUpdateCustomerThrowEmailAlreadyExistException() {
        String id = UUID.randomUUID().toString();
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.of(2000,9,20), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customerFound = Customer.builder().name("old_name").firstname("old_firstname").nationality("old_nationality")
                .dateOfBirth(LocalDate.of(1995,5,15)).placeOfBirth("old_place").sex(Sex.F)
                .email("old_mail@spring.io").creation(LocalDateTime.now()).lastUpdated(null).id(id).cin("old_cin")
                .build();

        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality())
                .dateOfBirth(dto.dateOfBirth()).placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email())
                .creation(LocalDateTime.now()).lastUpdated(LocalDateTime.now()).id(id).cin(dto.cin())
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerFound));
        when(customerRepository.cinExists(anyString())).thenReturn(false);
        when(customerRepository.emailExists(anyString())).thenReturn(true);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(customer)).thenReturn(new CustomerResponseDTO(customer.getId(), customer.getCin(),
                customer.getFirstname(), customer.getName(), customer.getDateOfBirth(), customer.getPlaceOfBirth(),
                customer.getNationality(), customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated())
        );
        assertThrows(EmailAlreadyExistException.class, () -> customerService.updateCustomer(id, dto));
    }

    @Test
    void testUpdateCustomerThrowCinAlreadyExistException() {
        String id = UUID.randomUUID().toString();
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.of(2000,9,20), "world", "world", Sex.M, "email@spring.io"
        );
        Customer customerFound = Customer.builder().name("old_name").firstname("old_firstname").nationality("old_nationality")
                .dateOfBirth(LocalDate.of(1995,5,15)).placeOfBirth("old_place").sex(Sex.F)
                .email("old_mail@spring.io").creation(LocalDateTime.now()).lastUpdated(null).id(id).cin("old_cin")
                .build();

        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality())
                .dateOfBirth(dto.dateOfBirth()).placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email())
                .creation(LocalDateTime.now()).lastUpdated(LocalDateTime.now()).id(id).cin(dto.cin())
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customerFound));
        when(customerRepository.cinExists(anyString())).thenReturn(true);
        when(customerRepository.emailExists(anyString())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(customer)).thenReturn(new CustomerResponseDTO(customer.getId(), customer.getCin(),
                customer.getFirstname(), customer.getName(), customer.getDateOfBirth(), customer.getPlaceOfBirth(),
                customer.getNationality(), customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated())
        );
        assertThrows(CinAlreadyExistException.class, () -> customerService.updateCustomer(id, dto));
    }

    @Test
    void testUpdateCustomerThrowCustomerNotFoundException() {
        String id = UUID.randomUUID().toString();
        CustomerRequestDTO dto = new CustomerRequestDTO("123cin321","firstname", "name",
                LocalDate.now(), "world", "world", Sex.M, "email@spring.io"
        );

        Customer customer = Customer.builder().name(dto.name()).firstname(dto.firstname()).nationality(dto.nationality())
                .dateOfBirth(dto.dateOfBirth()).placeOfBirth(dto.placeOfBirth()).sex(dto.sex()).email(dto.email())
                .creation(LocalDateTime.now()).lastUpdated(LocalDateTime.now()).id(id).cin(dto.cin())
                .build();

        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        when(customerRepository.cinExists(anyString())).thenReturn(false);
        when(customerRepository.emailExists(anyString())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);
        when(mappers.fromCustomer(customer)).thenReturn(new CustomerResponseDTO(customer.getId(), customer.getCin(),
                customer.getFirstname(), customer.getName(), customer.getDateOfBirth(), customer.getPlaceOfBirth(),
                customer.getNationality(), customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated())
        );
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(id, dto));
    }

    @Test
    void testGetCustomerByIdShouldFound() {
        String id = UUID.randomUUID().toString();
        Customer customer = Customer.builder().name("old_name").firstname("old_firstname").nationality("old_nationality")
                .dateOfBirth(LocalDate.of(1995,5,15)).placeOfBirth("old_place").sex(Sex.F)
                .email("old_mail@spring.io").creation(LocalDateTime.now()).lastUpdated(null).id(id).cin("old_cin")
                .build();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(mappers.fromCustomer(customer)).thenReturn(new CustomerResponseDTO(customer.getId(), customer.getCin(),
                customer.getFirstname(), customer.getName(), customer.getDateOfBirth(), customer.getPlaceOfBirth(),
                customer.getNationality(), customer.getSex(), customer.getEmail(), customer.getCreation(), customer.getLastUpdated())
        );
        CustomerResponseDTO dto = customerService.getCustomerById(id);
        assertNotNull(dto);
        assertEquals(id, dto.id());
    }

    @Test
    void testGetCustomerByIdThrowCustomerNotFoundException() {
        String id = UUID.randomUUID().toString();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void testGetAllCustomers() {
        // Mock data
        int page = 0;
        int size = 10;
        Customer c1 = Customer.builder().id(UUID.randomUUID().toString()).placeOfBirth("world").sex(Sex.M)
                .firstname("firstname").name("name").cin("123cin321").email("user@gmail.com").nationality("world")
                .creation(LocalDateTime.now()).build();
        Customer c2 = Customer.builder().id(UUID.randomUUID().toString()).placeOfBirth("world").sex(Sex.M)
                .firstname("firstname").name("name").cin("777cin321").email("admin@gmail.com").nationality("world")
                .creation(LocalDateTime.now()).build();
        Page<Customer> customerPage = new PageImpl<>(Arrays.asList(c1, c2));

        // Mock behavior
        when(customerRepository.findAll(PageRequest.of(page, size))).thenReturn(customerPage);
        when(mappers.fromListOfCustomers(customerPage.getContent())).thenReturn(Arrays.asList(
                new CustomerResponseDTO(c1.getId(), c1.getCin(), c1.getFirstname(), c1.getName(), c1.getDateOfBirth(),
                        c1.getPlaceOfBirth(), c1.getNationality(), c1.getSex(), c1.getEmail(), c1.getCreation(), null
                ),
                new CustomerResponseDTO(c2.getId(), c2.getCin(), c2.getFirstname(), c2.getName(), c2.getDateOfBirth(),
                        c2.getPlaceOfBirth(), c2.getNationality(), c2.getSex(), c2.getEmail(), c2.getCreation(), null
                )
        ));

        // Call the method under test
        PageModel<CustomerResponseDTO> responseDTO = customerService.getAllCustomers(page, size);

        // Assertions
        assertNotNull(responseDTO);
        assertEquals(customerPage.getTotalPages(), responseDTO.getTotalPages());
        assertFalse(responseDTO.getContent().isEmpty());
        assertEquals(2,responseDTO.getContent().size());
    }

    @Test
    void testSearchCustomers() {
        // Mock data
        String keyword = "searchKeyword";
        int page = 0;
        int size = 10;
        Customer c1 = Customer.builder().id(UUID.randomUUID().toString()).placeOfBirth("world").sex(Sex.M)
                .firstname("firstname").name("name").cin("123cin321").email("user@gmail.com").nationality("world")
                .creation(LocalDateTime.now()).build();
        Customer c2 = Customer.builder().id(UUID.randomUUID().toString()).placeOfBirth("world").sex(Sex.M)
                .firstname("firstname").name("name").cin("777cin321").email("admin@gmail.com").nationality("world")
                .creation(LocalDateTime.now()).build();
        Page<Customer> customerPage = new PageImpl<>(Arrays.asList(c1, c2));

        // Mock behavior
        when(customerRepository.search("%" + keyword + "%", PageRequest.of(page, size))).thenReturn(customerPage);
        when(mappers.fromListOfCustomers(customerPage.getContent())).thenReturn(Arrays.asList(
                new CustomerResponseDTO(c1.getId(), c1.getCin(), c1.getFirstname(), c1.getName(), c1.getDateOfBirth(),
                        c1.getPlaceOfBirth(), c1.getNationality(), c1.getSex(), c1.getEmail(), c1.getCreation(), null
                ),
                new CustomerResponseDTO(c2.getId(), c2.getCin(), c2.getFirstname(), c2.getName(), c2.getDateOfBirth(),
                        c2.getPlaceOfBirth(), c2.getNationality(), c2.getSex(), c2.getEmail(), c2.getCreation(), null
                )
        ));

        // Call the method under test
        PageModel<CustomerResponseDTO> responseDTO = customerService.searchCustomers(keyword, page, size);

        // Assertions
        assertNotNull(responseDTO);
        assertEquals(customerPage.getTotalPages(), responseDTO.getTotalPages());
        assertEquals(2, responseDTO.getContent().size());
        assertFalse(responseDTO.getContent().isEmpty());
    }

    @Test
    void testDeleteCustomerById() {
        // Mock data
        String customerId = "customerId";

        // Call the method under test
        customerService.deleteCustomerById(customerId);

        // Verify that the method was called with the correct parameter
        verify(customerRepository).deleteById(customerId);
    }
}
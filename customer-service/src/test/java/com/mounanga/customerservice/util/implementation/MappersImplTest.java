package com.mounanga.customerservice.util.implementation;

import com.mounanga.customerservice.dto.CustomerRequestDTO;
import com.mounanga.customerservice.dto.CustomerResponseDTO;
import com.mounanga.customerservice.entity.Customer;
import com.mounanga.customerservice.enums.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MappersImplTest {

    @InjectMocks
    private MappersImpl mappers;

    @BeforeEach
    void setUp() {
        mappers = new MappersImpl();
    }

    @Test
    void testFromCustomerRequestDTO() {
        CustomerRequestDTO dto = new CustomerRequestDTO(
                "cin123", "firstname", "name",
                LocalDate.now(), "world", "world",
                Sex.M, "name@email.com"
        );
        Customer customer = mappers.fromCustomerRequestDTO(dto);
        assertNotNull(customer);
        assertEquals(dto.cin(), customer.getCin());
        assertEquals(dto.firstname(), customer.getFirstname());
        assertEquals(dto.name(), customer.getName());
        assertEquals(dto.dateOfBirth(), customer.getDateOfBirth());
        assertEquals(dto.placeOfBirth(), customer.getPlaceOfBirth());
        assertEquals(dto.nationality(), customer.getNationality());
        assertEquals(dto.sex(), customer.getSex());
        assertEquals(dto.email(), customer.getEmail());
    }

    @Test
    void testFromCustomer() {
        Customer customer = Customer.builder().sex(Sex.M).cin("123cin321").name("john").firstname("doe")
                .email("john.doe@mail.com").placeOfBirth("world").nationality("world")
                .dateOfBirth(LocalDate.now()).creation(LocalDateTime.now())
                .id(UUID.randomUUID().toString()).lastUpdated(LocalDateTime.now())
                .build();

        CustomerResponseDTO dto = mappers.fromCustomer(customer);
        assertNotNull(dto);
        assertEquals(customer.getId(), dto.id());
        assertEquals(customer.getCin(), dto.cin());
        assertEquals(customer.getFirstname(), dto.firstname());
        assertEquals(customer.getName(), dto.name());
        assertEquals(customer.getDateOfBirth(), dto.dateOfBirth());
        assertEquals(customer.getPlaceOfBirth(), dto.placeOfBirth());
        assertEquals(customer.getNationality(), dto.nationality());
        assertEquals(customer.getSex(), dto.sex());
        assertEquals(customer.getEmail(), dto.email());
        assertEquals(customer.getCreation(), dto.creation());
        assertEquals(customer.getLastUpdated(), dto.lastUpdated());
    }


    @Test
    void testFromListOfCustomers() {
        Customer customer1 = Customer.builder().sex(Sex.M).cin("123cin321").name("john").firstname("doe")
                .email("john.doe@mail.com").placeOfBirth("world").nationality("world")
                .dateOfBirth(LocalDate.now()).creation(LocalDateTime.now())
                .id(UUID.randomUUID().toString()).lastUpdated(LocalDateTime.now())
                .build();
        Customer customer2 = Customer.builder().sex(Sex.M).cin("888cin777").name("marvin").firstname("doe")
                .email("marvin.doe@mail.com").placeOfBirth("world").nationality("world")
                .dateOfBirth(LocalDate.now()).creation(LocalDateTime.now())
                .id(UUID.randomUUID().toString()).lastUpdated(LocalDateTime.now())
                .build();

        List<Customer> customers = List.of(customer1, customer2);

        List<CustomerResponseDTO> responseDTOS = mappers.fromListOfCustomers(customers);

        assertNotNull(responseDTOS);
        assertFalse(responseDTOS.isEmpty());
        assertEquals(2, responseDTOS.size());
    }
}
package com.horseapp.controller;

import com.horseapp.service.AuthenticationService;
import com.horseapp.service.AuthorizationService;
import com.horseapp.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private AuthorizationService authorizationService;

    @Test
    public void testGetCurrentCustomer_Unauthorized() throws Exception {
        mockMvc.perform(get("/customer"))
                .andExpect(status().isUnauthorized());
    }
}

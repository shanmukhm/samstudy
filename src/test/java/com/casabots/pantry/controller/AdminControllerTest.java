/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Admin;
import com.casabots.pantry.repository.AdminRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTest {

    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminController adminController = new AdminController();
    private MockMvc controller;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void shouldCreateAdmin() throws Exception {
        Admin admin = new Admin();
        admin.setAdminId("testadmin");
        admin.setPassword("password");

        when(adminRepository.insert(any(Admin.class)))
                .thenReturn(admin);

        controller.perform(post("/admin/create")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(admin)));

        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository).insert(captor.capture());
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/admin-request.json")) {
            return IOUtils.toString(in);
        }
    }
}

/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.User;
import com.casabots.pantry.repository.UserRepository;
import com.casabots.pantry.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController = new UserController();

    private MockMvc controller;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldGetUser() throws Exception {
        User user = new User();
        user.setFirstName("testuser");
        user.setEmail("test@abc.com");
        user.setDob(new Date());
        user.setUserId("testID");
        when(userService.get(eq("testID"))).thenReturn(user);

        controller.perform(get("/users/{userId}", "testID"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(user)));
    }

    @Test
    public void shouldCreateUser() throws Exception {
        controller.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).create(captor.capture());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        when(userService.exists(eq("testuser"))).thenReturn(true);
        controller.perform(post("/users/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userService).update(captor.capture());
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        controller.perform(delete("/users/{userId}", "testuser"))
                .andExpect(status().is(HttpStatus.OK.value()));

        verify(userService).remove("testuser");
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/user-request.json")) {

            return IOUtils.toString(in);
        }
    }
}

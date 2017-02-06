/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.PendingOrder;
import com.casabots.pantry.model.Portion;
import com.casabots.pantry.repository.PendingOrderRepository;
import com.casabots.pantry.repository.SaladRepository;
import com.casabots.pantry.repository.SallyRepository;
import com.casabots.pantry.security.CustomMatcher;
import com.casabots.pantry.security.PantryRealm;
import com.casabots.pantry.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.Subject;
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
import security.TestRealm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;
    @Mock
    private SallyRepository sallyRepository;
    @Mock
    private SaladRepository saladRepository;
    @Mock
    private PendingOrderRepository pendingOrderRepository;
    private TestRealm realm = new TestRealm();
    @Mock
    private CustomMatcher customMatcher;
    private DefaultSecurityManager securityManager = new DefaultSecurityManager();
    @InjectMocks
    private OrderController orderController = new OrderController();
    private MockMvc controller;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(orderController).build();
        realm.setCredentialsMatcher(customMatcher);
        securityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(securityManager);

        when(customMatcher.doCredentialsMatch(any(), any()))
                .thenReturn(true);
    }

    @Test
    public void shouldPlaceOrder() throws Exception {
        // Place order for an existing salad
        Portion portion = new Portion();
        portion.setIngredientId("testingredient");
        portion.setSizeId("large");

        List<String> portions = new ArrayList<>();
        portions.add("portion1");
        portions.add("portion2");

        PendingOrder saladOrder = new PendingOrder();
        saladOrder.setOrderId("testorder");
        saladOrder.setUserId("testuser");
        saladOrder.setSallyId("testsally");
        saladOrder.setSaladId("testsalad");

        loginUser("testuser", "password");

        when(sallyRepository.exists(anyString())).thenReturn(true);
        when(saladRepository.exists(anyString())).thenReturn(true);
        when(pendingOrderRepository.findByUserId(eq("testuser"))).thenReturn(null);
        when(orderService.placeOrder(any(PendingOrder.class)))
                .thenReturn(saladOrder);

        controller.perform(post("/orders/place-order")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(requestJsonForSaladOrder().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(saladOrder)));

        //Place order for a custom salad

        PendingOrder customSaladOrder = new PendingOrder();
        customSaladOrder.setOrderId("testorder");
        customSaladOrder.setSallyId("testsally");
        customSaladOrder.setUserId("testuser");

        when(orderService.placeOrder(any(PendingOrder.class)))
                .thenReturn(customSaladOrder);

        controller.perform(post("/orders/place-order")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJsonForCustomSaladOrder().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(customSaladOrder)));

        ArgumentCaptor<PendingOrder> captor = ArgumentCaptor.forClass(PendingOrder.class);
        verify(orderService, times(2)).placeOrder(captor.capture());
    }

    private String requestJsonForSaladOrder() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/salad-order-request.json")) {

            return IOUtils.toString(in);
        }
    }

    private String requestJsonForCustomSaladOrder() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/custom-salad-order-request.json")) {

            return IOUtils.toString(in);
        }
    }

    private void loginUser(String userName, String password) {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        currentUser.login(token);
    }
}

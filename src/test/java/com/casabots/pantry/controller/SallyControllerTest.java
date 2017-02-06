/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.*;
import com.casabots.pantry.security.CustomMatcher;
import com.casabots.pantry.service.OwnerService;
import com.casabots.pantry.service.SallyService;
import com.casabots.pantry.util.SessionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import security.TestRealm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.casabots.pantry.util.Constants.Roles.SALLY_ROLE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class SallyControllerTest {

    @Mock
    private SallyService sallyService;

    @InjectMocks
    private SallyController sallyController = new SallyController();

    @Mock
    private OwnerService ownerService;

    @Mock
    private CustomMatcher customMatcher;

    private TestRealm realm = new TestRealm();

    private DefaultSecurityManager securityManager = new DefaultSecurityManager();

    private MockMvc controller;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(sallyController).build();
        realm.setCredentialsMatcher(customMatcher);
        securityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(securityManager);

        when(customMatcher.doCredentialsMatch(any(), any()))
                .thenReturn(true);
    }

    @Test
    public void shouldGetSally() throws Exception {
        Sally sally = returnSally();

        when(sallyService.get(eq("testsally"))).thenReturn(sally);

        controller.perform(get("/sally/{sallyId}", "testsally"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(sally)));
    }

    @Test
    public void shouldCreateSally() throws Exception {
        controller.perform(post("/sally")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<Sally> captor = ArgumentCaptor.forClass(Sally.class);
        verify(sallyService).create(captor.capture());
    }

    @Test
    public void shouldUpdateSally() throws Exception {
        when(sallyService.exists(eq("testsally"))).thenReturn(true);
        controller.perform(post("/sally/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<Sally> captor = ArgumentCaptor.forClass(Sally.class);
        verify(sallyService).update(captor.capture());
    }

    @Test
    public void shouldDeleteSally() throws Exception {
        controller.perform(delete("/sally/{sallyId}", "testsally"))
                .andExpect(status().is(HttpStatus.OK.value()));

        verify(sallyService).remove("testsally");
    }

    @Test
    public void shouldGetSaladsOfSally() throws Exception {
        Salad salad1 = new Salad();
        salad1.setSaladId("testsalad1");
        salad1.setName("Test Salad1");
        salad1.setAvailable(true);
        salad1.setImageUrl("testurl");

        Salad salad2 = new Salad();
        salad2.setSaladId("testsalad2");
        salad2.setName("Test Salad2");
        salad2.setAvailable(true);
        salad2.setImageUrl("testurl");

        Salad salad3 = new Salad();
        salad3.setSaladId("testsalad3");
        salad3.setName("Test Salad3");
        salad3.setAvailable(true);
        salad3.setImageUrl("testurl");

        List<Salad> saladList = new ArrayList<>();
        saladList.add(salad1);
        saladList.add(salad2);
        saladList.add(salad3);

        when(sallyService.getSalads(eq("testsally")))
                .thenReturn(saladList);

        controller.perform(get("/sally/{sallyId}/salads", "testsally"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(saladList)));
    }

    @Test
    public void shouldGetIngredientsOfSally() throws Exception {
        Ingredient ing1 = new Ingredient();
        ing1.setIngredientId("test-ing1");
        ing1.setName("Test Ingredient1");
        ing1.setCategory("test category");

        Ingredient ing2 = new Ingredient();
        ing2.setIngredientId("test-ing2");
        ing2.setName("Test Ingredient2");
        ing2.setCategory("test category");

        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(ing1);
        ingredientList.add(ing2);

        when(sallyService.getIngredients(eq("testsally")))
                .thenReturn(ingredientList);

        controller.perform(get("/sally/{sallyId}/ingredients", "testsally"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(ingredientList)));
    }

    @Test
    public void shouldGetPendingOrder() throws Exception {
        Portion portion = new Portion();
        portion.setSizeId("large");

        Order saladOrder = new Order();
        saladOrder.setOrderId("testorder");
        saladOrder.setSallyId("testsally");
        saladOrder.setUserId("testuser");
        saladOrder.setSaladId("testsalad");
        saladOrder.setCustomSalad(false);

        when(sallyService.getPendingOrder(eq("testsally")))
                .thenReturn(saladOrder);

        SessionUtils.loginUser("testsally", null, SALLY_ROLE, null, new MockHttpServletResponse());
        controller.perform(get("/sally/pending-order"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(saladOrder)));
        SessionUtils.logoutCurrentUser();
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/sally-request.json")) {

            return IOUtils.toString(in);
        }
    }

    private Sally returnSally() throws JsonProcessingException {
        List<ItemStatus> salads = new ArrayList<>();
        salads.add(new ItemStatus("salad1", true, 0));
        salads.add(new ItemStatus("salad2", true, 0));
        List<ItemStatus> ingredients = new ArrayList<>();
        ingredients.add(new ItemStatus("ingredient1", true, 0));
        ingredients.add(new ItemStatus("ingredient2", true, 0));
        Sally sally = new Sally();
        sally.setSallyId("testsally");
        sally.setOwnerId("testowner");
        sally.setSalads(salads);
        sally.setIngredients(ingredients);
        sally.setName("Test Sally");
        sally.setLocation(new GeoLocation(3.5, 78.9));

        return sally;
    }

    private Owner returnOwner() {
        Owner owner = new Owner();
        owner.setOwnerId("testowner");
        owner.setFirstName("Test Owner");
        owner.setLastName("Last");
        owner.setEmail("testowner@abc.com");
        owner.setPassword("password");
        owner.setDob("1990-01-01");
        owner.setGender('M');
        owner.setLocation(new GeoLocation(0.0, 00.00));

        return owner;
    }

    private void loginOwner(Owner owner) {
        when(ownerService.get(owner.getOwnerId())).thenReturn(owner);
        Subject currentSally = SecurityUtils.getSubject();
        currentSally.logout();
        UsernamePasswordToken token = new UsernamePasswordToken(owner.getOwnerId(), owner.getPassword());
        currentSally.login(token);
    }
}

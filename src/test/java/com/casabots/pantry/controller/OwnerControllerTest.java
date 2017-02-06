/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.GeoLocation;
import com.casabots.pantry.model.ItemStatus;
import com.casabots.pantry.model.Owner;
import com.casabots.pantry.model.Sally;
import com.casabots.pantry.security.CustomMatcher;
import com.casabots.pantry.service.OwnerService;
import com.casabots.pantry.util.SessionUtils;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import security.TestRealm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class OwnerControllerTest {

    @Mock
    private OwnerService ownerService;

    private TestRealm realm = new TestRealm();

    @Mock
    private CustomMatcher customMatcher;

    private DefaultSecurityManager securityManager = new DefaultSecurityManager();

    @InjectMocks
    private OwnerController ownerController = new OwnerController();

    private MockMvc controller;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(ownerController).build();
        realm.setCredentialsMatcher(customMatcher);
        securityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(securityManager);

        when(customMatcher.doCredentialsMatch(any(), any()))
                .thenReturn(true);
    }

    @Test
    public void shouldGetCurrentOwner() throws Exception {
        Owner owner = new Owner();
        owner.setFirstName("Test Owner");
        owner.setEmail("test@abc.com");
        owner.setDob("testDate");
        owner.setOwnerId("testowner");
        owner.setPassword("password");

        when(ownerService.get(eq("testowner"))).thenReturn(owner);
        loginOwner(owner);
        controller.perform(get("/owners/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(owner)));
        SessionUtils.logoutCurrentUser();
    }

    @Test
    public void shouldCreateOwner() throws Exception {
        Owner owner = new Owner();
        owner.setOwnerId("testowner");
        owner.setFirstName("Test Owner");
        owner.setLastName("Last");
        owner.setEmail("testowner@abc.com");
        owner.setPassword("password");
        owner.setDob("1990-01-01");
        owner.setGender('M');
        owner.setLocation(new GeoLocation(0.0, 00.00));

        when(ownerService.create(any(Owner.class))).thenReturn(owner);

        controller.perform(post("/owners")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(owner)))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        verify(ownerService).create(captor.capture());
    }

    @Test
    public void shouldUpdateCurrentOwner() throws Exception {
        Owner owner = returnOwner();

        loginOwner(owner);
        when(ownerService.update(any(Owner.class))).thenReturn(owner);
        controller.perform(post("/owners/update-current")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(owner)))
                .andExpect(status().is(HttpStatus.OK.value()));

        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        verify(ownerService).update(captor.capture());
        SessionUtils.logoutCurrentUser();
    }

    @Test
    public void shouldDeleteCurrentOwner() throws Exception {
        Owner owner = returnOwner();
        loginOwner(owner);

        controller.perform(delete("/owners/delete"))
                .andExpect(status().is(HttpStatus.OK.value()));

        verify(ownerService).remove("testowner");
    }

    @Test
    public void shouldGetOwner() throws Exception {
        Owner owner = new Owner();
        owner.setFirstName("Test Owner");
        owner.setEmail("test@abc.com");
        owner.setDob("testDate");
        owner.setOwnerId("testID");
        owner.setPassword("password");
        when(ownerService.get(eq("testID"))).thenReturn(owner);

        controller.perform(get("/owners/{ownerId}", "testID"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(owner)));
    }

    @Test
    public void shouldUpdateOwner() throws Exception {
        Owner owner = returnOwner();

        when(ownerService.update(any(Owner.class))).thenReturn(owner);

        controller.perform(post("/owners/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(owner)));

        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        verify(ownerService).update(captor.capture());
    }

    @Test
    public void shouldDeleteOwner() throws Exception {
        controller.perform(delete("/owners/{ownerId}", "testowner"))
                .andExpect(status().is(HttpStatus.OK.value()));

        verify(ownerService).remove("testowner");
    }

    @Test
    public void shouldGetSallyDevices() throws Exception {
        List<Sally> sallyList = returnSallyDevices();
        Owner owner = returnOwner();
        when(ownerService.getSallyDevices(eq("testowner"))).thenReturn(sallyList);

        loginOwner(owner);

        controller.perform(get("/owners/sally-devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(sallyList)));
        SessionUtils.logoutCurrentUser();
    }

    @Test
    public void shouldAddSaladsToSally() throws Exception {
        String[] saladsToAdd = {"new-salad1", "new-salad2"};
        List<ItemStatus> salads = new ArrayList<>();
        salads.add(new ItemStatus("salad1"));
        salads.add(new ItemStatus("salad2"));
        when(ownerService.addSaladsToSally(eq("testsally1"), eq(saladsToAdd))).thenReturn(salads);

        controller.perform(post("/owners/add-salads/{sallyId}", "testsally1").param("salads", saladsToAdd))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(salads)));
    }

    @Test
    public void shouldAddIngredientsToSally() throws Exception {
        String[] ingredientsToAdd = {"new-ingredient1", "new-ingredient2"};
        List<ItemStatus> ingredients = new ArrayList<>();
        ingredients.add(new ItemStatus("ingredient1"));

        when(ownerService.addIngredientsToSally(eq("testsally1"), eq(ingredientsToAdd))).thenReturn(ingredients);

        controller.perform(post("/owners/add-ingredients/{sallyId}", "testsally1").param("ingredients", ingredientsToAdd))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(ingredients)));
    }

    @Test
    public void shouldRemoveIngredientsFromSally() throws Exception {
        String[] ingredientsToRemove = {"ingredient3", "ingredient4"};
        List<ItemStatus> ingredients = new ArrayList<>();
        ingredients.add(new ItemStatus("ingredient1"));
        ingredients.add(new ItemStatus("ingredient2"));

        when(ownerService.removeIngredientsFromSally(eq("testsally1"), eq(ingredientsToRemove))).thenReturn(ingredients);

        controller.perform(post("/owners/remove-ingredients/{sallyId}", "testsally1").param("ingredients", ingredientsToRemove))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(ingredients)));
    }

    @Test
    public void shouldRemoveSaladsFromSally() throws Exception {
        String[] saladsToRemove = {"salad3", "salad4"};
        List<ItemStatus> salads = new ArrayList<>();
        salads.add(new ItemStatus("salad1"));
        salads.add(new ItemStatus("salad2"));

        when(ownerService.removeSaladsFromSally(eq("testsally1"), eq(saladsToRemove))).thenReturn(salads);

        controller.perform(post("/owners/remove-salads/{sallyId}", "testsally1").param("salads", saladsToRemove))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(salads)));
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/owner-request.json")) {

            return IOUtils.toString(in);
        }
    }

    private void loginOwner(Owner owner) {
        when(ownerService.get(owner.getOwnerId())).thenReturn(owner);
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        UsernamePasswordToken token = new UsernamePasswordToken(owner.getOwnerId(), owner.getPassword());
        currentUser.login(token);
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

    private List<Sally> returnSallyDevices() {
        List<Sally> sallyList = new ArrayList<>();
        List<ItemStatus> salads = new ArrayList<>();
        ItemStatus[] saladArray = {new ItemStatus("salad1"), new ItemStatus("salad2"), new ItemStatus("salad3")};
        salads.addAll(Arrays.asList(saladArray));

        List<ItemStatus> ingredients = new ArrayList<>();
        ItemStatus[] ingredientArray = {new ItemStatus("ingredient1"), new ItemStatus("ingredient2"), new ItemStatus("ingredient3")};
        ingredients.addAll(Arrays.asList(ingredientArray));

        Sally sally1 = new Sally();
        sally1.setSallyId("testsally1");
        sally1.setName("Sally 1");
        sally1.setOwnerId("testowner");
        sally1.setSalads(salads);
        sally1.setIngredients(ingredients);
        sally1.setLocation(new GeoLocation(0.0, 0.0));

        Sally sally2 = new Sally();
        sally2.setSallyId("testsally2");
        sally2.setName("Sally 2");
        sally2.setOwnerId("testowner");
        sally2.setSalads(salads);
        sally2.setIngredients(ingredients);
        sally2.setLocation(new GeoLocation(1.0, 3.0));

        sallyList.add(sally1);
        sallyList.add(sally2);

        return sallyList;
    }
}

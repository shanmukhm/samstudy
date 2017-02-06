/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Ingredient;
import com.casabots.pantry.repository.IngredientRepository;
import com.casabots.pantry.service.IngredientService;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class IngredientControllerTest {
    
    @Mock
    private IngredientService ingredientService;
    @InjectMocks
    private IngredientController ingredientController = new IngredientController();
    private MockMvc controller;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void shouldCreateIngredient() throws Exception {
        Ingredient ingredient = getIngredient();

        when(ingredientService.create(any(Ingredient.class)))
                .thenReturn(ingredient);

        controller.perform(post("/ingredients")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(ingredient)));

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientService).create(captor.capture());
    }

    @Test
    public void shouldGetIngredient() throws Exception {
        when(ingredientService.get(eq("testingredient")))
                .thenReturn(getIngredient());

        controller.perform(get("/ingredients/{ingredientId}", "testingredient"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(getIngredient())));
    }

    @Test
    public void shouldUpdateIngredient() throws Exception {
        Ingredient ingredient = getIngredient();

        when(ingredientService.update(any(Ingredient.class)))
                .thenReturn(ingredient);

        controller.perform(post("/ingredients/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(ingredient)));

        ArgumentCaptor<Ingredient> captor = ArgumentCaptor.forClass(Ingredient.class);
        verify(ingredientService).update(captor.capture());
    }

    @Test
    public void shouldDeleteIngredient() throws Exception {
        controller.perform(delete("/ingredients/{ingredientId}", "testingredient"))
                .andExpect(status().isOk());

        verify(ingredientService).delete(eq("testingredient"));
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/ingredient-request.json")) {

            return IOUtils.toString(in);
        }
    }

    private Ingredient getIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId("testingredient");
        ingredient.setName("Test Ingredient");
        ingredient.setImageUrl("testurl");
        ingredient.setAvailable(true);

        return ingredient;
    }
}

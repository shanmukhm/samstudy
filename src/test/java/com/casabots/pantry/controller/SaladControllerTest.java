/*******************************************************************************
 * Copyright (c) CASABOTS 2016
 * All Rights Reserved
 ******************************************************************************/

package com.casabots.pantry.controller;

import com.casabots.pantry.model.Salad;
import com.casabots.pantry.repository.PortionRepository;
import com.casabots.pantry.repository.SaladRepository;
import com.casabots.pantry.service.SaladService;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class SaladControllerTest {

    @Mock
    private SaladService saladService;
    @InjectMocks
    private SaladController saladController = new SaladController();
    private MockMvc controller;

    @Before
    public void setUp() {
        controller = MockMvcBuilders.standaloneSetup(saladController).build();
    }

    @Test
    public void shouldCreateSalad() throws Exception {
        Salad salad = getSalad();

        when(saladService.create(any(Salad.class)))
                .thenReturn(salad);

        controller.perform(post("/salads")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(salad)));

        ArgumentCaptor<Salad> captor = ArgumentCaptor.forClass(Salad.class);
        verify(saladService).create(captor.capture());
    }

    @Test
    public void shouldGetSalad() throws Exception {
        when(saladService.get(eq("testsalad")))
                .thenReturn(getSalad());

        controller.perform(get("/salads/{saladId}", "testsalad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(getSalad())));
    }

    @Test
    public void shouldUpdateSalad() throws Exception {
        Salad salad = getSalad();

        when(saladService.update(any(Salad.class)))
                .thenReturn(getSalad());

        controller.perform(post("/salads/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(requestJson().getBytes(Charset.forName("UTF-8"))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(new ObjectMapper().writeValueAsString(salad)));

        ArgumentCaptor<Salad> captor = ArgumentCaptor.forClass(Salad.class);
        verify(saladService).update(captor.capture());
    }

    @Test
    public void shouldDeleteSalad() throws Exception {
        controller.perform(delete("/salads/{saladId}", "testsalad"))
                .andExpect(status().isOk());

        verify(saladService).delete(eq("testsalad"));
    }

    private String requestJson() throws IOException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("json/controller/salad-request.json")) {

            return IOUtils.toString(in);
        }
    }

    private Salad getSalad() {
        Salad salad = new Salad();
        salad.setSaladId("testsalad");
        salad.setName("Test Portion");
        salad.setImageUrl("testurl");
        salad.setAvailable(true);
        List<String> portions = new ArrayList<>();
        portions.add("portion1");
        portions.add("portion2");
        salad.setPortions(portions);
        return salad;
    }
}

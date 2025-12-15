package com.kadri.portfolio_service.controller;

import com.kadri.portfolio_service.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PortfolioController.class)
public class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PortfolioService portfolioService;

    @Test
    void getHoldingsReturn200() throws Exception {
        Mockito.when(portfolioService.getHoldingsForUser(anyLong()))
                .thenReturn(List.of());
        mockMvc.perform(get("/api/v1/portfolio/1/holdings"))
                .andExpect(status().isOk());
    }
}

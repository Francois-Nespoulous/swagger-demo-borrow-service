package com.example.borrow.e2e;

import com.example.borrow.dto.out.BorrowDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BorrowControllerE2ETest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void borrowBook() throws Exception {
        String user = "user2";
        String userId = "33333333-1111-1111-1111-111111111102";
        String bookInstanceIdToBorrow = "22222222-0000-0000-0000-000000000004";

        Cookie jwtCookie = this.login(user);

        //check user before borrow
        mockMvc.perform(get("/api/v1/logged-user")
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbOfBooksBorrowed").value("1"));

        //borrow
        MvcResult borrowResult = mockMvc.perform(post("/api/v1/borrow/" + bookInstanceIdToBorrow)
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookInstanceDto.id").value(bookInstanceIdToBorrow))
                .andExpect(jsonPath("$.userDto.id").value(userId))
                .andExpect(jsonPath("$.borrowDate").isNotEmpty())
                .andExpect(jsonPath("$.returnDate").isEmpty())
                .andReturn();

        BorrowDto borrowFromResponse = mapper.readValue(borrowResult.getResponse().getContentAsString(), BorrowDto.class);
        LocalDate borrowDateFromResponse = borrowFromResponse.borrowDate().toLocalDate();
        LocalDate today = LocalDate.now();

        Assertions.assertEquals(today, borrowDateFromResponse);

        //check user after borrow
        mockMvc.perform(get("/api/v1/logged-user")
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbOfBooksBorrowed").value("2"));
    }

    @Test
    void returnBook() throws Exception {
        String user = "user2";
        String userId = "33333333-1111-1111-1111-111111111102";
        String bookInstanceIdToReturn = "22222222-0000-0000-0000-000000000002";
        String borrowId = "44444444-1111-1111-1111-111111111002";
        LocalDate borrowDateFromDB = LocalDate.of(2025, 6, 1);

        Cookie jwtCookie = this.login(user);

        //check user before return
        mockMvc.perform(get("/api/v1/logged-user")
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbOfBooksBorrowed").value("1"));

        //return
        MvcResult borrowResult = mockMvc.perform(post("/api/v1/return/" + borrowId)
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(borrowId))
                .andExpect(jsonPath("$.bookInstanceDto.id").value(bookInstanceIdToReturn))
                .andExpect(jsonPath("$.userDto.id").value(userId))
                .andExpect(jsonPath("$.borrowDate").isNotEmpty())
                .andExpect(jsonPath("$.returnDate").isNotEmpty())
                .andReturn();

        BorrowDto borrowFromResponse = mapper.readValue(borrowResult.getResponse().getContentAsString(), BorrowDto.class);
        LocalDate borrowDateFromResponse = borrowFromResponse.borrowDate().toLocalDate();
        LocalDate returnDateFromResponse = borrowFromResponse.returnDate().toLocalDate();
        LocalDate today = LocalDate.now();

        Assertions.assertEquals(today, returnDateFromResponse);
        Assertions.assertEquals(borrowDateFromDB, borrowDateFromResponse);

        //check user after return
        mockMvc.perform(get("/api/v1/logged-user")
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nbOfBooksBorrowed").value("0"));
    }


    private Cookie login(String user) throws Exception {
        String userRequest = String.format("""
                {
                  "username": "%s",
                  "password": "%s"
                }""", user, user);
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequest))
                .andExpect(status().isOk())
                .andReturn();

        return loginResult.getResponse().getCookie("jwt");
    }
}

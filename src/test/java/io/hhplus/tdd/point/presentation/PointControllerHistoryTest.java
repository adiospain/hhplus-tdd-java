package io.hhplus.tdd.point.presentation;

import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.domain.TransactionType;
import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PointController.class)
@DisplayName("포인트 내역 조회")
public class PointControllerHistoryTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    void printResponse(MockHttpServletResponse response) throws UnsupportedEncodingException {
        logger.info("Status = {}", response.getStatus());
        logger.info("Error message = {}", response.getErrorMessage());
        logger.info("Headers = {}", response.getHeaderNames().stream()
                .collect(Collectors.toMap(h -> h, response::getHeader)));
        logger.info("Content type = {}", response.getContentType());
        logger.info("Body = {}", response.getContentAsString());
        logger.info("Forwarded URL = {}", response.getForwardedUrl());
        logger.info("Redirected URL = {}", response.getRedirectedUrl());
        logger.info("Cookies = {}", Arrays.toString(response.getCookies()));
    }

    @Test
    @DisplayName("포인트 내역이 없는 사용자의 내역을 조회 한다")
    void history() throws Exception {
        //given : 포인트 내역이 없는 사용자
        long userId = 5L;


        //when : pointService의 모의 행동 설정
        when(pointService.history(userId)).thenReturn(List.of());

        //when : mockMvc를 사용하여 API 엔드포인트를 호출하고 결과를 가져옴
        MvcResult result = mockMvc.perform(get("/point/{userId}/histories", userId))

        //then : 응답값 검증
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                        .andReturn();

        //then : 메서드가 호출되었는지 확인
        verify(pointService).history(userId);

        //then : 응답 출력
        printResponse(result.getResponse());


    }
}
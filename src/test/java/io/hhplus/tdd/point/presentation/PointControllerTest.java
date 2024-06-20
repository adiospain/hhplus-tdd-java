package io.hhplus.tdd.point.presentation;

import io.hhplus.tdd.point.application.PointService;
import io.hhplus.tdd.point.dto.UserPoint;
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
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PointController.class)
public class PointControllerTest {

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
    void point() throws Exception {
        //given :
        long id = 5L;
        long point = 2000L;
        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        when(pointService.point(id)).thenReturn(userPoint);

        //when : originally tried this but returned Status code 400
//        ResultActions resultActionsResponse = mockMvc.perform(patch("/point/{id}/charge", id));
//        resultActionsResponse.andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.point").value(0 + point));

        //when :
        MvcResult result = mockMvc.perform(get("/point/{id}", id)).andReturn();

        int a =0;

        //then : 응답 출력
        printResponse(result.getResponse());


        //then : 필드값 검증
        mockMvc.perform(get("/point/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.point").value(point));
    }






}

package io.hhplus.tdd.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PointController.class)
public class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @Test
    @DisplayName("(포인트가 0원인 사용자가) 포인트를 충전한다")
    void charge() throws Exception {
        //given : 특정 사용자와 추가할 포인트 금액
        long id = 5L;
        long amount = 3000L;
        UserPoint userPoint = new UserPoint(id, 0 + amount, System.currentTimeMillis());

        //when : pointService의 모의 동작 설정
        when(pointService.charge(id, amount)).thenReturn(userPoint);

        //when : mockMvc를 사용하여 API 엔드포인트를 호출하고 결과를 가져옴
        ResultActions response = mockMvc.perform(patch("/point/{id}/charge", id)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(String.valueOf(amount)));

        //then : HTTP 상태가 ok이고 반환된 JSON에서 id와 point가 예상 값과 일치하는지 확인
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.point").value(0 + amount));
        //then : 메서드가 호출되었는지 확인
        verify(pointService).charge(id, amount);

        //then : 추가적으로 JSON 응답을 검증하기 위해 문자열로 변환
        MvcResult mvcResult = response.andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        //then : ObjectMapper로 JSON 응답 역직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        UserPoint actualUserPoint = objectMapper.readValue(jsonResponse, UserPoint.class);

        //then : 필드값 검증
        assertEquals(id, actualUserPoint.id());
        assertEquals(0+ amount, actualUserPoint.point());
    }

    @Test
    @DisplayName("(포인트가 3000인 사용자가) 포인트를 사용한다")
    void usingPointWhoHasZero() throws Exception {
        //given : 특정 사용자와 사용할 포인트 금액
        long id = 4L;
        long amount = 2000L;
        UserPoint userPoint = new UserPoint(id, 3000L - amount, System.currentTimeMillis());

        //when : mockMvc를 사용하여 API 엔드포인트를 호출하고 결과를 가져옴
        when(pointService.use(id, amount)).thenReturn(userPoint);
        //when(pointService.use(id, amount)).thenThrow(new PointException(ErrorCode.NOT_ENOUGH_POINTS)); //Why not 409 code, but 500

        //when : mockMvc를 사용하여 API 엔드포인트를 호출하고 결과를 가져옴
        ResultActions response = mockMvc.perform(patch("/point/{id}/use", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(amount)));

        //then : HTTP 상태가 ok이고 반환된 JSON에서 id와 point가 예상 값과 일치하는지 확인
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.point").value(3000L - amount));
        //then : 메서드가 호출되었는지 확인
        verify(pointService).use(id,amount);

        //then : 추가적으로 JSON 응답을 검증하기 위해 문자열로 변환
        MvcResult mvcResult = response.andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();

        //then : ObjectMapper로 JSON 응답 역직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        UserPoint actualUserPoint = objectMapper.readValue(jsonResponse, UserPoint.class);

        //then : 필드값 검증
        assertEquals(id, actualUserPoint.id());
        assertEquals(3000L - amount, actualUserPoint.point());
    }
}

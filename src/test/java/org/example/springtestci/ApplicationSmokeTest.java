package org.example.springtestci;

import org.example.springtestci.ui.FlowerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("애플리케이션 스모크 테스트는")
class ApplicationSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("꽃 등록부터 목록/개수 조회까지 전체 흐름이 정상 동작한다")
    void it_registers_and_retrieves_flower_end_to_end() throws Exception {
        // given: 등록할 꽃 요청 DTO 준비
        FlowerDto request = new FlowerDto("장미", "빨강", 5000);

        // when & then: 등록 API가 저장된 꽃 정보를 응답해야 함
        mockMvc.perform(post("/api/flowers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("장미"))
                .andExpect(jsonPath("$.color").value("빨강"))
                .andExpect(jsonPath("$.price").value(5000));

        // when & then: 목록 조회 API에 등록한 꽃이 포함되어야 함
        mockMvc.perform(get("/api/flowers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("장미"));

        // when & then: 개수 조회 API가 실제 저장 개수를 반환해야 함
        mockMvc.perform(get("/api/flowers/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }
}

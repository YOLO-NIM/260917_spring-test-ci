package org.example.springtestci.ui;

import tools.jackson.databind.ObjectMapper;
import org.example.springtestci.app.FlowerUseCase;
import org.example.springtestci.domain.Flower;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowerApiController는")
class FlowerApiControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FlowerUseCase flowerUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // given: 모킹된 유스케이스로 컨트롤러 준비
        mockMvc = MockMvcBuilders.standaloneSetup(new FlowerApiController(flowerUseCase)).build();
    }

    @Nested
    @DisplayName("GET /api/flowers/count는")
    class Describe_count {

        @Test
        @DisplayName("전체 꽃 개수를 응답한다")
        void it_responds_with_flower_count() throws Exception {
            // given: 유스케이스 개수를 3으로 설정
            given(flowerUseCase.count()).willReturn(3L);

            // when & then: 개수 응답 검증
            mockMvc.perform(get("/api/flowers/count"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("3"));
        }
    }

    @Nested
    @DisplayName("POST /api/flowers는")
    class Describe_save {

        @Test
        @DisplayName("요청받은 꽃 정보를 저장하고 응답한다")
        void it_saves_and_returns_flower() throws Exception {
            // given: 저장 요청 DTO와 저장 결과 준비
            FlowerDto request = new FlowerDto("장미", "빨강", 5000);
            Flower savedFlower = new Flower("장미", "빨강", 5000);
            given(flowerUseCase.save(savedFlower)).willReturn(savedFlower);

            // when & then: 저장된 꽃 정보 응답 검증
            mockMvc.perform(post("/api/flowers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("장미"))
                    .andExpect(jsonPath("$.color").value("빨강"))
                    .andExpect(jsonPath("$.price").value(5000));
        }
    }

    @Nested
    @DisplayName("GET /api/flowers는")
    class Describe_findAll {

        @Test
        @DisplayName("전체 꽃 목록을 응답한다")
        void it_responds_with_all_flowers() throws Exception {
            // given: 유스케이스가 꽃 목록을 반환하도록 설정
            Flower rose = new Flower("장미", "빨강", 5000);
            Flower tulip = new Flower("튤립", "노랑", 3000);
            given(flowerUseCase.findAll()).willReturn(List.of(rose, tulip));

            // when & then: 목록 응답 검증
            mockMvc.perform(get("/api/flowers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].name").value("장미"))
                    .andExpect(jsonPath("$[1].name").value("튤립"));
        }
    }
}

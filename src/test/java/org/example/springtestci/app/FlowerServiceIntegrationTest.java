package org.example.springtestci.app;

import org.example.springtestci.domain.Flower;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@DisplayName("FlowerService 통합 테스트는")
class FlowerServiceIntegrationTest {

    @Autowired
    private FlowerService flowerService;

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Test
        @DisplayName("실제 저장소에 꽃을 저장하고 반환한다")
        void it_saves_flower_via_real_repository() {
            // given: 실제 DB에 저장할 꽃 준비
            Flower flower = new Flower("장미", "빨강", 5000);

            // when: save 메서드로 실제 저장소에 저장
            Flower saved = flowerService.save(flower);

            // then: 저장 결과가 입력값과 일치해야 함
            assertThat(saved).isEqualTo(flower);
        }
    }

    @Nested
    @DisplayName("findAll과 count는")
    class Describe_findAll_and_count {

        @Test
        @DisplayName("저장한 꽃을 실제 조회 결과에 반영한다")
        void it_reflects_saved_flower_in_query_results() {
            // given: 실제 저장소에 꽃 두 건 저장
            Flower rose = flowerService.save(new Flower("장미", "빨강", 5000));
            Flower tulip = flowerService.save(new Flower("튤립", "노랑", 3000));

            // when: findAll과 count 메서드 호출
            List<Flower> flowers = flowerService.findAll();
            long count = flowerService.count();

            // then: 실제 저장된 내용과 개수가 일치해야 함
            assertThat(flowers).containsExactlyInAnyOrder(rose, tulip);
            assertThat(count).isEqualTo(2L);
        }

        @Test
        @DisplayName("저장된 꽃이 없으면 빈 목록과 0을 반환한다")
        void it_returns_empty_state_when_nothing_saved() {
            // when: 아무것도 저장하지 않고 조회
            List<Flower> flowers = flowerService.findAll();
            long count = flowerService.count();

            // then: 빈 목록과 0이 반환되어야 함
            assertThat(flowers).isEmpty();
            assertThat(count).isZero();
        }
    }
}

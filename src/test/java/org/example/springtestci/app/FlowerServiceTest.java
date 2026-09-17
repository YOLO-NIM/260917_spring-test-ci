package org.example.springtestci.app;

import org.example.springtestci.domain.Flower;
import org.example.springtestci.domain.FlowerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowerService는")
class FlowerServiceTest {

    @Mock
    private FlowerRepository flowerRepository;

    @InjectMocks
    private FlowerService flowerService;

    @Nested
    @DisplayName("count 메서드는")
    class Describe_count {

        @Test
        @DisplayName("저장소의 개수를 그대로 위임하여 반환한다")
        void it_delegates_count_to_repository() {
            // given: 저장소 개수를 2로 설정
            given(flowerRepository.count()).willReturn(2L);

            // when: count 메서드 호출
            long count = flowerService.count();

            // then: 저장소 값과 일치해야 함
            assertThat(count).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Test
        @DisplayName("전달받은 꽃 저장을 저장소에 위임한다")
        void it_delegates_save_to_repository() {
            // given: 저장할 꽃과 저장소 반환값 준비
            Flower flower = new Flower("장미", "빨강", 5000);
            given(flowerRepository.save(flower)).willReturn(flower);

            // when: save 메서드 호출
            Flower saved = flowerService.save(flower);

            // then: 저장소가 반환한 값과 동일해야 함
            assertThat(saved).isEqualTo(flower);
            verify(flowerRepository).save(flower);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class Describe_findAll {

        @Test
        @DisplayName("저장소의 목록을 그대로 위임하여 반환한다")
        void it_delegates_findAll_to_repository() {
            // given: 저장소가 꽃 두 건을 반환하도록 설정
            Flower rose = new Flower("장미", "빨강", 5000);
            Flower tulip = new Flower("튤립", "노랑", 3000);
            given(flowerRepository.findAll()).willReturn(List.of(rose, tulip));

            // when: findAll 메서드 호출
            List<Flower> flowers = flowerService.findAll();

            // then: 저장소 목록과 동일해야 함
            assertThat(flowers).containsExactly(rose, tulip);
        }

        @Test
        @DisplayName("저장소가 비어 있으면 빈 목록을 반환한다")
        void it_returns_empty_list_when_repository_is_empty() {
            // given: 저장소가 빈 목록을 반환하도록 설정
            given(flowerRepository.findAll()).willReturn(List.of());

            // when: findAll 메서드 호출
            List<Flower> flowers = flowerService.findAll();

            // then: 빈 목록이 반환되어야 함
            assertThat(flowers).isEmpty();
        }
    }
}

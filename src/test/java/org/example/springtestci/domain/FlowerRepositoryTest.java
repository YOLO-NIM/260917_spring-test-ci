package org.example.springtestci.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowerRepository 포트는")
class FlowerRepositoryTest {

    @Mock
    private FlowerRepository flowerRepository;

    @Nested
    @DisplayName("count 메서드는")
    class Describe_count {

        @Test
        @DisplayName("저장된 꽃의 개수를 반환한다")
        void it_returns_saved_flower_count() {
            // given: 저장된 꽃 개수를 2개로 설정
            given(flowerRepository.count()).willReturn(2L);

            // when: count 메서드 호출
            long count = flowerRepository.count();

            // then: 설정한 개수와 일치해야 함
            assertThat(count).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Test
        @DisplayName("전달받은 꽃을 저장하고 반환한다")
        void it_saves_and_returns_flower() {
            // given: 저장할 꽃 객체 준비
            Flower flower = new Flower("장미", "빨강", 5000);
            given(flowerRepository.save(flower)).willReturn(flower);

            // when: save 메서드 호출
            Flower saved = flowerRepository.save(flower);

            // then: 저장된 꽃이 입력값과 동일함
            assertThat(saved).isEqualTo(flower);
            verify(flowerRepository).save(flower);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class Describe_findAll {

        @Test
        @DisplayName("저장된 모든 꽃 목록을 반환한다")
        void it_returns_all_flowers() {
            // given: 저장된 꽃 목록 두 건 준비
            Flower rose = new Flower("장미", "빨강", 5000);
            Flower tulip = new Flower("튤립", "노랑", 3000);
            given(flowerRepository.findAll()).willReturn(List.of(rose, tulip));

            // when: findAll 메서드 호출
            List<Flower> flowers = flowerRepository.findAll();

            // then: 목록 크기와 순서가 일치함
            assertThat(flowers).hasSize(2).containsExactly(rose, tulip);
        }

        @Test
        @DisplayName("저장된 꽃이 없으면 빈 목록을 반환한다")
        void it_returns_empty_list_when_no_flowers() {
            // given: 빈 목록을 반환하도록 설정
            given(flowerRepository.findAll()).willReturn(List.of());

            // when: findAll 메서드 호출
            List<Flower> flowers = flowerRepository.findAll();

            // then: 빈 목록이 반환되어야 함
            assertThat(flowers).isEmpty();
        }
    }
}

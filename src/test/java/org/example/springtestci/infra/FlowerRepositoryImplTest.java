package org.example.springtestci.infra;

import org.example.springtestci.domain.Flower;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("FlowerRepositoryImpl은")
class FlowerRepositoryImplTest {

    @Mock
    private FlowerJpaRepository flowerJpaRepository;

    @InjectMocks
    private FlowerRepositoryImpl flowerRepository;

    @Nested
    @DisplayName("count 메서드는")
    class Describe_count {

        @Test
        @DisplayName("JPA 저장소의 개수를 그대로 반환한다")
        void it_returns_count_from_jpa_repository() {
            // given: JPA 저장소 개수를 2로 설정
            given(flowerJpaRepository.count()).willReturn(2L);

            // when: count 메서드 호출
            long count = flowerRepository.count();

            // then: JPA 저장소 값과 일치해야 함
            assertThat(count).isEqualTo(2L);
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Test
        @DisplayName("도메인 객체를 엔티티로 변환해 저장을 위임한다")
        void it_delegates_save_with_converted_entity() {
            // given: 저장할 꽃과 저장 결과 엔티티 준비
            Flower flower = new Flower("장미", "빨강", 5000);
            FlowerJpaEntity savedEntity = FlowerJpaEntity.builder()
                    .id(1L)
                    .name("장미")
                    .color("빨강")
                    .price(5000)
                    .build();
            given(flowerJpaRepository.save(any(FlowerJpaEntity.class))).willReturn(savedEntity);

            // when: save 메서드 호출
            Flower saved = flowerRepository.save(flower);

            // then: 변환된 도메인 객체가 반환되어야 함
            assertThat(saved).isEqualTo(flower);
            verify(flowerJpaRepository).save(any(FlowerJpaEntity.class));
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class Describe_findAll {

        @Test
        @DisplayName("저장된 꽃이 없으면 빈 목록을 반환한다")
        void it_returns_empty_list_when_no_flowers() {
            // given: JPA 저장소가 빈 목록을 반환하도록 설정
            given(flowerJpaRepository.findAll()).willReturn(List.of());

            // when: findAll 메서드 호출
            List<Flower> flowers = flowerRepository.findAll();

            // then: 빈 목록이 반환되어야 함
            assertThat(flowers).isEmpty();
        }

        @Test
        @DisplayName("엔티티 목록을 도메인 목록으로 변환해 반환한다")
        void it_returns_all_flowers_converted_to_domain() {
            // given: JPA 저장소에 엔티티 두 건 준비
            FlowerJpaEntity roseEntity = FlowerJpaEntity.builder()
                    .id(1L).name("장미").color("빨강").price(5000).build();
            FlowerJpaEntity tulipEntity = FlowerJpaEntity.builder()
                    .id(2L).name("튤립").color("노랑").price(3000).build();
            given(flowerJpaRepository.findAll()).willReturn(List.of(roseEntity, tulipEntity));

            // when: findAll 메서드 호출
            List<Flower> flowers = flowerRepository.findAll();

            // then: 도메인 객체 목록으로 변환되어야 함
            assertThat(flowers).containsExactly(
                    new Flower("장미", "빨강", 5000),
                    new Flower("튤립", "노랑", 3000)
            );
        }
    }
}

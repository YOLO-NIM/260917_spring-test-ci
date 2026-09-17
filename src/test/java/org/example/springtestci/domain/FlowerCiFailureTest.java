package org.example.springtestci.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CI 실패 검증용 테스트는")
class FlowerCiFailureTest {

    @Nested
    @DisplayName("가격 계산 로직은")
    class Describe_price {

        @Test
        @DisplayName("의도적으로 실패하여 CI 파이프라인 동작을 확인한다")
        void it_intentionally_fails_to_verify_ci() {
            // given: 장미 한 송이 가격 준비
            Flower rose = new Flower("장미", "빨강", 5000);

            // when: 가격을 확인
            int price = rose.price();

            // then: 실제 값과 다른 기대값으로 의도적 실패 유발
            assertThat(price).isEqualTo(9999);
        }
    }
}

package org.example.springtestci.infra;

import lombok.RequiredArgsConstructor;
import org.example.springtestci.domain.Flower;
import org.example.springtestci.domain.FlowerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FlowerRepositoryImpl implements FlowerRepository {

    private final FlowerJpaRepository flowerJpaRepository;

    @Override
    public long count() {
        // JPA 저장소에 위임해 전체 개수를 조회
        return flowerJpaRepository.count();
    }

    @Override
    public Flower save(Flower flower) {
        // 도메인 객체를 엔티티로 변환 후 저장
        FlowerJpaEntity entity = toEntity(flower);
        FlowerJpaEntity saved = flowerJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Flower> findAll() {
        // 엔티티 목록을 도메인 객체 목록으로 변환
        return flowerJpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private FlowerJpaEntity toEntity(Flower flower) {
        // Lombok 빌더로 신규 엔티티 생성
        return FlowerJpaEntity.builder()
                .name(flower.name())
                .color(flower.color())
                .price(flower.price())
                .build();
    }

    private Flower toDomain(FlowerJpaEntity entity) {
        // 엔티티를 도메인 레코드로 매핑
        return new Flower(entity.getName(), entity.getColor(), entity.getPrice());
    }
}
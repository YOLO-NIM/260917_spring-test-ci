package org.example.springtestci.ui;

import lombok.RequiredArgsConstructor;
import org.example.springtestci.app.FlowerUseCase;
import org.example.springtestci.domain.Flower;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flowers")
@RequiredArgsConstructor
public class FlowerApiController {

    private final FlowerUseCase flowerUseCase;

    @GetMapping("/count")
    public long count() {
        // 전체 꽃 개수 조회를 유스케이스에 위임
        return flowerUseCase.count();
    }

    @PostMapping
    public FlowerDto save(@RequestBody FlowerDto request) {
        // 요청 DTO를 도메인으로 변환 후 저장
        Flower saved = flowerUseCase.save(toDomain(request));
        return toDto(saved);
    }

    @GetMapping
    public List<FlowerDto> findAll() {
        // 도메인 목록을 응답 DTO 목록으로 변환
        return flowerUseCase.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    private Flower toDomain(FlowerDto dto) {
        // 요청 DTO를 도메인 레코드로 매핑
        return new Flower(dto.name(), dto.color(), dto.price());
    }

    private FlowerDto toDto(Flower flower) {
        // 도메인 레코드를 응답 DTO로 매핑
        return new FlowerDto(flower.name(), flower.color(), flower.price());
    }
}

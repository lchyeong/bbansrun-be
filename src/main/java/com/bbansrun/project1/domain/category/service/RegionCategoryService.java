package com.bbansrun.project1.domain.category.service;

import com.bbansrun.project1.domain.category.dto.RegionCategoryRequest;
import com.bbansrun.project1.domain.category.dto.RegionCategoryResponse;
import com.bbansrun.project1.domain.category.entity.RegionCategory;
import com.bbansrun.project1.domain.category.repository.RegionCategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@RequiredArgsConstructor
@Transactional
@Service
public class RegionCategoryService {

    private final RegionCategoryRepository regionCategoryRepository;

    /**
     * 모든 지역 정보를 조회한다.
     *
     * @return 모든 지역 정보 리스트
     */
    public List<RegionCategory> getAllRegions() {
        return regionCategoryRepository.findAllBy();
    }

    /**
     * 특정 경로를 기준으로 하위 지역 정보를 조회한다.
     *
     * @param code 특정 경로
     * @return 하위 지역 정보 리스트
     */
    public List<RegionCategoryResponse> getRegionsByCode(RegionCategoryRequest request) {
        List<RegionCategory> regions = regionCategoryRepository.findByRegionCodeStartingWith(
            request.getRegionCode());

        return regions.stream()
            .map(region -> new RegionCategoryResponse(
                region.getRegionName(),
                region.getRegionCode()))
            .collect(Collectors.toList());
    }
}


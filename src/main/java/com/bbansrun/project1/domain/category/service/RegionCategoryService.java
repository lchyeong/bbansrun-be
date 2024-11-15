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


    public List<RegionCategory> getAllRegions() {
        return regionCategoryRepository.findAllBy();
    }

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


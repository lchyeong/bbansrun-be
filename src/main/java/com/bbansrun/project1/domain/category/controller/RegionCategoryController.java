package com.bbansrun.project1.domain.category.controller;

import com.bbansrun.project1.domain.category.dto.RegionCategoryRequest;
import com.bbansrun.project1.domain.category.dto.RegionCategoryResponse;
import com.bbansrun.project1.domain.category.entity.RegionCategory;
import com.bbansrun.project1.domain.category.service.RegionCategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category/regions")
public class RegionCategoryController {

    private final RegionCategoryService regionCategoryService;

    @GetMapping
    public ResponseEntity<List<RegionCategory>> getAllRegions() {
        return ResponseEntity.ok(regionCategoryService.getAllRegions());
    }

    @GetMapping("/path")
    public ResponseEntity<List<RegionCategoryResponse>> findByRegionPathStartingWith(
        @RequestBody RegionCategoryRequest request) {

        List<RegionCategoryResponse> regions = regionCategoryService.getRegionsByPath(request);
        return ResponseEntity.ok(regions);
    }
}

package com.bbansrun.project1.domain.category.repository;


import com.bbansrun.project1.domain.category.entity.RegionCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegionCategoryRepository extends JpaRepository<RegionCategory, Long> {

    //공부를 위해 명시
    @Query("select r "
        + "from RegionCategory r")
    List<RegionCategory> findAllBy();

    @Query("select r "
        + "from RegionCategory r "
        + "where r.regionCode = :code "
        + "or r.regionCode "
        + "like CONCAT(:code, '-%')")
    List<RegionCategory> findByRegionCodeStartingWith(@Param("code") String code);
}
package com.bbansrun.project1.domain.post.repository;

import com.bbansrun.project1.domain.post.entity.RegionPost;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegionPostRepository extends JpaRepository<RegionPost, Long> {
    @NotNull
    Optional<RegionPost> findById(@NotNull Long id);

    @Query("SELECT p "
            + "FROM RegionPost p "
            + "WHERE p.regionCategory.regionCode "
            + "LIKE :regionCode%")
    List<RegionPost> findPostsByRegionCode(@Param("regionCode") String regionCode);
}

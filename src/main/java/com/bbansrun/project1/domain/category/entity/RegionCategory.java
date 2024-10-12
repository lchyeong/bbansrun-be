package com.bbansrun.project1.domain.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
@Table(name = "region_category")
public class RegionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String regionName;
    private String regionCode;

    @JsonIgnore
    @OneToMany(mappedBy = "parentRegion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegionCategory> childrenRegions;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private RegionCategory parentRegion;
}

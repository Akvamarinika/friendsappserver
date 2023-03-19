package com.akvamarin.friendsappserver.domain.entity.location;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="federal_districts")
@Entity
public class FederalDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "federalDistrict", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Region> regions;

    public void addRegionToFederalDistrict(Region region){
        if (regions == null){
            regions = new HashSet<>();
        }

        regions.add(region);

        region.setFederalDistrict(this);
    }

    public FederalDistrict(String name) {
        this.name = name;
    }
}

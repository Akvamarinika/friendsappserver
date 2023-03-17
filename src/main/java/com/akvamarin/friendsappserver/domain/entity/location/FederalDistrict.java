package com.akvamarin.friendsappserver.domain.entity.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
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

    @OneToMany(mappedBy = "federalDistrict", fetch = FetchType.LAZY)
    private List<Region> regions;

    private void addRegion(Region region){
        if (regions == null){
            regions = new ArrayList<>();
        }

        regions.add(region);

        region.setFederalDistrict(this);
    }


}

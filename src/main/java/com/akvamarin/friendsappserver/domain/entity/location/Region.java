package com.akvamarin.friendsappserver.domain.entity.location;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(onlyExplicitlyIncluded=true)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "regions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "federal_district_id"}))
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //cascade = {CascadeType.ALL},
    @ManyToOne//( fetch = FetchType.EAGER)
    //@MapsId
    @JoinColumn(name = "federal_district_id", referencedColumnName = "id")
    private FederalDistrict federalDistrict;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "region", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<City> cities;

    @ManyToOne(fetch = FetchType.LAZY) //***
    @JoinColumn(name = "country_id")
    private Country country;

    public void addCityToRegion(City city){
        if (cities == null){
            cities = new ArrayList<>();
        }

        cities.add(city);

        city.setRegion(this);
    }

    public Region(String name) {
        this.name = name;
    }

}

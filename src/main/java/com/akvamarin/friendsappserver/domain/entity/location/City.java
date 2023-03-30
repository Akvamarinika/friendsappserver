package com.akvamarin.friendsappserver.domain.entity.location;

import com.akvamarin.friendsappserver.domain.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double lat;
    private double lon;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    //@NonNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)  //***
    @JoinColumn(name = "federal_district_id")
    private FederalDistrict federalDistrict;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    @ToString.Exclude
    private List<User> users;

    public City(String city) {
        this.name = city;
    }
}

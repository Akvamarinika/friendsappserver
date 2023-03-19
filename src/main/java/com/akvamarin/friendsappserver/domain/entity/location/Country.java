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
@Table(name="countries")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "country", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<City> cities;

    public void addCityToCountry(City city){
        if (cities == null){
            cities = new ArrayList<>();
        }

        cities.add(city);

        city.setCountry(this);
    }

    public Country(String name) {
        this.name = name;
    }
}

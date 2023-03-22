package com.akvamarin.friendsappserver.domain.entity.location;

import com.akvamarin.friendsappserver.domain.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@ToString
@Getter @Setter
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    @ToString.Exclude
    private List<User> users;
}

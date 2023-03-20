package com.akvamarin.friendsappserver.domain.entity;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

//@JsonIgnoreProperties (ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true)   // for auth
    private String username;

    @Email
    @Column(unique = true, length = 64, nullable = false)
    private String email;

    @Column(unique = true, length = 14)
    private String phone;

    @Column(length = 128, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(name = "date_of_birthday", nullable = false)
    //@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirthday;

    //@Enumerated(EnumType.STRING)
    @Column(name = "e_sex")
    private Sex sex;

    @Column(name = "about_me", length = 320)
    private String aboutMe;

    //@Enumerated(EnumType.STRING)
    @Column(name = "e_smoking")
    private Smoking smoking;

    //@Enumerated(EnumType.STRING)
    @Column(name = "e_alcohol")
    private Alcohol alcohol;

    // @Enumerated(EnumType.STRING)
    @Column(name = "e_psychotype")
    private Psychotype psychotype;

    @Column(name = "url_avatar")
    private String urlAvatar;

    @ManyToOne//(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "city_id")
    private City city;

    //целевые объекты нельзя выбирать, сохранять, мержить напрямую, не зависимо от родительского объекта
    //Значения ElementCollection всегда хранятся в отдельных таблицах
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable//(name="e_role", joinColumns=@JoinColumn(name="role_id"))
    @Builder.Default
    @Enumerated(EnumType.STRING)
    //@Column(name = "e_role")
    private Collection<Role> authorities = List.of(Role.USER);

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
   // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt; //= LocalDateTime.now();

 //   @Transient
    @UpdateTimestamp
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Column(name = "vk_id", length = 64)
    private String vkId;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

  //  @Transient
  //  public boolean isNewUser() {
  //      return id == 0;
  //  }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", dateOfBirthday=" + dateOfBirthday +
                ", sex=" + sex +
                ", aboutMe='" + aboutMe + '\'' +
                ", smoking=" + smoking +
                ", alcohol=" + alcohol +
                ", psychotype=" + psychotype +
                ", phone='" + phone + '\'' +
                ", urlAvatar='" + urlAvatar + '\'' +
                ", role list=" + authorities +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", city='" + city + '\'' +
                '}';
    }


    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}

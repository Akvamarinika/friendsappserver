package com.akvamarin.friendsappserver.domain.entity;

import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.event.NotificationParticipant;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.message.Comment;
import com.akvamarin.friendsappserver.domain.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
    private Long id;

    @Column(name = "username", unique = true)   // for auth
    private String username;

    @Email
    @Column(length = 64, nullable = false)
    private String email;

    @Column(unique = true, length = 14)
    private String phone;

    @Column(length = 128)
    private String password;

    @Column(length = 50, nullable = false)
    private String nickname;

    @Column(name = "date_of_birthday", nullable = false)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
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
    @CollectionTable//(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Builder.Default
    @Enumerated(EnumType.STRING)
    //@Column(name = "role")
    private Collection<Role> authorities = List.of(Role.USER);

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

 //   @Transient
    @UpdateTimestamp
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "vk_id", length = 64)
    private String vkId;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Event> events;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<NotificationParticipant> participants;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<NotificationParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<NotificationParticipant> participants) {
        this.participants = participants;
    }

}

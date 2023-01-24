package com.example.authorization.entities;
import com.example.authorization.enums.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String nickname;

    @Column(name = "date_of_birthday")
    //@Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirthday;

    //@Enumerated(EnumType.STRING)
    @Column(name = "e_sex")
    private Sex sex;

    @Column(name = "about_me")
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

    private String phone;

    @Column(name = "url_avatar")
    private String urlAvatar;

    //@Enumerated(EnumType.STRING)
    @Column(name = "e_role")
    private Roles role;

    private String sol;

    private String password;

    @Builder.Default
    @Column(name = "created_at")
   // @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    private String city;

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
                ", role=" + role +
                ", sol='" + sol + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", city='" + city + '\'' +
                '}';
    }
}

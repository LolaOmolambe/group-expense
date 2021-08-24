package com.expense.tracker.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@SQLDelete(sql = "UPDATE users SET deleted=true WHERE user_id=?")
@Where(clause = "deleted = false")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long Id;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotEmpty
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotEmpty
    private String lastName;

    @Column(name = "email", nullable = false, length = 150)
    @Email
    private String email;

    @NotEmpty
    @Column(name = "password_encrypted")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<TeamUser> teamUsers;

    @Builder.Default
    private Boolean deleted = false;


    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}

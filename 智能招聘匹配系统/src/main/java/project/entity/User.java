package project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "account"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 60)
    private String account;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.CANDIDATE;
    @Column(length = 60)
    private String name;
    @Column(length = 30)
    private String phone;
    @Column(length = 80)
    private String email;
    @Column(length = 40)
    private String education;
    @Column(length = 80)
    private String major;
    private Integer age;
    @Column(length = 1000)
    private String introduction;
    @Column(length = 20)
    private String status = "启用";
    private LocalDateTime joinTime = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getJoinTime() { return joinTime; }
    public void setJoinTime(LocalDateTime joinTime) { this.joinTime = joinTime; }
}

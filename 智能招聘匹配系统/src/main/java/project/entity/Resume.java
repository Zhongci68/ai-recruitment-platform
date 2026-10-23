package project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Resume
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;
    @Column(length = 6000)
    private String rawText;
    @Column(length = 6000)
    private String structuredJson;
    @Column(length = 40)
    private String eduLevel;
    @Column(length = 1000)
    private String skills;
    private Integer experienceYears = 0;
    @Column(length = 3000)
    private String educationExperience;
    @Column(length = 3000)
    private String workExperience;
    @Column(length = 3000)
    private String projectExperience;
    private LocalDateTime updateTime = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRawText() { return rawText; }
    public void setRawText(String rawText) { this.rawText = rawText; }
    public String getStructuredJson() { return structuredJson; }
    public void setStructuredJson(String structuredJson) { this.structuredJson = structuredJson; }
    public String getEduLevel() { return eduLevel; }
    public void setEduLevel(String eduLevel) { this.eduLevel = eduLevel; }
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    public String getEducationExperience() { return educationExperience; }
    public void setEducationExperience(String educationExperience) { this.educationExperience = educationExperience; }
    public String getWorkExperience() { return workExperience; }
    public void setWorkExperience(String workExperience) { this.workExperience = workExperience; }
    public String getProjectExperience() { return projectExperience; }
    public void setProjectExperience(String projectExperience) { this.projectExperience = projectExperience; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

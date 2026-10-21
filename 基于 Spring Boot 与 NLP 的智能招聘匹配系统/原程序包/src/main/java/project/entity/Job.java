package project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User publisher;
    @ManyToOne
    private JobCategory category;
    @Column(nullable = false, length = 120)
    private String title;
    @Column(length = 60)
    private String salaryRange;
    @Column(length = 60)
    private String location;
    @Column(length = 40)
    private String education;
    @Column(length = 300)
    private String imageUrl;
    @Column(length = 3000)
    private String description;
    @Column(length = 2000)
    private String requirementsText;
    @Column(length = 800)
    private String keywords;
    private Integer vacancyCount = 1;
    private Integer viewCount = 0;
    private Double ratingAverage = 0.0;
    @Column(length = 20)
    private String status = "招聘中";
    private LocalDateTime createTime = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getPublisher() { return publisher; }
    public void setPublisher(User publisher) { this.publisher = publisher; }
    public JobCategory getCategory() { return category; }
    public void setCategory(JobCategory category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSalaryRange() { return salaryRange; }
    public void setSalaryRange(String salaryRange) { this.salaryRange = salaryRange; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequirementsText() { return requirementsText; }
    public void setRequirementsText(String requirementsText) { this.requirementsText = requirementsText; }
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public Integer getVacancyCount() { return vacancyCount; }
    public void setVacancyCount(Integer vacancyCount) { this.vacancyCount = vacancyCount; }
    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }
    public Double getRatingAverage() { return ratingAverage; }
    public void setRatingAverage(Double ratingAverage) { this.ratingAverage = ratingAverage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

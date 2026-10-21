package project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ApplicationRecord application;
    @ManyToOne
    private User interviewer;
    @ManyToOne
    private User candidate;
    private LocalDateTime interviewTime;
    @Column(length = 30)
    private String mode;
    @Column(length = 160)
    private String location;
    @Column(length = 30)
    private String status = "待确认";
    @Column(length = 800)
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ApplicationRecord getApplication() { return application; }
    public void setApplication(ApplicationRecord application) { this.application = application; }
    public User getInterviewer() { return interviewer; }
    public void setInterviewer(User interviewer) { this.interviewer = interviewer; }
    public User getCandidate() { return candidate; }
    public void setCandidate(User candidate) { this.candidate = candidate; }
    public LocalDateTime getInterviewTime() { return interviewTime; }
    public void setInterviewTime(LocalDateTime interviewTime) { this.interviewTime = interviewTime; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}

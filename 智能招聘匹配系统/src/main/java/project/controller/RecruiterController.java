package project.controller;

import project.config.AuthContext;
import project.config.RequireRole;
import project.dto.ApiResponse;
import project.entity.*;
import project.entity.ApplicationRecord;
import project.entity.Interview;
import project.entity.Job;
import project.entity.UserRole;
import project.repository.ApplicationRecordRepository;
import project.repository.InterviewRepository;
import project.repository.JobRepository;
import project.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequireRole(UserRole.RECRUITER)
@RestController
@RequestMapping("/api/hr")
public class RecruiterController {
    private final RecruitmentService service;
    private final JobRepository jobs;
    private final ApplicationRecordRepository applications;
    private final InterviewRepository interviews;

    public RecruiterController(RecruitmentService service, JobRepository jobs,
                               ApplicationRecordRepository applications, InterviewRepository interviews) {
        this.service = service;
        this.jobs = jobs;
        this.applications = applications;
        this.interviews = interviews;
    }

    @GetMapping("/jobs")
    public ApiResponse<?> jobs() {
        return ApiResponse.ok(jobs.findByPublisher(service.user(AuthContext.userId())));
    }

    @PostMapping("/jobs")
    public ApiResponse<?> saveJob(@RequestBody Job job) {
        job.setPublisher(service.user(AuthContext.userId()));
        return ApiResponse.ok(service.saveJob(job));
    }

    @GetMapping("/applications")
    public ApiResponse<?> applications() {
        List<ApplicationRecord> records = jobs.findByPublisher(service.user(AuthContext.userId())).stream()
                .flatMap(job -> applications.findByJob(job).stream())
                .toList();
        return ApiResponse.ok(records);
    }

    @PutMapping("/applications/{id}")
    public ApiResponse<?> updateStatus(@PathVariable Long id, @RequestBody ApplicationRecord input) {
        ApplicationRecord record = applications.findById(id).orElseThrow();
        record.setStatus(input.getStatus());
        return ApiResponse.ok(applications.save(record));
    }

    @PostMapping("/interviews")
    public ApiResponse<?> arrange(@RequestBody Interview interview) {
        interview.setInterviewer(service.user(AuthContext.userId()));
        if (interview.getApplication() != null && interview.getApplication().getId() != null) {
            ApplicationRecord app = applications.findById(interview.getApplication().getId()).orElseThrow();
            interview.setApplication(app);
            interview.setCandidate(app.getUser());
            app.setStatus("等待面试");
            applications.save(app);
        }
        return ApiResponse.ok(interviews.save(interview));
    }

    @GetMapping("/interviews")
    public ApiResponse<?> interviews() {
        return ApiResponse.ok(interviews.findByInterviewer(service.user(AuthContext.userId())));
    }

    @GetMapping("/dashboard")
    public ApiResponse<?> dashboard() {
        return ApiResponse.ok(service.dashboard());
    }
}

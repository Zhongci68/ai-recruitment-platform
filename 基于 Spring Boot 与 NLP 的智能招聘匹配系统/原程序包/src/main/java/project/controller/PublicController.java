package project.controller;

import project.dto.ApiResponse;
import project.repository.*;
import project.repository.CommentRepository;
import project.repository.HotJobRepository;
import project.repository.JobCategoryRepository;
import project.repository.JobRepository;
import project.service.RecruitmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController
{
    private final RecruitmentService recruitmentService;
    private final JobRepository jobs;
    private final JobCategoryRepository categories;
    private final HotJobRepository hotJobs;
    private final CommentRepository comments;

    public PublicController(RecruitmentService recruitmentService, JobRepository jobs, JobCategoryRepository categories,
                            HotJobRepository hotJobs, CommentRepository comments) {
        this.recruitmentService = recruitmentService;
        this.jobs = jobs;
        this.categories = categories;
        this.hotJobs = hotJobs;
        this.comments = comments;
    }

    @GetMapping("/home")
    public ApiResponse<?> home() {
        return ApiResponse.ok(Map.of(
                "hotJobs", hotJobs.findAll().stream().map(h -> h.getJob()).toList(),
                "categories", categories.findAll(),
                "latestJobs", jobs.findTop10ByOrderByCreateTimeDesc()
        ));
    }

    @GetMapping("/jobs")
    public ApiResponse<?> jobs(@RequestParam(required = false) String keyword, @RequestParam(required = false) Long categoryId) {
        return ApiResponse.ok(recruitmentService.searchJobs(keyword, categoryId));
    }

    @GetMapping("/jobs/{id}")
    public ApiResponse<?> job(@PathVariable Long id) {
        var job = recruitmentService.viewJob(id);
        return ApiResponse.ok(Map.of("job", job, "comments", comments.findByJobOrderByCreateTimeDesc(job)));
    }

    @GetMapping("/rank")
    public ApiResponse<?> rank() {
        return ApiResponse.ok(jobs.rank(PageRequest.of(0, 10)));
    }
}

package project.controller;

import project.config.AuthContext;
import project.config.RequireRole;
import project.dto.ApiResponse;
import project.entity.*;
import project.repository.*;
import project.entity.*;
import project.repository.*;
import project.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

@RequireRole(UserRole.ADMIN)
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final RecruitmentService service;
    private final UserRepository users;
    private final JobRepository jobs;
    private final JobCategoryRepository categories;
    private final ApplicationRecordRepository applications;
    private final FavoriteRepository favorites;
    private final RatingRepository ratings;
    private final CommentRepository comments;
    private final HotJobRepository hotJobs;
    private final AdminActionRepository actions;
    private final UserGroupRepository groups;

    public AdminController(RecruitmentService service, UserRepository users, JobRepository jobs,
                           JobCategoryRepository categories, ApplicationRecordRepository applications,
                           FavoriteRepository favorites, RatingRepository ratings, CommentRepository comments,
                           HotJobRepository hotJobs, AdminActionRepository actions, UserGroupRepository groups) {
        this.service = service;
        this.users = users;
        this.jobs = jobs;
        this.categories = categories;
        this.applications = applications;
        this.favorites = favorites;
        this.ratings = ratings;
        this.comments = comments;
        this.hotJobs = hotJobs;
        this.actions = actions;
        this.groups = groups;
    }

    @GetMapping("/dashboard")
    public ApiResponse<?> dashboard() {
        return ApiResponse.ok(service.dashboard());
    }

    @GetMapping("/{type}")
    public ApiResponse<?> list(@PathVariable String type) {
        return ApiResponse.ok(switch (type) {
            case "users" -> users.findAll();
            case "jobs" -> jobs.findAll();
            case "categories" -> categories.findAll();
            case "applications" -> applications.findAll();
            case "favorites" -> favorites.findAll();
            case "ratings" -> ratings.findAll();
            case "comments" -> comments.findAll();
            case "hotJobs" -> hotJobs.findAll();
            case "actions" -> actions.findTop8ByOrderByCreateTimeDesc();
            case "groups" -> groups.findAll();
            default -> throw new IllegalArgumentException("未知管理对象");
        });
    }

    @PostMapping("/categories")
    public ApiResponse<?> saveCategory(@RequestBody JobCategory category) {
        log("维护了类别：" + category.getName());
        return ApiResponse.ok(categories.save(category));
    }

    @PostMapping("/jobs")
    public ApiResponse<?> saveJob(@RequestBody Job job) {
        log("维护了职位：" + job.getTitle());
        return ApiResponse.ok(service.saveJob(job));
    }

    @PostMapping("/hotJobs")
    public ApiResponse<?> saveHotJob(@RequestBody HotJob hotJob) {
        log("设置了热门职位");
        return ApiResponse.ok(hotJobs.save(hotJob));
    }

    @PostMapping("/groups")
    public ApiResponse<?> saveGroup(@RequestBody UserGroup group) {
        log("维护了权限组：" + group.getName());
        return ApiResponse.ok(groups.save(group));
    }

    @PutMapping("/applications/{id}/status")
    public ApiResponse<?> updateApplication(@PathVariable Long id, @RequestBody ApplicationRecord input) {
        ApplicationRecord record = applications.findById(id).orElseThrow();
        record.setStatus(input.getStatus());
        log("修改了投递状态：" + input.getStatus());
        return ApiResponse.ok(applications.save(record));
    }

    @DeleteMapping("/{type}/{id}")
    public ApiResponse<?> delete(@PathVariable String type, @PathVariable Long id) {
        switch (type) {
            case "users" -> users.deleteById(id);
            case "jobs" -> jobs.deleteById(id);
            case "categories" -> categories.deleteById(id);
            case "applications" -> applications.deleteById(id);
            case "favorites" -> favorites.deleteById(id);
            case "ratings" -> ratings.deleteById(id);
            case "comments" -> comments.deleteById(id);
            case "hotJobs" -> hotJobs.deleteById(id);
            case "groups" -> groups.deleteById(id);
            default -> throw new IllegalArgumentException("未知管理对象");
        }
        log("删除了" + type + "#" + id);
        return ApiResponse.ok(true);
    }

    private void log(String actionText) {
        AdminAction action = new AdminAction();
        action.setAdmin(service.user(AuthContext.userId()));
        action.setAction(actionText);
        actions.save(action);
    }
}
    
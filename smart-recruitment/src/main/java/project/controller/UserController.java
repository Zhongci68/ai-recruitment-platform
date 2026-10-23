package project.controller;

import project.config.AuthContext;
import project.config.RequireRole;
import project.dto.ApiResponse;
import project.entity.Resume;
import project.entity.User;
import project.entity.UserRole;
//import project.repository.*;
import project.repository.*;
import project.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequireRole(UserRole.CANDIDATE)
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final RecruitmentService service;
    private final UserRepository users;
    private final ResumeRepository resumes;
    private final FavoriteRepository favorites;
    private final ApplicationRecordRepository applications;
    private final RatingRepository ratings;
    private final InterviewRepository interviews;

    public UserController(RecruitmentService service, UserRepository users, ResumeRepository resumes,
                          FavoriteRepository favorites, ApplicationRecordRepository applications,
                          RatingRepository ratings, InterviewRepository interviews) {
        this.service = service;
        this.users = users;
        this.resumes = resumes;
        this.favorites = favorites;
        this.applications = applications;
        this.ratings = ratings;
        this.interviews = interviews;
    }

    @GetMapping("/me")
    public ApiResponse<?> me() {
        User user = service.user(AuthContext.userId());
        return ApiResponse.ok(user);
    }

    @PutMapping("/me")
    public ApiResponse<?> updateMe(@RequestBody User input) {
        User user = service.user(AuthContext.userId());
        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEducation(input.getEducation());
        user.setMajor(input.getMajor());
        user.setAge(input.getAge());
        user.setIntroduction(input.getIntroduction());
        return ApiResponse.ok(users.save(user));
    }

    @GetMapping("/resume")
    public ApiResponse<?> resume() {
        User user = service.user(AuthContext.userId());
        return ApiResponse.ok(resumes.findByUser(user).orElse(new Resume()));
    }

    @PostMapping("/resume")
    public ApiResponse<?> saveResume(@RequestBody Resume resume) {
        return ApiResponse.ok(service.saveResume(AuthContext.userId(), resume));
    }

    @GetMapping("/recommend")
    public ApiResponse<?> recommend() {
        return ApiResponse.ok(service.recommend(AuthContext.userId()));
    }

    @PostMapping("/jobs/{id}/favorite")
    public ApiResponse<?> favorite(@PathVariable Long id) {
        return ApiResponse.ok(service.favorite(AuthContext.userId(), id));
    }

    @DeleteMapping("/favorites/{id}")
    public ApiResponse<?> cancelFavorite(@PathVariable Long id) {
        favorites.deleteById(id);
        return ApiResponse.ok(true);
    }

    @GetMapping("/favorites")
    public ApiResponse<?> favorites() {
        return ApiResponse.ok(favorites.findByUserOrderByCreateTimeDesc(service.user(AuthContext.userId())));
    }

    @PostMapping("/jobs/{id}/apply")
    public ApiResponse<?> apply(@PathVariable Long id) {
        return ApiResponse.ok(service.apply(AuthContext.userId(), id));
    }

    @GetMapping("/applications")
    public ApiResponse<?> applications() {
        List<Map<String, Object>> rows = applications.findByUserOrderByApplyTimeDesc(service.user(AuthContext.userId()))
                .stream()
                .map(record -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("record", record);
                    row.put("interview", interviews.findTopByApplicationOrderByInterviewTimeDesc(record).orElse(null));
                    return row;
                })
                .toList();
        return ApiResponse.ok(rows);
    }

    @PostMapping("/jobs/{id}/rate")
    public ApiResponse<?> rate(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return ApiResponse.ok(service.rate(AuthContext.userId(), id, body.getOrDefault("score", 5)));
    }

    @GetMapping("/ratings")
    public ApiResponse<?> ratings() {
        return ApiResponse.ok(ratings.findByUserOrderByCreateTimeDesc(service.user(AuthContext.userId())));
    }

    @PostMapping("/jobs/{id}/comments")
    public ApiResponse<?> comment(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ApiResponse.ok(service.comment(AuthContext.userId(), id, body.get("content")));
    }
}

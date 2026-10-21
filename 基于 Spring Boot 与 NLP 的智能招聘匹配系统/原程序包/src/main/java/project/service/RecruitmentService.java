package project.service;

import project.entity.*;
import project.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entity.*;
import project.repository.*;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecruitmentService {
    private final UserRepository users;
    private final JobRepository jobs;
    private final JobCategoryRepository categories;
    private final ResumeRepository resumes;
    private final ApplicationRecordRepository applications;
    private final FavoriteRepository favorites;
    private final RatingRepository ratings;
    private final CommentRepository comments;
    private final NlpMatchService nlp;

    public RecruitmentService(UserRepository users, JobRepository jobs, JobCategoryRepository categories,
                              ResumeRepository resumes, ApplicationRecordRepository applications,
                              FavoriteRepository favorites, RatingRepository ratings,
                              CommentRepository comments, NlpMatchService nlp) {
        this.users = users;
        this.jobs = jobs;
        this.categories = categories;
        this.resumes = resumes;
        this.applications = applications;
        this.favorites = favorites;
        this.ratings = ratings;
        this.comments = comments;
        this.nlp = nlp;
    }

    public User user(Long id) {
        return users.findById(id).orElseThrow(() -> new NoSuchElementException("用户不存在"));
    }

    public Job job(Long id) {
        return jobs.findById(id).orElseThrow(() -> new NoSuchElementException("职位不存在"));
    }

    @Transactional
    public Job saveJob(Job job) {
        job.setKeywords(nlp.extractKeywords((job.getDescription() == null ? "" : job.getDescription()) + " " + job.getRequirementsText()));
        return jobs.save(job);
    }

    @Transactional
    public Job viewJob(Long jobId) {
        Job job = job(jobId);
        job.setViewCount(job.getViewCount() + 1);
        return jobs.save(job);
    }

    public List<Job> searchJobs(String keyword, Long categoryId) {
        if (categoryId != null) {
            return jobs.findByCategory(categories.findById(categoryId).orElseThrow());
        }
        if (keyword != null && !keyword.isBlank()) {
            return jobs.search(keyword);
        }
        return jobs.findTop10ByOrderByCreateTimeDesc();
    }

    public List<Map<String, Object>> recommend(Long userId) {
        User user = user(userId);
        Resume resume = resumes.findByUser(user).orElse(null);
        return jobs.findAll().stream()
                .map(job -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("job", job);
                    row.put("matchScore", nlp.match(resume, job));
                    row.put("favorite", favorites.existsByJobAndUser(job, user));
                    return row;
                })
                .sorted((a, b) -> Double.compare((Double) b.get("matchScore"), (Double) a.get("matchScore")))
                .limit(12)
                .toList();
    }

    @Transactional
    public Resume saveResume(Long userId, Resume input) {
        User user = user(userId);
        Resume resume = resumes.findByUser(user).orElseGet(Resume::new);
        resume.setUser(user);
        resume.setEduLevel(input.getEduLevel());
        resume.setSkills(input.getSkills());
        resume.setExperienceYears(input.getExperienceYears());
        resume.setRawText(input.getRawText());
        resume.setEducationExperience(input.getEducationExperience());
        resume.setWorkExperience(input.getWorkExperience());
        resume.setProjectExperience(input.getProjectExperience());
        resume.setStructuredJson(nlp.structureResume(input.getRawText() + " " + input.getSkills()));
        resume.setUpdateTime(LocalDateTime.now());
        return resumes.save(resume);
    }

    @Transactional
    public Favorite favorite(Long userId, Long jobId) {
        User user = user(userId);
        Job job = job(jobId);
        return favorites.findByJobAndUser(job, user).orElseGet(() -> {
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setJob(job);
            return favorites.save(favorite);
        });
    }

    @Transactional
    public ApplicationRecord apply(Long userId, Long jobId) {
        User user = user(userId);
        Job job = job(jobId);
        if (applications.existsByJobAndUser(job, user)) {
            throw new IllegalArgumentException("该职位已经投递过");
        }
        Resume resume = resumes.findByUser(user).orElse(null);
        ApplicationRecord record = new ApplicationRecord();
        record.setUser(user);
        record.setJob(job);
        record.setResume(resume);
        record.setMatchScore(nlp.match(resume, job));
        return applications.save(record);
    }

    @Transactional
    public Rating rate(Long userId, Long jobId, int score) {
        User user = user(userId);
        Job job = job(jobId);
        Rating rating = ratings.findByJobAndUser(job, user).orElseGet(Rating::new);
        rating.setUser(user);
        rating.setJob(job);
        rating.setScore(Math.max(1, Math.min(5, score)));
        rating.setCreateTime(LocalDateTime.now());
        Rating saved = ratings.save(rating);
        List<Rating> jobRatings = ratings.findByJob(job);
        double avg = jobRatings.stream().mapToInt(Rating::getScore).average().orElse(0);
        job.setRatingAverage(Math.round(avg * 10.0) / 10.0);
        jobs.save(job);
        return saved;
    }

    @Transactional
    public Comment comment(Long userId, Long jobId, String content) {
        Comment comment = new Comment();
        comment.setUser(user(userId));
        comment.setJob(job(jobId));
        comment.setContent(content);
        return comments.save(comment);
    }

    public Map<String, Object> dashboard() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userCount", users.count());
        map.put("jobCount", jobs.count());
        map.put("applicationCount", applications.count());
        map.put("favoriteCount", favorites.count());
        map.put("rank", jobs.rank(PageRequest.of(0, 10)));
        map.put("trend", List.of(6, 9, 12, 15, 11, 18, 23));
        map.put("skills", List.of("Java", "Spring Boot", "MySQL", "Vue", "NLP", "测试", "Redis"));
        return map;
    }
}

package project.config;

import project.entity.*;
import project.repository.*;
import project.entity.*;
import project.repository.*;
import project.service.RecruitmentService;
import project.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Order(10)
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository users;
    private final JobCategoryRepository categories;
    private final JobRepository jobs;
    private final HotJobRepository hotJobs;
    private final ResumeRepository resumes;
    private final UserGroupRepository groups;
    private final RatingRepository ratings;
    private final CommentRepository comments;
    private final ApplicationRecordRepository applications;
    private final UserService userService;
    private final RecruitmentService recruitmentService;

    public DataInitializer(UserRepository users, JobCategoryRepository categories, JobRepository jobs,
                           HotJobRepository hotJobs, ResumeRepository resumes, UserGroupRepository groups,
                           RatingRepository ratings, CommentRepository comments, ApplicationRecordRepository applications,
                           UserService userService, RecruitmentService recruitmentService) {
        this.users = users;
        this.categories = categories;
        this.jobs = jobs;
        this.hotJobs = hotJobs;
        this.resumes = resumes;
        this.groups = groups;
        this.ratings = ratings;
        this.comments = comments;
        this.applications = applications;
        this.userService = userService;
        this.recruitmentService = recruitmentService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        User admin = user("admin", "123456", UserRole.ADMIN, "系统管理员", "18800000001");
        User alice = user("alice", "123456", UserRole.CANDIDATE, "胡同学", "18800000002");
        User bob = user("bob", "123456", UserRole.CANDIDATE, "陈同学", "18800000004");
        User cindy = user("cindy", "123456", UserRole.CANDIDATE, "林同学", "18800000005");
        User hr = user("hr", "123456", UserRole.RECRUITER, "招聘经理", "18800000003");

        group("管理员组", "拥有用户、岗位、类别、评分、评论、热门职位和数据看板管理权限");
        group("招聘者组", "拥有岗位发布、简历筛选、面试安排和公司信息维护权限");

        JobCategory backend = category("后端开发");
        JobCategory frontend = category("前端开发");
        JobCategory ai = category("人工智能");
        JobCategory test = category("软件测试");
        JobCategory data = category("数据分析");
        JobCategory product = category("产品运营");

        List<Job> seedJobs = List.of(
                job(hr, backend, "Java后端开发工程师", "12K-20K", "杭州", "本科", "负责企业招聘平台后端接口、权限控制、推荐服务开发。", "熟悉Java、Spring Boot、MySQL、Redis，理解RESTful接口与数据库设计。", 8, 321),
                job(hr, backend, "Spring Boot后端实习生", "6K-10K", "南京", "本科", "参与智能招聘系统后端模块、数据库表结构和推荐接口开发。", "熟悉Java、Spring Boot、MySQL，了解Redis和接口调试。", 3, 156),
                job(hr, backend, "微服务开发工程师", "16K-28K", "深圳", "本科", "负责招聘业务微服务拆分、消息队列和高并发接口治理。", "熟悉Spring Cloud、Docker、RabbitMQ、MySQL和Linux。", 4, 244),
                job(hr, frontend, "Web前端工程师", "10K-18K", "上海", "本科", "负责招聘系统前端页面、可视化看板与移动端适配。", "熟悉HTML、CSS、JavaScript、Vue或React，了解Bootstrap和ECharts。", 6, 286),
                job(hr, frontend, "Vue前端开发实习生", "5K-9K", "杭州", "大专", "参与职位列表、简历编辑、后台管理页面开发。", "熟悉Vue、JavaScript、CSS，能独立完成页面交互。", 5, 187),
                job(hr, ai, "NLP算法实习生", "8K-15K", "北京", "本科", "参与中文简历解析、岗位关键词抽取、匹配度算法优化。", "了解Python、机器学习、NLP、TextRank、TF-IDF，有项目经验优先。", 4, 415),
                job(hr, ai, "机器学习工程师", "18K-35K", "杭州", "硕士", "建设候选人画像、推荐排序模型和招聘数据智能分析能力。", "熟悉Python、机器学习、深度学习、NLP和数据结构。", 2, 376),
                job(hr, test, "自动化测试工程师", "9K-16K", "广州", "大专", "负责招聘系统接口测试、功能测试、自动化脚本维护。", "熟悉软件测试流程、接口测试、自动化测试、Linux基础。", 5, 198),
                job(hr, test, "测试开发工程师", "12K-22K", "成都", "本科", "负责自动化测试平台、测试数据构造和质量度量工具开发。", "熟悉Java或Python、接口测试、自动化测试和持续集成。", 3, 203),
                job(hr, data, "数据分析实习生", "5K-8K", "杭州", "本科", "负责招聘数据统计、岗位趋势分析和可视化报表维护。", "熟悉SQL、Excel、ECharts，有数据分析基础。", 2, 167),
                job(hr, data, "BI数据分析师", "11K-20K", "上海", "本科", "负责招聘转化漏斗、投递趋势、城市岗位分布等分析。", "熟悉SQL、Python、数据可视化和业务分析方法。", 3, 226),
                job(hr, product, "招聘产品助理", "6K-9K", "杭州", "本科", "负责智能招聘产品需求整理、用户反馈分析和原型优化。", "具备产品思维，熟悉Axure、数据分析和沟通协作。", 2, 142)
        );
        seedJobs.forEach(recruitmentService::saveJob);
        jobs.findTop10ByOrderByViewCountDesc().forEach(this::hotJob);

        resume(alice, "本科", "Java, Spring Boot, MySQL, Vue, ECharts, NLP", "本科软件工程学生，熟悉Java、Spring Boot、MySQL，做过智能招聘匹配系统和数据分析看板。");
        resume(bob, "本科", "Python, NLP, 机器学习, 数据分析, ECharts", "本科人工智能方向，熟悉Python、NLP、机器学习和招聘文本分析。");
        resume(cindy, "大专", "软件测试, 自动化测试, Linux, 接口测试", "熟悉软件测试流程，掌握接口测试、自动化测试和Linux基础。");

        seedDemoFeedback(List.of(alice, bob, cindy));
        seedApplications(alice, bob, cindy);
    }

    private User user(String account, String password, UserRole role, String name, String phone) {
        return users.findByAccount(account).orElseGet(() -> {
            User user = new User();
            user.setAccount(account);
            user.setPassword(userService.hash(password));
            user.setRole(role);
            user.setName(name);
            user.setPhone(phone);
            return users.save(user);
        });
    }

    private JobCategory category(String name) {
        return categories.findByName(name).orElseGet(() -> {
            JobCategory category = new JobCategory();
            category.setName(name);
            return categories.save(category);
        });
    }

    private void group(String name, String description) {
        boolean exists = groups.findAll().stream().anyMatch(g -> name.equals(g.getName()));
        if (!exists) {
            UserGroup group = new UserGroup();
            group.setName(name);
            group.setDescription(description);
            groups.save(group);
        }
    }

    private Job job(User publisher, JobCategory category, String title, String salary, String location, String edu, String description, String requirements, int count, int views) {
        if (jobs.existsByTitle(title)) {
            return jobs.search(title).stream().filter(j -> title.equals(j.getTitle())).findFirst().orElseThrow();
        }
        Job job = new Job();
        job.setPublisher(publisher);
        job.setCategory(category);
        job.setTitle(title);
        job.setSalaryRange(salary);
        job.setLocation(location);
        job.setEducation(edu);
        job.setDescription(description);
        job.setRequirementsText(requirements);
        job.setVacancyCount(count);
        job.setViewCount(views);
        job.setImageUrl("/css/job.svg");
        return job;
    }

    private void hotJob(Job job) {
        boolean exists = hotJobs.findAll().stream().anyMatch(h -> h.getJob() != null && h.getJob().getId().equals(job.getId()));
        if (!exists) {
            HotJob hotJob = new HotJob();
            hotJob.setJob(job);
            hotJobs.save(hotJob);
        }
    }

    private void resume(User user, String edu, String skills, String rawText) {
        Resume resume = resumes.findByUser(user).orElseGet(Resume::new);
        resume.setUser(user);
        resume.setEduLevel(edu);
        resume.setSkills(skills);
        resume.setExperienceYears(1);
        resume.setRawText(rawText);
        resumes.save(resume);
    }

    private void seedDemoFeedback(List<User> candidates) {
        if (ratings.count() > 0 && comments.count() > 0) return;
        List<Job> ranked = jobs.findTop10ByOrderByViewCountDesc();
        String[] texts = {"岗位描述清晰，技术栈和薪资范围都比较明确。", "比较适合软件工程学生练习项目展示，要求写得很具体。", "地区和学历要求清楚，投递前能快速判断是否匹配。"};
        for (int i = 0; i < Math.min(ranked.size(), 8); i++) {
            Job job = ranked.get(i);
            for (int u = 0; u < candidates.size(); u++) {
                User candidate = candidates.get(u);
                if (ratings.findByJobAndUser(job, candidate).isEmpty()) {
                    Rating rating = new Rating();
                    rating.setJob(job);
                    rating.setUser(candidate);
                    rating.setScore(4 + ((i + u) % 2));
                    ratings.save(rating);
                }

                Comment comment = new Comment();
                comment.setJob(job);
                comment.setUser(candidate);
                comment.setContent(texts[(i + u) % texts.length]);
                comments.save(comment);
            }
            List<Rating> jobRatings = ratings.findByJob(job);
            double avg = jobRatings.stream().mapToInt(Rating::getScore).average().orElse(0);
            job.setRatingAverage(Math.round(avg * 10.0) / 10.0);
            jobs.save(job);
        }
    }

    private void seedApplications(User alice, User bob, User cindy) {
        if (applications.count() > 0) return;
        jobs.findTop10ByOrderByViewCountDesc().stream().limit(4).forEach(job -> apply(alice, job));
        jobs.search("NLP").stream().findFirst().ifPresent(job -> apply(bob, job));
        jobs.search("测试").stream().findFirst().ifPresent(job -> apply(cindy, job));
    }

    private void apply(User user, Job job) {
        if (applications.existsByJobAndUser(job, user)) return;
        Resume resume = resumes.findByUser(user).orElse(null);
        ApplicationRecord record = new ApplicationRecord();
        record.setUser(user);
        record.setJob(job);
        record.setResume(resume);
        record.setMatchScore(80.0);
        applications.save(record);
    }
}

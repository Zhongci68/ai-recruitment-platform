package project.service;

import project.entity.Job;
import project.entity.Resume;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class NlpMatchService
{
    private static final List<String> TECH_WORDS = List.of(
            "Java", "Spring", "Spring Boot", "MySQL", "Redis", "Vue", "React", "JavaScript",
            "Python", "Django", "Flask", "Linux", "Docker", "Kubernetes", "算法", "数据结构",
            "机器学习", "深度学习", "NLP", "自然语言处理", "测试", "自动化测试", "产品", "运营",
            "ECharts", "Bootstrap", "HTML", "CSS", "微服务", "RabbitMQ", "消息队列", "前端", "后端"
    );

    public String extractKeywords(String text) {
        String safe = Optional.ofNullable(text).orElse("");
        return TECH_WORDS.stream()
                .filter(word -> safe.toLowerCase().contains(word.toLowerCase()))
                .distinct()
                .collect(Collectors.joining(","));
    }

    public String structureResume(String rawText) {
        String text = Optional.ofNullable(rawText).orElse("");
        String phone = find(text, "1[3-9]\\d{9}");
        String email = find(text, "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        String skills = extractKeywords(text);
        String edu = detectEducation(text);
        return "{\"phone\":\"" + escape(phone) + "\",\"email\":\"" + escape(email)
                + "\",\"education\":\"" + escape(edu) + "\",\"skills\":\"" + escape(skills) + "\"}";
    }

    public double match(Resume resume, Job job) {
        String resumeText = join(resume == null ? "" : resume.getRawText(), resume == null ? "" : resume.getSkills(),
                resume == null ? "" : resume.getProjectExperience(), resume == null ? "" : resume.getWorkExperience());
        String jobText = join(job.getTitle(), job.getDescription(), job.getRequirementsText(), job.getKeywords());
        Map<String, Double> a = vectorize(resumeText);
        Map<String, Double> b = vectorize(jobText);
        double cosine = cosine(a, b);
        double educationFactor = educationFactor(resume == null ? "" : resume.getEduLevel(), job.getEducation());
        return Math.min(100.0, Math.round(cosine * educationFactor * 10000.0) / 100.0);
    }

    private Map<String, Double> vectorize(String text) {
        String normalized = Optional.ofNullable(text).orElse("").toLowerCase(Locale.ROOT);
        Map<String, Double> vector = new HashMap<>();
        for (String word : TECH_WORDS) {
            if (normalized.contains(word.toLowerCase(Locale.ROOT))) {
                vector.put(word.toLowerCase(Locale.ROOT), word.length() > 4 ? 1.4 : 1.0);
            }
        }
        for (String token : normalized.split("[^a-zA-Z0-9\\u4e00-\\u9fa5]+")) {
            if (token.length() >= 2) {
                vector.merge(token, 0.4, Double::sum);
            }
        }
        return vector;
    }

    private double cosine(Map<String, Double> a, Map<String, Double> b) {
        if (a.isEmpty() || b.isEmpty()) {
            return 0.0;
        }
        double dot = 0, normA = 0, normB = 0;
        for (double value : a.values()) {
            normA += value * value;
        }
        for (double value : b.values()) {
            normB += value * value;
        }
        for (String key : a.keySet()) {
            dot += a.getOrDefault(key, 0.0) * b.getOrDefault(key, 0.0);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private double educationFactor(String resumeEdu, String jobEdu) {
        int resume = eduRank(resumeEdu);
        int job = eduRank(jobEdu);
        if (job == 0 || resume >= job) {
            return 1.0;
        }
        return Math.max(0.65, 1.0 - (job - resume) * 0.12);
    }

    private int eduRank(String edu) {
        String safe = Optional.ofNullable(edu).orElse("");
        if (safe.contains("博士")) return 5;
        if (safe.contains("硕士") || safe.contains("研究生")) return 4;
        if (safe.contains("本科")) return 3;
        if (safe.contains("大专") || safe.contains("专科")) return 2;
        if (safe.contains("高中")) return 1;
        return 0;
    }

    private String detectEducation(String text) {
        return List.of("博士", "硕士", "研究生", "本科", "大专", "专科", "高中").stream()
                .filter(text::contains).findFirst().orElse("");
    }

    private String find(String text, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        return matcher.find() ? matcher.group() : "";
    }

    private String join(String... parts) {
        return Arrays.stream(parts).filter(Objects::nonNull).collect(Collectors.joining(" "));
    }

    private String escape(String value) {
        return Optional.ofNullable(value).orElse("").replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

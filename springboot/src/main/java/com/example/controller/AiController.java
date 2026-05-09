package com.example.controller;

import com.example.common.Result;
import com.example.entity.Apply;
import com.example.entity.Coach;
import com.example.entity.Course;
import com.example.entity.User;
import com.example.mapper.ApplyMapper;
import com.example.mapper.CoachMapper;
import com.example.mapper.CourseMapper;
import com.example.mapper.UserMapper;
import com.example.service.RechargeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Resource
    CourseMapper courseMapper;

    @Resource
    CoachMapper coachMapper;

    @Resource
    ApplyMapper applyMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    RechargeService rechargeService;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/chat")
    public Result chat(@RequestBody Map<String, Object> body) {
        String userMessage = (String) body.get("message");

        // 查询系统真实数据
        String systemPrompt = buildSystemPrompt();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
            "model", "deepseek-chat",
            "messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userMessage)
            ),
            "max_tokens", 600
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://api.deepseek.com/chat/completions", request, Map.class
        );

        List<Map> choices = (List<Map>) response.getBody().get("choices");
        Map message = (Map) choices.get(0).get("message");
        String reply = (String) message.get("content");

        return Result.success(reply);
    }

    private String buildSystemPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业的AI健身顾问，服务于本健身俱乐部管理系统。请根据以下系统真实数据回答用户问题，推荐时请结合具体课程和教练信息，回答简洁友好。\n\n");

        // 拼接课程数据
        try {
            List<Course> courses = courseMapper.selectActiveCourses();
            if (!courses.isEmpty()) {
                sb.append("【当前可预约课程】\n");
                courses.forEach(c -> sb.append(String.format(
                    "- %s：%s，价格¥%s，时长%d分钟，教练：%s\n",
                    c.getName(),
                    c.getDescription() != null ? c.getDescription() : "暂无描述",
                    c.getPrice(),
                    c.getDuration() != null ? c.getDuration() : 0,
                    c.getCoachName() != null ? c.getCoachName() : "待定"
                )));
                sb.append("\n");
            }
        } catch (Exception ignored) {}

        // 拼接教练数据
        try {
            List<Coach> coaches = coachMapper.selectAll(new Coach());
            if (!coaches.isEmpty()) {
                sb.append("【教练团队】\n");
                coaches.forEach(c -> sb.append(String.format(
                    "- %s（%s）：私教费用¥%s，剩余名额%d人\n",
                    c.getName(),
                    c.getPosition() != null ? c.getPosition() : "健身教练",
                    c.getPrice() != null ? c.getPrice() : "面议",
                    c.getNum() != null ? c.getNum() : 0
                )));
                sb.append("\n");
            }
        } catch (Exception ignored) {}

        sb.append("请根据以上信息回答用户的问题，如果用户询问课程推荐、教练推荐、价格等，请直接给出具体建议。");
        return sb.toString();
    }

    /**
     * AI 审批建议
     */
    @GetMapping("/applyAdvice/{applyId}")
    public Result applyAdvice(@PathVariable Integer applyId) {
        Apply apply = applyMapper.selectById(applyId);
        if (apply == null) return Result.error("预约不存在");

        // 查会员余额
        java.math.BigDecimal balance = rechargeService.getBalance(apply.getUserId());

        // 查历史预约记录
        Apply query = new Apply();
        query.setUserId(apply.getUserId());
        List<Apply> history = applyMapper.selectAll(query);
        long successCount = history.stream().filter(a -> "预约成功".equals(a.getStatus())).count();
        long failCount = history.stream().filter(a -> "预约失败".equals(a.getStatus())).count();

        // 拼 prompt
        StringBuilder prompt = new StringBuilder("请分析以下预约申请并给出审批建议，回复格式：第一行只写「建议通过」或「建议拒绝」，第二行写理由（30字以内）。\n\n");
        prompt.append("会员：").append(apply.getUserName()).append("\n");
        prompt.append("当前余额：¥").append(balance).append("\n");
        if ("COURSE".equals(apply.getCourseType())) {
            prompt.append("预约课程：").append(apply.getCourseName())
                  .append("，价格：¥").append(apply.getCoursePrice()).append("\n");
        } else {
            prompt.append("自定义课程：").append(apply.getTitle()).append("\n");
        }
        prompt.append("历史预约：共").append(history.size()).append("次，成功").append(successCount)
              .append("次，失败").append(failCount).append("次\n");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = Map.of(
            "model", "deepseek-chat",
            "messages", List.of(
                Map.of("role", "system", "content", "你是健身俱乐部的智能审批助手，根据会员信息给出简洁的审批建议。"),
                Map.of("role", "user", "content", prompt.toString())
            ),
            "max_tokens", 100
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
            "https://api.deepseek.com/chat/completions", request, Map.class
        );
        List<Map> choices = (List<Map>) response.getBody().get("choices");
        String reply = (String) ((Map) choices.get(0).get("message")).get("content");
        return Result.success(reply);
    }
}

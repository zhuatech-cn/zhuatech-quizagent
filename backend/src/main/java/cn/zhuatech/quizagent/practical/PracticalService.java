/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.quizagent.practical;

import static cn.zhuatech.quizagent.practical.PracticalInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class PracticalService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);
    keys(r, "questions", "answers");
    Map<String, Map<String, Object>> questions = new LinkedHashMap<>();
    for (var q : rows(r, "questions", 1, 200)) {
      keys(q, "id", "type", "points", "choices", "correct");
      String id = id(q, "id");
      if (questions.putIfAbsent(id, q) != null) throw new IllegalArgumentException("题目重复");
      String type = text(q, "type", 10);
      if (!Set.of("SINGLE", "MULTI").contains(type)) throw new IllegalArgumentException("题型错误");
      decimal(q, "points", 0.000001, 1000);
      var choices = strings(q, "choices", 2, 20);
      var correct = strings(q, "correct", 1, 20);
      if (new HashSet<>(choices).size() != choices.size()
          || new HashSet<>(correct).size() != correct.size()
          || !choices.containsAll(correct)
          || type.equals("SINGLE") && correct.size() != 1)
        throw new IllegalArgumentException("标准答案或选项错误");
    }
    Map<String, List<String>> answers = new HashMap<>();
    for (var a : rows(r, "answers", 0, 200)) {
      keys(a, "question", "selected");
      String id = id(a, "question");
      if (!questions.containsKey(id)) throw new IllegalArgumentException("作答引用未知题目");
      var selected = strings(a, "selected", 0, 20);
      var q = questions.get(id);
      if (answers.putIfAbsent(id, selected) != null
          || new HashSet<>(selected).size() != selected.size()
          || !strings(q, "choices", 2, 20).containsAll(selected)
          || q.get("type").equals("SINGLE") && selected.size() > 1)
        throw new IllegalArgumentException("重复作答或选项错误");
    }
    BigDecimal total = BigDecimal.ZERO, earned = BigDecimal.ZERO;
    int unanswered = 0;
    List<Map<String, Object>> out = new ArrayList<>();
    for (var e : questions.entrySet()) {
      var q = e.getValue();
      var selected = answers.getOrDefault(e.getKey(), List.of());
      boolean empty = selected.isEmpty(),
          correct =
              !empty && new HashSet<>(selected).equals(new HashSet<>(strings(q, "correct", 1, 20)));
      var points = decimal(q, "points", 0.000001, 1000);
      var score = correct ? points : BigDecimal.ZERO;
      total = total.add(points);
      earned = earned.add(score);
      if (empty) unanswered++;
      out.add(
          Map.of(
              "id",
              e.getKey(),
              "state",
              empty ? "UNANSWERED" : correct ? "CORRECT" : "INCORRECT",
              "earned",
              score,
              "possible",
              points));
    }
    return Map.of(
        "earnedPoints",
        earned,
        "possiblePoints",
        total,
        "scorePercent",
        earned.multiply(new BigDecimal("100")).divide(total, 6, RoundingMode.HALF_UP),
        "unansweredCount",
        unanswered,
        "questions",
        out,
        "certificationIssued",
        false);
  }
}

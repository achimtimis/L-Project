package app.domain.results;

import app.domain.answers.QuizResponseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by achy_ on 6/10/2017.
 */
@Entity
@Table(name = "result_entity")
@Data
@NoArgsConstructor
public class ResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private QuizResponseEntity quizResponse;

    // 1- 100
    @Column(name = "total_score")
    private double totalScore;

    private Map<Long, String> observations = new HashMap<>();

    private Map<Long, String> scorePerQuestion = new HashMap<>();

    private String extraFeedback;

    private String recomandations;

    private int totalDuration;



}

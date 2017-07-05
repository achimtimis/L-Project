package app.domain.questions;

import lombok.Data;
import lombok.NoArgsConstructor;
import models.utils.QuizTypeEnum;
import models.utils.TopicEnum;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by achy_ on 6/7/2017.
 */
@Entity
@Table(name = "quiz_entity")
@Data
@NoArgsConstructor
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "quiz_question_id")
    private List<QuestionEntity> quiz_questions = new ArrayList<>();

    private TopicEnum topic;

    private QuizTypeEnum quizType;

    private int timer;

    private double minimumScoreToPass;

    private double totalScore;

    private String creatorId;

    private boolean isTimed;

    private String name;


}

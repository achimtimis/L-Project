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

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE} , fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinTable(name = "question_quiz", joinColumns = @JoinColumn(name = "question_id"), inverseJoinColumns = @JoinColumn(name = "quiz_id"))
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "quiestion_id")
    private List<QuestionEntity> quiz_questions = new ArrayList<>();

    private TopicEnum topic;

    private QuizTypeEnum quizType;

    private int timer;

    private double minimumScoreToPass;

    private String creatorId;

    private boolean isTimed;

    private String name;


}

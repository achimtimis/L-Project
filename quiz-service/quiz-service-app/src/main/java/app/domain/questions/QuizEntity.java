package app.domain.questions;

import app.domain.topic.TopicEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(name  = "quiz_id")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_id")
    private List<QuestionEntity> quiz_questions;

    private TopicEnum topic;

    private QuizTypeEnum quizType;

    private int timer;





}

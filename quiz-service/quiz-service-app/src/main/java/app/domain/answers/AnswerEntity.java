package app.domain.answers;

import app.domain.questions.QuestionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by achy_ on 6/7/2017.
 */
@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity quizQuestion;

    @ManyToOne(cascade = CascadeType.ALL)
    private QuizResponseEntity quizResponse;

    private String ciamUserId;

    @ElementCollection(targetClass = Integer.class)
    private List<Integer> option_responses = new ArrayList<>();

    private String input_response;
}

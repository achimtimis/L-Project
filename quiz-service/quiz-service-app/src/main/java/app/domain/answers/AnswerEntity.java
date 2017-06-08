package app.domain.answers;

import app.domain.questions.QuestionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private QuestionEntity quiz_question;

    private String ciamUserId;

    @ElementCollection(targetClass=Integer.class)
    private List<Integer> option_responses;

    private String input_response;
}

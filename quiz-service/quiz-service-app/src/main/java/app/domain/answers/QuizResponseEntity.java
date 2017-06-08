package app.domain.answers;

import app.domain.questions.QuizEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by achy_ on 6/8/2017.
 */
@Entity
@Table(name = "quiz_response")
@Data
@NoArgsConstructor
public class QuizResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_id")
    private QuizEntity quiz;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_id")
    private List<AnswerEntity> answers;

    private int time;

    private String userId;

}

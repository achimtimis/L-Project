package app.domain.questions;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by achy_ on 6/8/2017.
 */
@Entity
@Table(name = "correct_answers")
@Data
@NoArgsConstructor
public class QuestionCorrectAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
//    private QuestionEntity question;

    @ElementCollection(targetClass=Integer.class)
    private List<Integer> validAnswers;
}

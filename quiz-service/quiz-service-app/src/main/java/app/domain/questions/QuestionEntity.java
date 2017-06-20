package app.domain.questions;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by achy_ on 6/7/2017.
 */
@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    private String questionText;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quest_correct_answer_id")
    private QuestionCorrectAnswer questionCorrectAnswer;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Integer, String> options = new HashMap<>();

    private double score;

    public void addOption(String option) {
        if (options.keySet().size() > 0) {
            int position = Collections.max(options.keySet());
            options.put(++position, option);
        } else {
            options.put(1, option);
        }
    }

    private void deleteOption(int option_number) {
        options.remove(option_number);
    }

}

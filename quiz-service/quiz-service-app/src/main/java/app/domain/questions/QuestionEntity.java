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

    @ElementCollection
    private Map<Integer, String> options = new HashMap<>();

    public void addOption(String option) {
        if (options.keySet().size() > 0) {
            int position = Collections.max(options.keySet());
            options.put(position++, option);
        } else {
            options.put(1, option);
        }
    }

    private void deleteOption(int option_number) {
        options.remove(option_number);
    }

}

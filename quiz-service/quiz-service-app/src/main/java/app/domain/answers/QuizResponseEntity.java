package app.domain.answers;

import app.domain.questions.QuizEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by achy_ on 6/8/2017.
 */
@Entity
@Table(name = "quiz_response")
@Data
@NoArgsConstructor
public class QuizResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_response_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quiz_id")
    private QuizEntity quiz;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    @JoinTable(name = "quiz_answers", joinColumns = @JoinColumn(name = "quiz_id"),
//            inverseJoinColumns = @JoinColumn(name = "answer_id")
//            )
    @JoinColumn(name = "quiz_answer_id")
    private List<AnswerEntity> answers = new ArrayList<>();

    private int time;

    private String userId;

    private boolean isCorrected;

    private boolean wasFinishedInTime;

}

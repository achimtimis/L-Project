package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by achy_ on 6/10/2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultResponse {

    // the results for a given quiz for all students

    private Long id;

    private Long quizResponseId;

    private List<QuizStudentResultResponse> quizResults;


}

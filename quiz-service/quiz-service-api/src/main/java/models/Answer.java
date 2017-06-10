package models;

import lombok.Data;

import java.util.List;

/**
 * Created by achy_ on 6/11/2017.
 */
@Data
public class Answer {

    private Long id;

    private Long questionId;

    private String userId;

    private List<Integer> chosenOptions;

    private String inputResponse;
}

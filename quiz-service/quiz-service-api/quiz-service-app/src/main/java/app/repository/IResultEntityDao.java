package app.repository;

import app.domain.results.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by achy_ on 6/10/2017.
 */
public interface IResultEntityDao extends JpaRepository<ResultEntity,Long>{
}

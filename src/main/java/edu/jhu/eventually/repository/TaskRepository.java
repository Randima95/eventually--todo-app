package edu.jhu.eventually.repository;

import edu.jhu.eventually.model.AppUser;
import edu.jhu.eventually.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerOrderByDeadlineAsc(AppUser owner);

    List<Task> findByOwnerAndCompletedOrderByDeadlineAsc(AppUser owner, boolean completed);

    List<Task> findByOwnerAndDescriptionContainingIgnoreCaseOrderByDeadlineAsc(
            AppUser owner,
            String keyword
    );

    Optional<Task> findByIdAndOwner(Long id, AppUser owner);
}
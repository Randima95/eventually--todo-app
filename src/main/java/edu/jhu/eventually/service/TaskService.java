package edu.jhu.eventually.service;

import edu.jhu.eventually.model.AppUser;
import edu.jhu.eventually.model.Task;
import edu.jhu.eventually.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks(
            AppUser owner,
            String keyword,
            String status,
            String sort
    ) {
        List<Task> tasks;

        if (keyword != null && !keyword.isBlank()) {
            tasks = taskRepository
                    .findByOwnerAndDescriptionContainingIgnoreCaseOrderByDeadlineAsc(
                            owner,
                            keyword.trim()
                    );
        } else if ("pending".equalsIgnoreCase(status)) {
            tasks = taskRepository.findByOwnerAndCompletedOrderByDeadlineAsc(owner, false);
        } else if ("completed".equalsIgnoreCase(status)) {
            tasks = taskRepository.findByOwnerAndCompletedOrderByDeadlineAsc(owner, true);
        } else {
            tasks = taskRepository.findByOwnerOrderByDeadlineAsc(owner);
        }

        if ("overdue".equalsIgnoreCase(status)) {
            tasks = tasks.stream()
                    .filter(Task::isOverdue)
                    .toList();
        }

        if ("deadlineDesc".equals(sort)) {
            tasks = tasks.stream()
                    .sorted(Comparator.comparing(Task::getDeadline).reversed())
                    .toList();
        } else if ("newest".equals(sort)) {
            tasks = tasks.stream()
                    .sorted(Comparator.comparing(Task::getCreatedAt).reversed())
                    .toList();
        }

        return tasks;
    }

    public Task createTask(
            AppUser owner,
            String description,
            LocalDate deadline,
            LocalDateTime reminderAt
    ) {
        validateTaskInput(description, deadline);

        Task task = new Task(
                description.trim(),
                deadline,
                reminderAt,
                owner
        );

        return taskRepository.save(task);
    }

    public void completeTask(AppUser owner, Long taskId) {
        Task task = getOwnedTask(owner, taskId);
        task.setCompleted(true);
        taskRepository.save(task);
    }

    public void updateTask(
            AppUser owner,
            Long taskId,
            String description,
            LocalDate deadline,
            LocalDateTime reminderAt
    ) {
        validateTaskInput(description, deadline);

        Task task = getOwnedTask(owner, taskId);
        task.setDescription(description.trim());
        task.setDeadline(deadline);
        task.setReminderAt(reminderAt);

        taskRepository.save(task);
    }

    public void deleteTask(AppUser owner, Long taskId) {
        taskRepository.delete(getOwnedTask(owner, taskId));
    }

    public List<Task> getDueReminders(AppUser owner) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDay = now.plusDays(1);

        return taskRepository.findByOwnerOrderByDeadlineAsc(owner).stream()
                .filter(task -> !task.isCompleted())
                .filter(task -> task.getReminderAt() != null)
                .filter(task -> !task.getReminderAt().isBefore(now))
                .filter(task -> !task.getReminderAt().isAfter(nextDay))
                .toList();
    }

    private Task getOwnedTask(AppUser owner, Long taskId) {
        return taskRepository.findByIdAndOwner(taskId, owner)
                .orElseThrow(() -> new IllegalArgumentException("Task was not found."));
    }

    private void validateTaskInput(String description, LocalDate deadline) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A task description is required.");
        }

        if (deadline == null) {
            throw new IllegalArgumentException("A deadline is required.");
        }
    }
}
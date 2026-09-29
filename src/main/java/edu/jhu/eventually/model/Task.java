package edu.jhu.eventually.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "todo_tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDate deadline;

    private boolean completed = false;

    private LocalDateTime reminderAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AppUser owner;

    public Task() {
    }

    public Task(String description, LocalDate deadline, LocalDateTime reminderAt, AppUser owner) {
        this.description = description;
        this.deadline = deadline;
        this.reminderAt = reminderAt;
        this.owner = owner;
        this.completed = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getReminderAt() {
        return reminderAt;
    }

    public void setReminderAt(LocalDateTime reminderAt) {
        this.reminderAt = reminderAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public AppUser getOwner() {
        return owner;
    }

    public boolean isOverdue() {
        return !completed && deadline != null && deadline.isBefore(LocalDate.now());
    }

    public boolean isDueToday() {
        return !completed && deadline != null && deadline.isEqual(LocalDate.now());
    }
}
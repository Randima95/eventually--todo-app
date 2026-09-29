package edu.jhu.eventually.controller;

import edu.jhu.eventually.model.AppUser;
import edu.jhu.eventually.model.Task;
import edu.jhu.eventually.service.TaskService;
import edu.jhu.eventually.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String dashboard(
            Authentication authentication,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "deadlineAsc") String sort,
            Model model
    ) {
        AppUser currentUser = getCurrentUser(authentication);

        List<Task> tasks = taskService.getTasks(currentUser, keyword, status, sort);
        long totalTasks = taskService.getTasks(currentUser, "", "all", "deadlineAsc").size();
        long completedTasks = taskService.getTasks(currentUser, "", "completed", "deadlineAsc").size();

        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("sort", sort);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("reminders", taskService.getDueReminders(currentUser));
        model.addAttribute("totalTasks", totalTasks);
        model.addAttribute("completedTasks", completedTasks);

        return "dashboard";
    }

    @PostMapping("/tasks")
    public String createTask(
            Authentication authentication,
            @RequestParam String description,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate deadline,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime reminderAt,
            RedirectAttributes redirectAttributes
    ) {
        try {
            taskService.createTask(
                    getCurrentUser(authentication),
                    description,
                    deadline,
                    reminderAt
            );
            redirectAttributes.addFlashAttribute("successMessage", "Task added successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/complete")
    public String completeTask(
            Authentication authentication,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            taskService.completeTask(getCurrentUser(authentication), id);
            redirectAttributes.addFlashAttribute("successMessage", "Task marked as completed.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/edit")
    public String editTask(
            Authentication authentication,
            @PathVariable Long id,
            @RequestParam String description,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate deadline,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime reminderAt,
            RedirectAttributes redirectAttributes
    ) {
        try {
            taskService.updateTask(
                    getCurrentUser(authentication),
                    id,
                    description,
                    deadline,
                    reminderAt
            );
            redirectAttributes.addFlashAttribute("successMessage", "Task updated successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/tasks/{id}/delete")
    public String deleteTask(
            Authentication authentication,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            taskService.deleteTask(getCurrentUser(authentication), id);
            redirectAttributes.addFlashAttribute("successMessage", "Task deleted.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/";
    }

    private AppUser getCurrentUser(Authentication authentication) {
        return userService.getByUsername(authentication.getName());
    }
}
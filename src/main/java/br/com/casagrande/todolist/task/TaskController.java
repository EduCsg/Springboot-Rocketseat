package br.com.casagrande.todolist.task;

import br.com.casagrande.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        taskModel.setIdUser((UUID) request.getAttribute("idUser"));

        LocalDateTime currentDate = LocalDateTime.now();

        // Início < Atual || Término < Atual
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.badRequest().body("Insira datas válidas!");
        }

        // Início > Término
        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.badRequest().body("A data de início deve ser menor que a data de término");
        }

        TaskModel task = this.taskRepository.save(taskModel);

        return ResponseEntity.ok(task);
    }

    @GetMapping("/")
    public List<TaskModel> listById(HttpServletRequest request) {
        Object _idUser = request.getAttribute("idUser");

        List<TaskModel> tasks = this.taskRepository.findByIdUser((UUID) _idUser);

        if (tasks.isEmpty()) {
            return null;
        }

        return tasks;
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> update(@RequestBody TaskModel taskModel, @PathVariable UUID taskId) {
        Optional<TaskModel> updatedTask = this.taskRepository.findById(taskId);

        if (updatedTask.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Utils.copyNonNullProperties(taskModel, updatedTask.get());

        this.taskRepository.save(updatedTask.get());

        return ResponseEntity.ok(updatedTask.get());
    }
}

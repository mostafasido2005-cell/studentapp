package com.example.studentapp.controller;

import com.example.studentapp.model.Student;
import com.example.studentapp.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Read - عرض كل الطلاب مع Search
    @GetMapping("/")
    public String listStudents(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("students", studentRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("students", studentRepository.findAll());
        }
        model.addAttribute("search", search);
        return "index";
    }

    // Create - فورم إضافة طالب
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    // Create - حفظ الطالب
    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student) {
        studentRepository.save(student);
        return "redirect:/";
    }

    // Update - فورم تعديل طالب
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow();
        model.addAttribute("student", student);
        return "edit-student";
    }

    // Update - حفظ التعديل
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        studentRepository.save(student);
        return "redirect:/";
    }

    // Delete - حذف طالب
    @Transactional
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        entityManager.createNativeQuery("ALTER TABLE student AUTO_INCREMENT = 1").executeUpdate();
        return "redirect:/";
    }
}
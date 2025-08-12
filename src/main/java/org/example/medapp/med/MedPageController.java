package org.example.medapp.med;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/")
public class MedPageController {
    private final MedRepository repo;
    public MedPageController(MedRepository repo) { this.repo = repo; }

    // Список + поиск
    @GetMapping
    public String list(@RequestParam(value="q", required=false) String q, Model model) {
        List<Med> items = (q == null || q.isBlank())
                ? repo.findAll().stream().sorted((a,b)->a.getName().compareToIgnoreCase(b.getName())).toList()
                : repo.findByNameContainingIgnoreCaseOrderByNameAsc(q);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("items", items);
        return "list";
    }

    // Создание
    @GetMapping("/meds/new")
    public String createForm(Model model) {
        model.addAttribute("m", new Med());
        return "form";
    }

    @PostMapping("/meds")
    public String create(@ModelAttribute("m") @Valid Med m) {
        m.setUpdatedAt(System.currentTimeMillis());
        repo.save(m);
        return "redirect:/";
    }

    // Редактирование
    @GetMapping("/meds/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("m", repo.findById(id).orElseThrow());
        return "form";
    }

    @PostMapping("/meds/{id}")
    public String update(@PathVariable String id, @ModelAttribute("m") @Valid Med m) {
        m.setId(id);
        m.setUpdatedAt(System.currentTimeMillis());
        repo.save(m);
        return "redirect:/";
    }

    // Детали
    @GetMapping("/meds/{id}")
    public String details(@PathVariable String id, Model model) {
        model.addAttribute("m", repo.findById(id).orElseThrow());
        return "details";
    }

    // Удаление (по желанию)
    @PostMapping("/meds/{id}/delete")
    public String delete(@PathVariable String id) {
        repo.deleteById(id);
        return "redirect:/";
    }
}
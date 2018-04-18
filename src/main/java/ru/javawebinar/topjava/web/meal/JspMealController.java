package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static org.springframework.format.annotation.DateTimeFormat.*;

@Controller
@RequestMapping(path = "/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable Integer id) {
        model.addAttribute("meal", super.get(id));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @PostMapping("/save")
    public String save(HttpServletRequest request) {
        String id = request.getParameter("id");
        Meal meal = new Meal(id.isEmpty() ? null: Integer.parseInt(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, meal.getId());
        }
        return "redirect:/meals";
    }

    @PostMapping("/filter")
    public String filter(@DateTimeFormat(iso = ISO.DATE) @RequestParam LocalDate startDate,
                         @DateTimeFormat(iso = ISO.TIME) @RequestParam LocalTime startTime,
                         @DateTimeFormat(iso = ISO.DATE) @RequestParam LocalDate endDate,
                         @DateTimeFormat(iso = ISO.TIME) @RequestParam LocalTime endTime, Model model) {
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}

package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        //action=edit
        if("edit".equals(action)) {
            Meal meal = repository.get(getId(req));
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("/editMeal.jsp").forward(req, resp);
        }
        //action=add
        else if("add".equals(action)) {
            req.setAttribute("meal", new Meal(LocalDateTime.now().withNano(0), "some desc", 500));
            req.getRequestDispatcher("/editMeal.jsp").forward(req, resp);
        }
        //action=null || action=delete
        else {
            if ("delete".equals(action)) {
                repository.delete(getId(req));
            }
            List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(repository.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
            req.setAttribute("meals", meals);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        repository.save(new Meal(
                getId(req),
                LocalDateTime.parse(req.getParameter("dateTime")),
                req.getParameter("description"),
                Integer.parseInt(req.getParameter("calories"))
        ));

        resp.sendRedirect("meals");
    }

    private Integer getId(HttpServletRequest req) {
        String id = req.getParameter("id");
        return !"".equals(id) ? Integer.parseInt(id) : null;
    }
}

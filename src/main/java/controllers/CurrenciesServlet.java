package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        StringBuilder jsonBuilder = new StringBuilder("[");
        try (PrintWriter printWriter = resp.getWriter()) {
            currencyService.findAll().forEach(currencyDto -> {
                jsonBuilder.append(currencyDto.toString()).append(",");
            });
            if (jsonBuilder.length() > 1) {
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
            }
            printWriter.write(jsonBuilder.append("]").toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (code == null || name == null || sign == null) {
            resp.sendError(400, "Invalid currency data");
        }
        if (currencyService.findCurrencyByCode(code).isEmpty()) {
            currencyService.addCurrency(code, name, sign);
        } else {
            resp.sendError(409, "This currency already exists");
        }

    }
}
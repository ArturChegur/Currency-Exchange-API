package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(buildJson());
        } catch (SQLException e) {
            resp.sendError(500, "Problems with the database");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (code == null || name == null || sign == null) {
            resp.sendError(400, "Required currency data field is missing");
        }
        try {
            if (!currencyService.isCurrencyExists(code)) {
                currencyService.addCurrency(code, name, sign);
            } else {
                resp.sendError(409, "This currency already exists");
            }
        } catch (SQLException e) {
            resp.sendError(500, "Problems with database");
        }
    }

    private String buildJson() throws SQLException {
        StringBuilder jsonBuilder = new StringBuilder("[");
        currencyService.findAll().forEach(currencyDto -> {
            jsonBuilder.append(currencyDto.toString()).append(",");
        });
        if (jsonBuilder.length() > 1) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }
        return jsonBuilder.append("]").toString();
    }
}
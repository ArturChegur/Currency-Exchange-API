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


@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(400, "URL endpoint is empty");
        }
        try (PrintWriter printWriter = resp.getWriter()) {
            String currencyCode = req.getPathInfo().substring(1);
            if (currencyService.isCurrencyExists(currencyCode)) {
                printWriter.write(currencyService.findCurrencyByCode(currencyCode).toString());
            } else {
                resp.sendError(404, "Currency not found");
            }
        } catch (SQLException e) {
            resp.sendError(500, "Problems with the database");
        }
    }
}

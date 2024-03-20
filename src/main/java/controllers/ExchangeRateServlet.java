package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() == null || req.getPathInfo().equals("/")) {
            resp.sendError(400, "URL endpoint is empty");
            return;
        }
        try (PrintWriter printWriter = resp.getWriter()) {
            String exchangeRateCodePair = req.getPathInfo().substring(1);
            if (exchangeRateService.exists(exchangeRateCodePair)) {
                printWriter.write(exchangeRateService.findByCode(exchangeRateCodePair).toString());
            } else {
                resp.sendError(404, "Exchange rate not found");
            }

        } catch (SQLException e) {
            resp.sendError(500, "Problems with the database");
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() == null || req.getPathInfo().equals("/") || req.getPathInfo().length() < 7) {
            resp.sendError(400, "URL endpoint is empty");
            return;
        }
        String rate = req.getParameter("rate");
        if (rate == null) {
            resp.sendError(400, "Required currency data field is missing");
        }
        try {
            rate = req.getParameter("rate");
            String codePair = req.getPathInfo().substring(1, 7);
            if (exchangeRateService.exists(codePair)) {
                exchangeRateService.update(codePair, rate);
            } else {
                resp.sendError(404, "Exchange rate not found");
            }
        } catch (SQLException e) {
            resp.sendError(500, "Problems with the database");
        }
    }
}
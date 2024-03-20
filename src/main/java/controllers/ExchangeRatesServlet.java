package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import service.ExchangeRateService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;


@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try (PrintWriter printWriter = resp.getWriter()) {
            printWriter.write(buildJson());
        } catch (SQLException e) {
            resp.sendError(500, "Problems with the database");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");
        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            resp.sendError(400, "Invalid parameters for exchange rate");
        }
        try {
            if (currencyService.exists(baseCurrencyCode) && currencyService.exists(targetCurrencyCode)) {
                if (!exchangeRateService.exists(baseCurrencyCode + targetCurrencyCode)) {
                    exchangeRateService.add(baseCurrencyCode, targetCurrencyCode, rate);
                } else {
                    resp.sendError(409, "This exchange rate already exists");
                }
            } else {
                resp.sendError(404, "Currency might not exist");
            }
        } catch (SQLException e) {
            resp.sendError(500, "Problems with the database");
        }
    }

    private String buildJson() throws SQLException {
        StringBuilder jsonBuilder = new StringBuilder("[");
        exchangeRateService.findAll().forEach(exchangeRateDto -> {
            jsonBuilder.append(exchangeRateDto.toString()).append(",");
        });
        if (jsonBuilder.length() > 1) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }
        return jsonBuilder.append("]").toString();
    }
}

package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        StringBuilder jsonBuilder = new StringBuilder("[");
        try (var printWriter = resp.getWriter()) {
            exchangeRateService.findAll().forEach(exchangeRateDto -> {
                jsonBuilder.append(exchangeRateDto.toString()).append(",");
            });
            if (jsonBuilder.length() > 1) {
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
            }
            printWriter.write(jsonBuilder.append("]").toString());
        }
    }
}

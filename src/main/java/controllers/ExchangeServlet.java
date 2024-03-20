package controllers;

import dto.CurrencyDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;
import service.ExchangeRateService;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String amount = req.getParameter("amount");
        if (to == null || from == null || amount == null) {
            resp.sendError(400, "URL endpoint is empty");
            return;
        }
        try (PrintWriter printWriter = resp.getWriter()) {
            if (exchangeRateService.exists(from + to)) {
                printWriter.write(buildExchange(from, to, amount, true, false));
            } else if (exchangeRateService.exists(to + from)) {
                printWriter.write(buildExchange(from, to, amount, false, false));
            } else if (exchangeRateService.exists("USD" + from) && exchangeRateService.exists("USD" + to)) {
                printWriter.write(buildExchange(from, to, amount, true, true));
            } else {
                resp.sendError(404, "Don`t have enough resources to make conversion");
            }
        } catch (SQLException e) {
            resp.sendError(500, "Problems with the database");
        }
    }

    private String buildExchange(String from, String to, String amount, boolean isDirectConversion, boolean isIntermediateConversion) throws SQLException {
        CurrencyDto currencyFrom = currencyService.findByCode(from);
        CurrencyDto currencyTo = currencyService.findByCode(to);
        BigDecimal amountOfCurrency = new BigDecimal(amount);
        BigDecimal rate;
        if (isIntermediateConversion) {
            BigDecimal rateFrom = exchangeRateService.findByCode("USD" + from).getRate();
            BigDecimal rateTo = exchangeRateService.findByCode("USD" + to).getRate();
            rate = rateTo.divide(rateFrom, 5, RoundingMode.HALF_UP);
        } else if (isDirectConversion) {
            rate = exchangeRateService.findByCode(from + to).getRate();
        } else {
            rate = exchangeRateService.findByCode(to + from).getRate();
            rate = BigDecimal.ONE.divide(rate, 5, RoundingMode.HALF_UP);
        }
        return "{" + "\"baseCurrency\": " + currencyFrom.toString() + ", " +
               "\"targetCurrency\": " + currencyTo.toString() + ", " +
               "\"rate\": " + rate + ", " +
               "\"amount\": " + amount + ", " +
               "\"convertedAmount\": " + rate.multiply(amountOfCurrency) +
               "}";
    }
}
package controllers;

import dto.CurrencyDto;
import dto.ExchangeRateDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() == 7) {
            String currencies = pathInfo.substring(1);
            String baseCurrency = currencies.substring(0, 3);
            String targetCurrency = currencies.substring(3);
            try (var printWriter = resp.getWriter()) {
                Optional<ExchangeRateDto> exchangeRateDto = Optional.ofNullable(exchangeRateService.findExchangeRateByCodePair(null));
                if (exchangeRateDto.isPresent()) {
                    printWriter.write(exchangeRateDto.get().toString());
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package controllers;

import dto.CurrencyDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String[] uriSegments = req.getRequestURI().split("/");
        System.out.println(Arrays.toString(uriSegments));
        try (var printWriter = resp.getWriter()) {
            if (uriSegments.length > 2) {
                String exchangeRateCodePair = uriSegments[uriSegments.length - 1];
                // доделать отправку раздельно парамтеров для метода поиска валюты по кодам
                Optional<CurrencyDto> currencyDto = exchangeRateService.findByCodePair();
                if (currencyDto.isPresent()) {
                    printWriter.write(currencyDto.get().toString());
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code");
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency endpoint");
            }
        }
        try (var printWriter = resp.getWriter()) {
            exchangeRateService.findAll().forEach(exchangeRateDto -> {
                jsonBuilder.append(exchangeRateDto.toString()).append(",");
            });
        }
    }
}

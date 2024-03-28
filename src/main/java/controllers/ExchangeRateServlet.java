package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.RequestExchangeRateDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private final ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

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
        String path = req.getPathInfo().substring(1);
        RequestExchangeRateDto request = new RequestExchangeRateDto();
        request.setBaseCurrency(path.substring(0, 3));
        request.setTargetCurrency(path.substring(3, 6));
        resp.getWriter().write(mapper.writeValueAsString(exchangeRateService.findByCode(request)));
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {
        String rate = req.getParameter("rate");
        String path = req.getPathInfo().substring(1);
        RequestExchangeRateDto request = new RequestExchangeRateDto();
        request.setBaseCurrency(path.substring(1, 3));
        request.setBaseCurrency(path.substring(3, 6));
        request.setRate(new BigDecimal(rate));
        exchangeRateService.update(request);
    }
}
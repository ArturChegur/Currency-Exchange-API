package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.RequestCurrencyDto;
import dto.ResponseCurrencyDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyCode = req.getPathInfo().substring(1); //todo validation
        RequestCurrencyDto request = new RequestCurrencyDto();
        request.setCode(currencyCode);
        ResponseCurrencyDto response = currencyService.findByCode(request);
        mapper.writeValueAsString(response);
        resp.getWriter().write(mapper.writeValueAsString(response));
    }
}
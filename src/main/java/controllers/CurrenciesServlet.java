package controllers;

import dto.CurrencyDto;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private static final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (var printWriter= resp.getWriter()) {
            printWriter.write("<h1>spisok</h1>");
            System.out.println(currencyService.findAll());
            currencyService.findAll().forEach(currencyDto -> {
                printWriter.write(currencyDto.toString() + " ff");
            });
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
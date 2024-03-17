package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CurrencyService;

import java.io.IOException;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //todo прописать все коды ошибок научится пробрасывать их с самого низа
        // 200 если все в порядке и 500 если проблемы на стороне сервера
        resp.setContentType("application/json");
        StringBuilder jsonBuilder = new StringBuilder("[");
        try (var printWriter = resp.getWriter()) {
            currencyService.findAll().forEach(currencyDto -> {
                jsonBuilder.append(currencyDto.toString()).append(",");
            });
            if (jsonBuilder.length() > 1) {
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
            }
            printWriter.write(jsonBuilder.append("]").toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //todo post method in localhost:8080/currencies endpoint
    }
}
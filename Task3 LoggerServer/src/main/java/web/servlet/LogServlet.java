package web.servlet;


import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.exceptions.TransactionException;
import entity.Log;
import entity.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import service.LoggingService;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LogServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();

    private LoggingService loggingService;

    @Override
    public void init() throws ServletException {
        loggingService = (LoggingService) getServletContext().getAttribute("loggingService");
        if (loggingService == null) {
            logger.fatal("Could not initialize servlet from application context");
            throw new UnavailableException(
                    "Could not get logging service.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        logger.info("Get /logs request entered.");
        List<Log> logs;
        String format = req.getParameter("format");
        try {
            logs = loggingService.getLogs();
            if ("pretty".equals(format)) {
                Gson gson = Converters.registerDateTime(new GsonBuilder()).create();
                String jsonLogs = gson.toJson(logs);
                resp.getWriter().write(jsonLogs);
            } else {
                req.setAttribute("logs", logs);
                req.getRequestDispatcher("logsJSP").forward(req, resp);
            }
        } catch (TransactionException e) {
            logger.error("Getting logs results into transaction exception.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        long dateMills = Long.parseLong(req.getParameter("date"));
        String typeName = req.getParameter("type");
        String message = req.getParameter("message");

        Log log = new Log(name, message, new DateTime(dateMills), Type.valueOf(typeName));

        try {
            loggingService.saveLog(log);
            logger.info("Log '{}' saved", log.getMessage());
        } catch (TransactionException e) {
            logger.error("Saving log results into transaction exception.", e);
            resp.sendError(500);
        }
    }
}
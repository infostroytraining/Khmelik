package web.servlet;


import db.exceptions.TransactionException;
import entity.Log;
import entity.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.LoggingService;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
        List<Log> logs;
        try {
            logs = loggingService.getLogs();
            req.setAttribute("logs", logs);
            req.getRequestDispatcher("logsJSP").forward(req, resp);
        } catch (TransactionException e) {
            logger.error("Getting logs results into transaction exception.", e);
            resp.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        //get parameters
        String name = req.getParameter("name");
        long date = Long.parseLong(req.getParameter("date"));
        String typeName = req.getParameter("type");
        String message = req.getParameter("message");

        Log log = new Log();
        log.setName(name);
        log.setMessage(message);
        log.setDate(new Date(date));
        log.setType(Type.valueOf(typeName));

        try {
            loggingService.saveLog(log);
            resp.setStatus(200);
            resp.sendRedirect("logs");
        } catch (TransactionException e) {
            logger.error("Saving log results into transaction exception.", e);
            resp.sendError(500);
        }
    }
}

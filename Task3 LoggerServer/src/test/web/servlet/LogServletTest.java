package web.servlet;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.GsonBuilder;
import db.exceptions.TransactionException;
import entity.Log;
import entity.Type;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.LoggingService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogServletTest {

    private static final String LOGGING_SERVICE_CONTEXT_ATTRIBUTE = "loggingService";
    private LogServlet servlet = new LogServlet();

    @Mock
    private ServletConfig servletConfig;
    @Mock
    private ServletContext servletContext;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private LoggingService loggingService;
    @Mock
    private PrintWriter printWriter;

    private Log testLog;
    private ArrayList<Log> testLogs;
    private String testLogsJson;

    @Before
    public void setUp() throws Exception {
        testLog = new Log("logEvent", "Test log message", new DateTime(123), Type.TRACE);
        testLogs = new ArrayList<>();
        testLogs.add(testLog);
        testLogsJson =
                Converters.registerDateTime(new GsonBuilder()).create().toJson(testLogs);

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute(LOGGING_SERVICE_CONTEXT_ATTRIBUTE)).thenReturn(loggingService);

        when(request.getServletContext()).thenReturn(servletContext);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(response.getWriter()).thenReturn(printWriter);

        when(loggingService.getLogs()).thenReturn(testLogs);
        when(loggingService.saveLog(testLog)).thenReturn(testLog);
    }

    @Test
    public void testInit() throws Exception {
        servlet.init(servletConfig);
    }

    @Test(expected = UnavailableException.class)
    public void testInitFail() throws Exception {
        when(servletContext.getAttribute(LOGGING_SERVICE_CONTEXT_ATTRIBUTE)).thenReturn(null);
        servlet.init(servletConfig);
    }

    @Test
    public void testDoGet() throws Exception {
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(loggingService).getLogs();
        verify(request).setAttribute("logs", testLogs);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetPretty() throws Exception {
        when(request.getParameter("format")).thenReturn("pretty");
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(loggingService).getLogs();
        verify(printWriter).write(testLogsJson);
    }

    @Test
    public void testDoGetTransactionException() throws Exception {
        when(loggingService.getLogs()).thenThrow(TransactionException.class);
        servlet.init(servletConfig);
        servlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDoPost() throws Exception {
        servlet.init(servletConfig);
        setRequestParams(testLog);
        servlet.doPost(request, response);
        verify(loggingService).saveLog(testLog);
    }

    @Test
    public void testDoPostTransactionException() throws Exception {
        when(loggingService.saveLog(testLog)).thenThrow(TransactionException.class);
        setRequestParams(testLog);
        servlet.init(servletConfig);
        servlet.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void setRequestParams(Log log) {
        when(request.getParameter("name")).thenReturn(log.getName());
        when(request.getParameter("date")).thenReturn(String.valueOf(log.getDateTime().getMillis()));
        when(request.getParameter("type")).thenReturn(log.getType().name());
        when(request.getParameter("message")).thenReturn(log.getMessage());
    }
}
package util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LogUtil {
    private static volatile LogUtil instance;
    private static final Logger logger = Logger.getLogger("CustomerFeedbackLogger");

    private LogUtil() {
        try {
            // ✅ Just use defaults — no external log.properties
            if (logger.getHandlers().length == 0) {
                FileHandler fileHandler = new FileHandler("CustomerFeedbackSystem.log", true);
                fileHandler.setFormatter(new Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = sdf.format(new Date(record.getMillis()));
                        return String.format("[%s] [%s] %s%n",
                                time, record.getLevel(), record.getMessage());
                    }
                });
                logger.addHandler(fileHandler);
                logger.setUseParentHandlers(true);
                logger.setLevel(Level.ALL);
            }
        } catch (IOException e) {
            System.err.println("❌ Logging setup failed: " + e.getMessage());
        }
    }

    public static LogUtil getInstance() {
        if (instance == null) {
            synchronized (LogUtil.class) {
                if (instance == null) {
                    instance = new LogUtil();
                }
            }
        }
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }
}

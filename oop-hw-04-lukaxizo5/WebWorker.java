import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {
    private static final String ERROR = "err";
    public static final String INTERRUPT = "interrupted";
    private String urlString;
    private int rowIndex;
    private String status;
    private WebFrame webFrame;

    public WebWorker(String urlString, int rowIndex, WebFrame webFrame) {
        this.urlString = urlString;
        this.rowIndex = rowIndex;
        this.status = "";
        this.webFrame = webFrame;
    }

    @Override
    public void run() {
        try {
            webFrame.runningThreads.incrementAndGet();
            webFrame.updateRunningThreadsLabel();
            download();
            webFrame.runningThreads.decrementAndGet();
            webFrame.updateRunningThreadsLabel();
            webFrame.completedThreads.incrementAndGet();
            webFrame.updateCompletedThreadsLabel();

        } finally {
            webFrame.getLauncher().getSemaphore().release();
        }
    }

    // This is the core web/download i/o code...
    public void download() {
        InputStream input = null;
        StringBuilder contents = null;
        long startTime = System.currentTimeMillis();
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            int bytesRead = 0;
            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                bytesRead += len;
                Thread.sleep(100);
            }
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            if (!webFrame.stopped) {
                status = new SimpleDateFormat("HH:mm:ss").format(new Date(endTime)) + " " + elapsedTime + "ms " + bytesRead + " bytes";
                webFrame.updateGUI(status, rowIndex);
            }
        }
        // Otherwise control jumps to a catch...
        catch (MalformedURLException ignored) {
            webFrame.updateGUI(ERROR, rowIndex);
        } catch (InterruptedException exception) {
            webFrame.updateGUI(INTERRUPT, rowIndex);
        } catch (IOException ignored) {
            webFrame.updateGUI(ERROR, rowIndex);
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }
    }
}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebFrame extends JFrame {
    private JPanel panel;
    public DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton singleThreadFetchButton;
    private JButton concurrentFetchButton;
    private JTextField numThreadsField;
    private JLabel runningLabel;
    private JLabel completedLabel;
    private JLabel elapsedLabel;
    private JProgressBar progressBar;
    private JButton stopButton;

    public AtomicInteger runningThreads;
    public AtomicInteger completedThreads;
    private long startTime;
    private long endTime;
    private long elapsedTime;

    private String fileName;
    private Launcher launcher;

    public Lock lock;
    public volatile boolean stopped = false;

    public WebFrame(String fileName) throws IOException {
        super("WebLoader");
        this.fileName = fileName;
        runningThreads = new AtomicInteger(0);
        completedThreads = new AtomicInteger(0);
        lock = new ReentrantLock(true);
        createAndShowGUI();
    }

    public Launcher getLauncher() {
        return launcher;
    }

    private void createAndShowGUI() throws IOException {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        model = new DefaultTableModel(new String[] {"url", "status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        while (true) {
            String link = reader.readLine();
            if (link == null) {
                break;
            }
            model.addRow(new Object[]{link, ""});
        }
        panel.add(scrollPane);
        reader.close();
        singleThreadFetchButton = new JButton("Single Thread Fetch");
        singleThreadFetchButton.setMaximumSize(new Dimension(160, 50));
        concurrentFetchButton = new JButton("Concurrent Fetch");
        concurrentFetchButton.setMaximumSize(new Dimension(150, 50));
        numThreadsField = new JTextField(1);
        numThreadsField.setMaximumSize(new Dimension(50, 50));
        runningLabel = new JLabel("Running:");
        completedLabel = new JLabel("Completed:");
        elapsedLabel = new JLabel("Elapsed:");
        progressBar = new JProgressBar();
        progressBar.setMaximum(model.getRowCount());
        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        addListeners();
        panel.add(singleThreadFetchButton);
        panel.add(concurrentFetchButton);
        panel.add(numThreadsField);
        panel.add(runningLabel);
        panel.add(completedLabel);
        panel.add(elapsedLabel);
        panel.add(progressBar);
        panel.add(stopButton);
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addListeners() {
        singleThreadFetchButton.addActionListener(e -> {
            resetGUI();
            startTime = System.currentTimeMillis();
            launcher = new Launcher(1);
            launcher.start();
        });

        concurrentFetchButton.addActionListener(e -> {
            int numWorkers = numThreadsField.getText().isEmpty()  ? 1 : Integer.parseInt(numThreadsField.getText());
            resetGUI();
            startTime = System.currentTimeMillis();
            launcher = new Launcher(numWorkers);
            launcher.start();
        });

        stopButton.addActionListener(e -> {
            if (launcher != null && launcher.isAlive()) {
                stopped = true;
                enableFetchButtons();
                lock.lock();
                launcher.stopAllWorkers();
                lock.unlock();
                launcher.interrupt();
            }
        });
    }

    private synchronized void resetGUI() {
        singleThreadFetchButton.setEnabled(false);
        concurrentFetchButton.setEnabled(false);
        stopButton.setEnabled(true);
        numThreadsField.setText("");
        runningLabel.setText("Running:");
        completedLabel.setText("Completed:");
        elapsedLabel.setText("Elapsed:");
        progressBar.setValue(0);
        runningThreads.set(0);
        completedThreads.set(0);
        stopped = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt("", i, 1);
        }
    }

    public synchronized void updateGUI(String status, int rowIndex) {
        SwingUtilities.invokeLater(() -> {
            model.setValueAt(status, rowIndex, 1);
            updateRunningThreadsLabel();
            updateCompletedThreadsLabel();
            progressBar.setValue(completedThreads.get());
        });
    }

    public class Launcher extends Thread {
        private int numWorkers;
        private Semaphore semaphore;
        private ArrayList<WebWorker> webWorkers;
        private volatile boolean runningOnSingleThread;

        public Launcher(int numWorkers) {
            this.numWorkers = numWorkers;
            semaphore = new Semaphore(numWorkers);
            webWorkers = new ArrayList<>();
            runningOnSingleThread = (numWorkers == 1);
        }

        public Semaphore getSemaphore() {
            return semaphore;
        }

        @Override
        public void run() {
            runningThreads.incrementAndGet();
            updateRunningThreadsLabel();
            if (runningOnSingleThread) {
                runSingleThread();
            }
            else {
                runMultiThread();
            }
            if (!stopped) {
                for (WebWorker webWorker : webWorkers) {
                    try {
                        webWorker.join();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            updateElapsedLabel();
            enableFetchButtons();
            runningThreads.decrementAndGet();
            updateRunningThreadsLabel();
        }

        private void runMultiThread() {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (stopped || isInterrupted()) break;
                String currURL = model.getValueAt(i, 0).toString();
                try {
                    semaphore.acquire();
                    if (stopped || isInterrupted()) {
                        semaphore.release();
                        break;
                    }
                    WebWorker webWorker = new WebWorker(currURL, i, WebFrame.this);
                    webWorkers.add(webWorker);
                    webWorker.start();
                } catch (InterruptedException e) {
                    int finalI = i;
                    SwingUtilities.invokeLater(() -> {
                        model.setValueAt(WebWorker.INTERRUPT, finalI, 1);
                    });
                    break;
                }
            }
        }

        private void runSingleThread() {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (stopped || isInterrupted()) break;
                String currURL  = model.getValueAt(i, 0).toString();
                updateRunningThreadsLabel();
                WebWorker webWorker = new WebWorker(currURL, i, WebFrame.this);
                webWorkers.add(webWorker);
                try {
                    webWorker.start();
                    webWorker.join();
                } catch (InterruptedException e) {
                    lock.lock();
                    updateGUI(WebWorker.INTERRUPT, i);
                    updateRunningThreadsLabel();
                    lock.unlock();
                    break;
                }
            }
        }

        private void stopAllWorkers() {
            lock.lock();
            for (WebWorker webWorker : webWorkers) {
                webWorker.interrupt();
            }
            lock.unlock();
        }
    }

    public synchronized void updateRunningThreadsLabel() {
        SwingUtilities.invokeLater(() -> {
            runningLabel.setText("Running:" +  runningThreads.get());
        });
    }

    public synchronized void updateCompletedThreadsLabel() {
        SwingUtilities.invokeLater(() -> {
            completedLabel.setText("Completed:" + completedThreads.get());
            progressBar.setValue(completedThreads.get());
        });
    }

    private synchronized void updateElapsedLabel() {
        SwingUtilities.invokeLater(() -> {
            elapsedLabel.setText("Elapsed:" + elapsedTime);
        });
    }

    private synchronized void enableFetchButtons() {
        SwingUtilities.invokeLater(() -> {
            singleThreadFetchButton.setEnabled(true);
            concurrentFetchButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
    }

    public static void main(String[] args) throws IOException {
        WebFrame frame = new WebFrame("links.txt");
    }
}
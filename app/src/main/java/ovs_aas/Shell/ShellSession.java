package ovs_aas.Shell;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ShellSession {
    private static final long WAITING_TIME = 10;
    private static final String KILL_CONTROLLER = "pkill ryu-manager";

    public void executeCommand(String command, String session, Boolean waitForResult) throws IOException, InterruptedException {
        Process process = getShell(session, command);

        if (waitForResult)
            process.waitFor(WAITING_TIME, TimeUnit.SECONDS);
    }

    private Process getShell(String session, String command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        return pb.start();
    }

    public void destroyProcess() {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", KILL_CONTROLLER);
        try {
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

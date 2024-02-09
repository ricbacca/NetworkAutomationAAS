package ovs_aas.Submodels.Machinery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ovs_aas.Submodels.Utils.SSHManager;

public class PingTest {
    private SSHManager sshManager = new SSHManager();

    public String pingTest(String pingDestination, String sshHost) {
        String command = ("ping -c 2 " + pingDestination);
        String input = sshManager.executeSingleCommand(command, sshHost);

        Pattern pattern = Pattern.compile("([0-9]+ packets transmitted, [0-9]+ received)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Nessuna corrispondenza trovata";
        }
    }
}
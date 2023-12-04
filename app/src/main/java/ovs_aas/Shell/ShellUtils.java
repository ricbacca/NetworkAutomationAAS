// Copyright 2023 riccardo.bacca@studio.unibo.it
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package ovs_aas.Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShellUtils {
    private ShellSession sessionManager = new ShellSession();

    private final String FIREWALL = "ryu.app.rest_firewall";
    private final String SIMPLE_SWITCH = "ryu.app.simple_switch_13";
    private final String API_REST = "ryu.app.ofctl_rest --wsapi-port";
    private final String BASE_COMMAND = "ryu-manager --ofp-tcp-listen-port";

    public String ovsVersion() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "ovs-vsctl --version");

        return waitForResult(processBuilder);
    }

    private String waitForResult(ProcessBuilder processBuilder) {
        StringBuilder sb = new StringBuilder();

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();   
    }

    public String pingTest(String ip) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "ping -c 2 " + ip);

        String input = waitForResult(processBuilder);

        Pattern pattern = Pattern.compile("([0-9]+ packets transmitted, [0-9]+ received)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "Nessuna corrispondenza trovata";
        }
    }

    private String selectCommand(int switchNumber, boolean isClosedController, boolean isSelectiveController) {
        int tcpPort = (switchNumber == 1 ? 6633 : 6653);
        int apiRestPort = (switchNumber == 1 ? 8080 : 9090);
        
        String controllerProgram = 
            (isSelectiveController || (isClosedController && switchNumber == 1)) ?
                FIREWALL : SIMPLE_SWITCH;

        return String.format("%s %d %s %s %d", 
            BASE_COMMAND, tcpPort, controllerProgram, API_REST, apiRestPort);
    }

    public void setSimpleControllers(boolean status) {
        if (status) {
            try {
                sessionManager.executeCommand(selectCommand(1, false, false), "CNT1", false);
                sessionManager.executeCommand(selectCommand(2, false, false), "CNT2", false);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            sessionManager.destroyProcess();
        }
    }

    public void setClosedController(boolean status) {
        if (status) {
            try {
                sessionManager.executeCommand(selectCommand(1, true, false), "FIREWALL_CNT1", true);
                sessionManager.executeCommand(selectCommand(2, true, false), "FIREWALL_CNT2", false);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            sessionManager.destroyProcess();
        }
    }

    public void setSelectiveController(boolean status) {
        if (status) {
            try {
                sessionManager.executeCommand(selectCommand(1, false, true), "SELECT_CNT1", true);
                sessionManager.executeCommand(selectCommand(2, false, true), "SELECT_CNT2", true);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            sessionManager.destroyProcess();
        }
    }
}
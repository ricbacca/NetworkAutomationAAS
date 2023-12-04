// Copyright 2023 riccardo
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

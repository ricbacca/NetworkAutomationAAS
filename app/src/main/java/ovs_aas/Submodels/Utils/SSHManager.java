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

package ovs_aas.Submodels.Utils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHManager {
    private final String SSH_PSW = "111111";
    private final String SSH_USER = "root";
    private final Integer SSH_PORT = 22;
    private final Integer TIMEOUT =  30000;

    public void executeCommandOnShell(String command, Channel ch) {
        PrintStream ps;
        try {
            ps = new PrintStream(ch.getOutputStream());
            ps.println(command);
            ps.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String executeSingleCommand(String command, String host) {
        ChannelExec ch = (ChannelExec) this.channelInit("exec", host);
        ch.setCommand(command);
        return this.readChannelOutput(ch);
    }

    private String readChannelOutput(ChannelExec channel) {
        StringBuilder sb = new StringBuilder();

        try {            
            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.connect();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException | JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.getSession().disconnect();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();   
    }

    public Channel channelInit(String type, String sshHost) {
        Channel channel = null;
        try {
            Session session = this.setupSshSession(sshHost);
            session.connect(TIMEOUT);
            channel = session.openChannel(type);
        } catch (JSchException e) {
            e.printStackTrace();
        }

        return channel;
    }

    private Session setupSshSession(String SSH_HOST) {
        Session session = null;
        try {
            session = new JSch().getSession(SSH_USER, SSH_HOST, SSH_PORT);
            session.setPassword(SSH_PSW);
            session.setConfig("StrictHostKeyChecking", "no"); // disable check for RSA key
        } catch (JSchException e) {
            e.printStackTrace();      
        }
        
        return session;
    }
}

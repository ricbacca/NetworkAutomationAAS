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

package ovs_aas;

/**
 * To manage System properties, used to mantain info throughout the app
 * about which controller is on, and not cause any problems.
 */
public class StaticProperties {
    public static final String SSH_CNT1 = "100.0.0.1";
    public static final String SSH_CNT2 = "100.0.0.2";
    
    public static final String CNT1_IP = "http://100.0.0.1:8080";
    public static final String CNT2_IP = "http://100.0.0.2:9090";
    
    public static final String REGISTRY_POLLING_IP = "http://100.0.1.1:4000/registry/api/v1/registry";
    public static final String REGISTRYPATH = "http://100.0.1.1:4000/registry/";

    public static final String SW1_IP = "http://100.0.1.2:3333";
    public static final String SW2_IP = "http://100.0.1.4:4444";

    public static final int NetworkInfrastructurePort = 6001;
    public static final int NetworkControlPlanePort = 6002;
    public static final int MachineOnePort = 6003;
    public static final int MachineTwoPort = 6004;
    public static final int MachineThreePort = 6005;

    public static final String Host1 = "10.0.1.1";
    public static final String Host2 = "10.0.1.2";
    public static final String Host3 = "10.0.2.1";
    public static final String Host4 = "10.0.2.2";
    public static final String Host5 = "10.0.3.1";
    public static final String Host6 = "10.0.3.2";

    private static final String SIMPLE_CONTROLLERS = "SIMPLECONTROLLERS";
    private static final String CLOSED_CONTROLLERS = "CLOSEDCONTROLLERS";
    private static final String SELECTIVE_CONTROLLERS = "SELECTIVECONTROLLERS";

    private static boolean getStatus(String CONTROLLER) {
        return Boolean.parseBoolean(System.getProperty(CONTROLLER, "false"));
    }

    private static void setStatus(String CONTROLLER, boolean newStatus) {
        System.out.println("" +     CONTROLLER + ": " + newStatus);
        System.setProperty(CONTROLLER,  Boolean.toString(newStatus));
    }

    public static boolean isSimpleControllers() {
        return getStatus(SIMPLE_CONTROLLERS);
    }

    public static boolean isClosedControllers() {
        return getStatus(CLOSED_CONTROLLERS);
    }

    public static boolean isSelectiveControllers() {
        return getStatus(SELECTIVE_CONTROLLERS);
    }

    public static void setSimpleControllers(Boolean newValue) {
        setStatus(SIMPLE_CONTROLLERS, newValue);
    }

    public static void setClosedControllers(Boolean newValue) {
        setStatus(CLOSED_CONTROLLERS, newValue);
    }

    public static void setSelectiveControllers(Boolean newValue) {
        setStatus(SELECTIVE_CONTROLLERS, newValue);
    }
}
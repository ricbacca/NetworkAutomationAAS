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

package ovs_aas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class StaticProperties {

    private static final String FILE_NAME = "/home/riccardo/Desktop/OVS_AAS/app/src/main/resources/config.properties";
    private static final String SIMPLE_CONTROLLERS = "SIMPLECONTROLLERS";
    private static final String CLOSED_CONTROLLERS = "CLOSEDCONTROLLERS";
    private static final String SELECTIVE_CONTROLLERS = "SELECTIVECONTROLLERS";

    static private Properties openFile() {
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream(FILE_NAME);
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return props;
    }

    private static boolean getStatus(String CONTROLLER) {
        return Boolean.parseBoolean(openFile().getProperty(CONTROLLER, "false"));
    }

    private static void setStatus(String CONTROLLER, boolean newStatus) {
        Properties props = openFile();

        try {
            FileOutputStream out = new FileOutputStream(FILE_NAME);
            props.setProperty(CONTROLLER, Boolean.toString(newStatus));
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
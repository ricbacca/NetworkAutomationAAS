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

import com.fasterxml.jackson.core.type.TypeReference;

import ovs_aas.RyuController.Controller;
import ovs_aas.RyuController.Models.RoleEnum;
import ovs_aas.RyuController.Utils.ApiEnum;

public class RyuControllerManager {
    private Controller client = new Controller();

    public String getResponseWithoutSerial(String url) {
        try {
            return client.makeRequestWithoutSerialize(url);
        } catch (IOException | InterruptedException e) {}
        
        return null;
    }

    public <T> T getResponseWithSerial(String url, TypeReference<T> typeRef) {
        return client.makeRequestWithSerialization(url, typeRef);
    }

    public Integer setRole(String url, int switchNumber, RoleEnum role) {
        return client.setControllerRole(url, switchNumber, role.toString());
    }

    public void enableFirewallController(boolean bothSwitches) {
        System.out.print("Waiting for Firewalls to start-up");

        while(!client.isServerAvailable(ApiEnum.getElement(1, ApiEnum.GETFIREWALLRULES))
            || !client.isServerAvailable(ApiEnum.getElement(2, ApiEnum.GETFIREWALLRULES))) {
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (bothSwitches)
            client.putRequest(ApiEnum.getElement(2, ApiEnum.FIREWALL_ON));
        client.putRequest(ApiEnum.getElement(1, ApiEnum.FIREWALL_ON));
    }

    public Integer postFirewallRules(String url, String src, String dst) {
        return client.postFirewallRules(url, src, dst, "ALLOW");
    }

    public Integer deleteFirewallRules(String url, int rule_id) {
        return client.deleteFirewallRule(url, rule_id);
    }
}

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

package ovs_aas.Submodels.RyuApi;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;

import ovs_aas.ControllerAPI.Controller.ControllerClient;
import ovs_aas.ControllerAPI.Models.RoleEnum;

public class RestRequestRyu {
    private ControllerClient client = new ControllerClient();

    public String getResponseWithoutSerial(String url) {
        try {
            return client.makeRequestWithoutSerialize(url);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getResponseWithSerial(String url, TypeReference<T> typeRef) {
        return client.makeRequestWithSerialization(url, typeRef);
    }

    public Integer setRole(String url, int switchNumber, RoleEnum role) {
        return client.setControllerRole(url, switchNumber, role.toString());
    }

    public void enableFirewallController(boolean bothSwitches) {
        if (bothSwitches)
            client.putRequest(RyuApiEnum.CONTROLLER2_FIREWALL_ENABLE.url);
        client.putRequest(RyuApiEnum.CONTROLLER1_FIREWALL_ENABLE.url);
    }

    public Integer postFirewallRules(String url, int src, int dst) {
        return client.postFirewallRules(url, "10.0.0." + src, "10.0.0." + dst, "ALLOW");
    }

    public Integer deleteFirewallRules(String url, int rule_id) {
        return client.deleteFirewallRule(url, rule_id);
    }
}

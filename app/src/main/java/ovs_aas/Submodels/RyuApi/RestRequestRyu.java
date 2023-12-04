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

package ovs_aas.Submodels.Controller;

import java.math.BigInteger;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;

import com.fasterxml.jackson.core.type.TypeReference;

import ovs_aas.RyuController.Models.AggregateFlowStats;
import ovs_aas.RyuController.Models.AllFlowStats;
import ovs_aas.RyuController.Models.Role;
import ovs_aas.RyuController.Models.RoleEnum;
import ovs_aas.RyuController.Models.RuleImpl.AccessControlList;
import ovs_aas.RyuController.Controller;


public class ControllerLambda {

    private Controller client;

    public ControllerLambda() {
        this.client = new Controller();
    }

    /**
     * @param url controller IP
     * @return Aggregate Stats from Controller at given URL
     */
    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getAggregateStats(String url) {
        return (args) -> {
            Map<String, AggregateFlowStats[]> res = client.makeRequestWithSerialization(url, new TypeReference<Map<String, AggregateFlowStats[]>>(){});
            return res.values().stream().findFirst().get()[0].formatResult();
        };
    }

    /**
     * 
     * @param url controller IP
     * @return All Flow Stats for given Controller IP
     */
    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getAllFlowStats(String url) {
        return (args) -> {
            Map<String, AllFlowStats[]> res = client.makeRequestWithSerialization(
                url, 
                new TypeReference<Map<String, AllFlowStats[]>>(){});
            
            return AllFlowStats.formatMultipleResults(res.values().stream().collect(Collectors.toList()));
        };
    }

    /**
     * 
     * @param url for controller
     * @return actual Role for given Controller IP
     */
    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getRole(String url) {
        return (args) -> {
            Map<String, Role[]> res = client.makeRequestWithSerialization(
                url, 
                new TypeReference<Map<String, Role[]>>(){});
            
            return res.values().stream().findFirst().get()[0].formatResult();
        };
    }

    /**
     * 
     * @param url controller IP
     * @return Sets a new Role (got from Input Field) for controller IP
     */
    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setRole(String url) {
        return (args) -> {
            int statusCode = 0;
            String input = (String) args.get("Role").getValue();
            if (EnumUtils.isValidEnum(RoleEnum.class, input)) {
                statusCode = client.setControllerRole(url, 1, RoleEnum.valueOf(input).toString());
            } else statusCode = 400;
            
            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            }; 
        }; 
    }

    /**
     * @param url Controller IP on which to create the new Firewall Rule
     * @return Sets a new Firewall Rule with given Source and Destination IP
     */
    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setFirewallRule(String url) {
        return (args) -> {
            String src = args.get("Source").getValue() == null ? "" : args.get("Source").getValue().toString();
            String dst = args.get("Destination").getValue() == null ? "" : args.get("Destination").getValue().toString();
            String type = args.get("Type").getValue().toString();

            if (!(type == "ALLOW" || type == "DENY")) {
                return new SubmodelElement[] {
                    new Property("Type MUST be ALLOW or DENY !")
                };
            } else {
                int statusCode = client.postFirewallRules(url, src, dst, "ALLOW");

                return new SubmodelElement[] {
                    new Property("StatusCode: " + statusCode)
                };
            }
        };
    }

    /**
     * 
     * @param url controller IP
     * @return actual firewall rules from controller
     */
    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getFirewallRules(String url) {
        return (args) -> {
            AccessControlList[] res = client.makeRequestWithSerialization(url, new TypeReference<AccessControlList[]>(){});
            return res[0].formatRules();
        };
    }

    /**
     * 
     * @param url controller IP
     * @return deletes a specific firewall rule (ip given from input field)
     */
    public Function<Map<String, SubmodelElement>, SubmodelElement[]> deleteFirewallRules(String url) {
        return (args) -> {
            int statusCode = client.deleteFirewallRule(url, ((BigInteger) args.get("RuleId").getValue()).intValue());

            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            };
        };
    }
}

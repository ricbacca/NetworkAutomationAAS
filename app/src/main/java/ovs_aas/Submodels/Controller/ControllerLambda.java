package ovs_aas.Submodels.Controller;

import java.math.BigInteger;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;

import com.fasterxml.jackson.core.type.TypeReference;

import ovs_aas.RyuController.Models.AccessControlList;
import ovs_aas.RyuController.Models.AggregateFlowStats;
import ovs_aas.RyuController.Models.AllFlowStats;
import ovs_aas.RyuController.Models.Role;
import ovs_aas.RyuController.Models.RoleEnum;
import ovs_aas.Submodels.Utils.RyuControllerManager;

public class ControllerLambda {

    private RyuControllerManager controller;

    public ControllerLambda(RyuControllerManager controller) {
        this.controller = controller;
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getAggregateStats(String url) {
        return (args) -> {
            Map<String, AggregateFlowStats[]> res = controller.getResponseWithSerial(url, new TypeReference<Map<String, AggregateFlowStats[]>>(){});
            return res.values().stream().findFirst().get()[0].formatResult();
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getAllFlowStats(String url) {
        return (args) -> {
            Map<String, AllFlowStats[]> res = controller.getResponseWithSerial(
                url, 
                new TypeReference<Map<String, AllFlowStats[]>>(){});
            
            return AllFlowStats.formatMultipleResults(res.values().stream().collect(Collectors.toList()));
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getRole(String url) {
        return (args) -> {
            Map<String, Role[]> res = controller.getResponseWithSerial(
                url, 
                new TypeReference<Map<String, Role[]>>(){});
            
            return res.values().stream().findFirst().get()[0].formatResult();
        };
    }

        public Function<Map<String, SubmodelElement>, SubmodelElement[]> setRole(String url) {
        return (args) -> {
            int statusCode = 0;
            String input = (String) args.get("Role").getValue();
            if (EnumUtils.isValidEnum(RoleEnum.class, input)) {
                statusCode = controller.setRole(url, 1, RoleEnum.valueOf(input));
            } else statusCode = 400;
            
            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            }; 
        }; 
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setFirewallRule(String url) {
        return (args) -> {
            String src = args.get("Source").getValue() == null ? "" : args.get("Source").getValue().toString();
            String dst = args.get("Destination").getValue() == null ? "" : args.get("Destination").getValue().toString();
            
            int statusCode = controller.postFirewallRules(url, src, dst);

            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            };
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getFirewallRules(String url) {
        return (args) -> {
            AccessControlList[] res = controller.getResponseWithSerial(url, new TypeReference<AccessControlList[]>(){});
            return res[0].formatRules();
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> deleteFirewallRules(String url) {
        return (args) -> {
            int statusCode = controller.deleteFirewallRules(url, ((BigInteger) args.get("RuleId").getValue()).intValue());

            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            };
        };
    }
}

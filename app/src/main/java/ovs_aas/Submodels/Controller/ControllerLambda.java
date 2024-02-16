package ovs_aas.Submodels.Controller;

import java.math.BigInteger;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.apache.http.client.HttpResponseException;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.joda.time.IllegalFieldValueException;

import com.fasterxml.jackson.core.type.TypeReference;

import ovs_aas.RyuController.Models.AggregateFlowStats;
import ovs_aas.RyuController.Models.AllFlowStats;
import ovs_aas.RyuController.Models.Role;
import ovs_aas.RyuController.Models.RoleEnum;
import ovs_aas.RyuController.Models.RuleImpl.AccessControlList;
import ovs_aas.RyuController.Utils.ApiEnum;
import ovs_aas.RyuController.Controller;

enum firewallRulesType {ALLOW, DENY};

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
            String input = (String) args.get("Role").getValue();

            if (EnumUtils.isValidEnum(RoleEnum.class, input)) {
                try {
                    client.setControllerRole(url, 1, RoleEnum.valueOf(input).toString());
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                }
            }
            
            return new SubmodelElement[] {
                new Property("All OK")
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
            int priority = args.get("Priority").getValue() == null ? 1 : Integer.valueOf(args.get("Priority").getValue().toString());

            if (!EnumUtils.isValidEnum(firewallRulesType.class, type)){
                throw new IllegalFieldValueException("Type", type);
            }
            
            try {
                client.postFirewallRules(url, src, dst, type, priority);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            }

            return new SubmodelElement[] {
                new Property("All OK")
            };
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
            try {
                client.deleteFirewallRule(url, ((BigInteger) args.get("RuleId").getValue()).intValue());
            } catch (HttpResponseException e) {
                e.printStackTrace();
            }

            return new SubmodelElement[] {
                new Property("All OK")
            };
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> isolateSingleHost() {
        return (args) -> {
            String hostIP = args.get("HostIP").getValue() == null ? "" : args.get("HostIP").getValue().toString();

            String cnt1 = ApiEnum.getElement(1, ApiEnum.GETFIREWALLRULES);
            String cnt2 = ApiEnum.getElement(2, ApiEnum.GETFIREWALLRULES);

            if (hostIP.isBlank())
                throw new IllegalFieldValueException("HostIP", hostIP);

            try {
                // Consento tutto il traffico tra tutti
                client.postFirewallRules(cnt1, "", "", "ALLOW", 1);        
                client.postFirewallRules(cnt2, "", "", "ALLOW", 1);    

                // Nego solo questo possibile traffico, con maggiore priorità rispetto alla regola precedente
                client.postFirewallRules(cnt1, hostIP, "", "DENY", 10);    
                client.postFirewallRules(cnt1, "", hostIP, "DENY", 10);
                client.postFirewallRules(cnt2, hostIP, "", "DENY", 10);    
                client.postFirewallRules(cnt2, "", hostIP, "DENY", 10);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            }
            
            return new SubmodelElement[] {
                new Property("All OK")
            };
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> enableSingleHost() {
        return (args) -> {
            String hostIP = args.get("HostIP").getValue() == null ? "" : args.get("HostIP").getValue().toString();

            String cnt1 = ApiEnum.getElement(1, ApiEnum.GETFIREWALLRULES);
            String cnt2 = ApiEnum.getElement(2, ApiEnum.GETFIREWALLRULES);

            if (hostIP.isBlank())
                throw new IllegalFieldValueException("HostIP", hostIP);

            try {
                // Il firewall è default deny, quindi il traffico è di default bloccato verso tutti
                // Consento solo il traffico da HostIP verso tutti e viceversa
                client.postFirewallRules(cnt1, hostIP, "", "ALLOW", 1);    
                client.postFirewallRules(cnt1, "", hostIP, "ALLOW", 1);
                client.postFirewallRules(cnt2, hostIP, "", "ALLOW", 1);    
                client.postFirewallRules(cnt2, "", hostIP, "ALLOW", 1);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            }
            
            return new SubmodelElement[] {
                new Property("All OK")
            };
        };
    }
}

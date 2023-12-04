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

package ovs_aas.Submodels.Utils;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.haskind.ModelingKind;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;
import com.fasterxml.jackson.core.type.TypeReference;

import ovs_aas.StaticProperties;
import ovs_aas.ControllerAPI.Models.AggregateFlowStats;
import ovs_aas.ControllerAPI.Models.AllFlowStats;
import ovs_aas.ControllerAPI.Models.Role;
import ovs_aas.ControllerAPI.Models.RoleEnum;
import ovs_aas.ControllerAPI.Models.AccessControlList.AccessControlList;
import ovs_aas.Shell.ShellUtils;
import ovs_aas.Submodels.RyuApi.RestRequestRyu;
import ovs_aas.Submodels.RyuApi.RyuApiEnum;

enum Switch {SIMPLE, CLOSED, SELECTIVE};

public class LambdaProvider {
    private RestRequestRyu controller;
    private ShellUtils shellUtils;
    private Utils utils;

    public LambdaProvider(Utils utils, ShellUtils shellUtils, RestRequestRyu controller) {
        this.shellUtils = shellUtils;
        this.utils = utils;
        this.controller = controller;
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getSwitchesNumber() {
        return (args) -> {
            SubmodelElement cnt1 = new Property("Controller1", ValueType.String);
            cnt1.setValue("Controller1 -> switch:" + 
                controller.getResponseWithoutSerial(RyuApiEnum.CONTROLLER1_GETALLSWITCHES.url));
            cnt1.setKind(ModelingKind.TEMPLATE);

            SubmodelElement cnt2 = new Property("Controller2", ValueType.String);
            cnt2.setValue("Controller2 -> switch:" + 
                controller.getResponseWithoutSerial(RyuApiEnum.CONTROLLER2_GETALLSWITCHES.url));
            cnt2.setKind(ModelingKind.TEMPLATE);

            return new SubmodelElement[] {
                cnt1, cnt2
            };
        };
    }

    private void checkEnvironment(boolean controllerInput, Switch applicant) {
        boolean closedController = StaticProperties.isClosedControllers();
        boolean simpleController = StaticProperties.isSimpleControllers();
        boolean selectiveController = StaticProperties.isSelectiveControllers();

        boolean throwException = false;

        switch (applicant) {
            case SIMPLE:
                if ((simpleController && controllerInput) || (!simpleController && !controllerInput) || (closedController || selectiveController))
                    throwException = true;
                break;
            case CLOSED:
                if ((closedController && controllerInput) || (!closedController && !controllerInput) || (simpleController || selectiveController))
                    throwException = true;
                break;
            case SELECTIVE:
                if ((selectiveController && controllerInput) || (!selectiveController && !controllerInput) || (simpleController || closedController))
                    throwException = true;
                break;
            default:
                throwException = false;
                break;
        }

        if (throwException)
            throw new IllegalArgumentException("Problem with controller status !");
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setSimpleSwitchControllers() {
        return (args) -> {
            Boolean controllerStatus = utils.getOrElse(args.get("Controller").getValue());

            checkEnvironment(controllerStatus, Switch.SIMPLE);

            shellUtils.setSimpleControllers(controllerStatus);

            StaticProperties.setSimpleControllers(controllerStatus);
            return new SubmodelElement[] {
                new Property(controllerStatus ? "Activated" : "Deactivated"),
            };
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setClosedControllers() {
        return (args) -> {
            Boolean controllerStatus = utils.getOrElse(args.get("Controller").getValue());

            checkEnvironment(controllerStatus, Switch.CLOSED);

            shellUtils.setClosedController(controllerStatus);
            if (controllerStatus)
                controller.enableFirewallController(false);
            StaticProperties.setClosedControllers(controllerStatus);
            
            return new SubmodelElement[] {
                new Property(controllerStatus ? "Activated" : "Deactivated"),
            };
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setSelectiveControllers(String sw1Selection, String sw2Selection) {
        return (args) -> {
            Boolean controllerStatus = utils.getOrElse(args.get("Controller").getValue());

            checkEnvironment(controllerStatus, Switch.SELECTIVE);

            shellUtils.setSelectiveController(controllerStatus);
            if (controllerStatus)
                controller.enableFirewallController(true);
            StaticProperties.setSelectiveControllers(controllerStatus);

            return new SubmodelElement[] {
                new Property(controllerStatus ? "Activated" : "Deactivated"),
            };

        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getAggregateStats(RyuApiEnum element) {
        return (args) -> {
            Map<String, AggregateFlowStats[]> res = controller.getResponseWithSerial(element.url, new TypeReference<Map<String, AggregateFlowStats[]>>(){});
            return res.values().stream().findFirst().get()[0].formatResult();
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getAllFlowStats(RyuApiEnum element) {
        return (args) -> {
            Map<String, AllFlowStats[]> res = controller.getResponseWithSerial(
                element.url, 
                new TypeReference<Map<String, AllFlowStats[]>>(){});
            
            return AllFlowStats.formatMultipleResults(res.values().stream().collect(Collectors.toList()));
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getRole(RyuApiEnum element) {
        return (args) -> {
            Map<String, Role[]> res = controller.getResponseWithSerial(
                element.url, 
                new TypeReference<Map<String, Role[]>>(){});
            
            return res.values().stream().findFirst().get()[0].formatResult();
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setRole(RyuApiEnum element) {
        return (args) -> {
            int statusCode = 0;
            String input = (String) args.get("Role").getValue();
            if (EnumUtils.isValidEnum(RoleEnum.class, input)) {
                statusCode = controller.setRole(element.url, 1, RoleEnum.valueOf(input));
            } else statusCode = 400;
            
            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            }; 
        }; 
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> setFirewallRule(RyuApiEnum element) {
        return (args) -> {
            int src = args.get("Source").getValue() == null ? 0 : ((BigInteger) args.get("Source").getValue()).intValue();
            int dst = args.get("Destination").getValue() == null ? 0 : ((BigInteger) args.get("Destination").getValue()).intValue();
            checkHosts(src, dst);
            
            int statusCode = controller.postFirewallRules(element.url, src, dst);

            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            };
        };
    }

    private void checkHosts(int src, int dst) {
        if (src != 0 && dst != 0) {
            if (!between(src, 1, 6) || !between(dst, 1, 6)) {
                throw new IllegalArgumentException("Src and Dst must be between 1 and 6 !");
            }
        }
    }

    public static boolean between(int variable, int minValueInclusive, int maxValueInclusive) {
        return variable >= minValueInclusive && variable <= maxValueInclusive;
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getFirewallRules(RyuApiEnum element) {
        return (args) -> {
            AccessControlList[] res = controller.getResponseWithSerial(element.url, new TypeReference<AccessControlList[]>(){});
            return res[0].formatRules();
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> deleteFirewallRules(RyuApiEnum element) {
        return (args) -> {
            int statusCode = controller.deleteFirewallRules(element.url, ((BigInteger) args.get("RuleId").getValue()).intValue());

            return new SubmodelElement[] {
                new Property("StatusCode: " + statusCode)
            };
        };
    }

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> pingMachinery(List<String> machineHosts) {
        return (args) -> {
            int host = ((BigInteger) args.get("To").getValue()).intValue();

            if (hostNumbers(machineHosts).contains(host))
                return new SubmodelElement[] {
                    new Property("Result: " + shellUtils.pingTest("10.0.0." + host))
                };
            else
                return new SubmodelElement[] {
                    new Property("Host not in this Machinery !")
                };



        };
    }

    private List<Integer> hostNumbers(List<String> machineHosts) {
        return machineHosts.stream().map(el -> Integer.parseInt(el.split("host")[1])).collect(Collectors.toList());
    }
}
package ovs_aas.Submodels.NetworkInfrastructure;

import java.util.Map;
import java.util.function.Function;

import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;

import ovs_aas.StaticProperties;
import ovs_aas.RyuController.Utils.ApiEnum;
import ovs_aas.Submodels.Utils.Utils;
import ovs_aas.Submodels.Utils.RyuControllerManager;

enum Switch {SIMPLE, CLOSED, SELECTIVE};


public class NetworkInfrastructureLambda {

    private RyuControllerManager controller;
    private Utils utils;
    private SSHController shellUtils;
    
    public NetworkInfrastructureLambda(RyuControllerManager controller, Utils utils) {
        this.controller = controller;
        this.utils = utils;
        this.shellUtils = new SSHController();
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

    public Function<Map<String, SubmodelElement>, SubmodelElement[]> getSwitchesNumber() {
        return (args) -> {
            SubmodelElement cnt1 = new Property("Controller1", ValueType.String);
            SubmodelElement cnt2 = new Property("Controller2", ValueType.String);

            String cnt1Status = controller.getResponseWithoutSerial(ApiEnum.getElement(1, ApiEnum.GETALLSWITCHES));
            String cnt2Status = controller.getResponseWithoutSerial(ApiEnum.getElement(2, ApiEnum.GETALLSWITCHES));

            if (cnt1Status == null) {
                cnt1.setValue("Controller 1: disabled");
            } else {
                cnt1.setValue("Controller 1: connected to Switch " + cnt1Status);
            }

            if (cnt2Status == null) {
                cnt2.setValue("Controller 2: disabled");
            } else {
                cnt2.setValue("Controller 2: connected to Switch " + cnt2Status);
            }

            return new SubmodelElement[] {
                cnt1, cnt2
            };
        };
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
            
            if (controllerStatus) {
                controller.enableFirewallController(true);
            }

            StaticProperties.setSelectiveControllers(controllerStatus);

            return new SubmodelElement[] {
                new Property(controllerStatus ? "Activated" : "Deactivated"),
            };

        };
    }
    
}

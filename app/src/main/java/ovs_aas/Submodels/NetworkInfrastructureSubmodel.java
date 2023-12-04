package ovs_aas.Submodels;

import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.operation.Operation;

import java.util.List;
import java.util.Map;
import ovs_aas.Submodels.AbstractSubmodel.AbstractSubmodelInstance;

public class NetworkInfrastructureSubmodel extends AbstractSubmodelInstance {

    @Override
    public List<Submodel> createSubmodel() {
		Submodel switches = new Submodel();
        Submodel controllers = new Submodel();

		switches.setIdShort("Switches");
        switches.setDescription(new LangStrings("Italian", getShellUtils().ovsVersion()));
        switches.addSubmodelElement(switchesOperation());

        controllers.setIdShort("SelectControllers");

        controllers.addSubmodelElement(setOpenController());
        controllers.addSubmodelElement(setClosedController());
        controllers.addSubmodelElement(setSelectiveController());

		return List.of(switches, controllers);
	}

    private Operation switchesOperation() {
        Operation switches = new Operation("SwitchesInfo");
        switches.setOutputVariables(getUtils().getOutputVariables(2));
        switches.setWrappedInvokable(getLambdaProvider().getSwitchesNumber());

        return switches;
    }

    private Operation setOpenController() {
        Operation setController = new Operation("OpenController");
        setController.setDescription(new LangStrings("Italian", "Sw1 and Sw2 are simple switches."));
        setController.setInputVariables(getUtils().getCustomInputVariables(
            Map.of(
                "Controller", ValueType.Boolean
            )));

        setController.setOutputVariables(getUtils().getOutputVariables(1));
        setController.setWrappedInvokable(getLambdaProvider().setSimpleSwitchControllers());

        return setController;
    }

    private Operation setClosedController() {
        Operation setController = new Operation("ClosedController");
        setController.setDescription(new LangStrings("Italian", "Sw2 and Sw2 talks only with their hosts."));
        setController.setInputVariables(getUtils().getCustomInputVariables(
            Map.of(
                "Controller", ValueType.Boolean
            )));

        setController.setOutputVariables(getUtils().getOutputVariables(1));
        setController.setWrappedInvokable(getLambdaProvider().setClosedControllers());

        return setController;
    }

    private Operation setSelectiveController() {
        Operation setController = new Operation("SelectiveController");
        setController.setInputVariables(getUtils().getCustomInputVariables(Map.of(
            "Controller", ValueType.Boolean
            )));
        setController.setOutputVariables(getUtils().getOutputVariables(1));
        setController.setWrappedInvokable(getLambdaProvider().setSelectiveControllers("Sw1Host", "Sw2Host"));

        return setController;
    }
}
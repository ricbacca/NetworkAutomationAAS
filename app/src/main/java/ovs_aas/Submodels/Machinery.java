package ovs_aas.Submodels;

import java.util.List;

import org.eclipse.basyx.submodel.metamodel.api.qualifier.haskind.ModelingKind;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.File;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.AASLambdaPropertyHelper;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.operation.Operation;
import java.util.Map;
import ovs_aas.Submodels.AbstractSubmodel.AbstractSubmodelInstance;

public class Machinery extends AbstractSubmodelInstance {
    String manual;
    String swVersion;
    String shellId;
    List<String> hosts;

    public Machinery(String manual, String version, String idShort, List<String> hosts) {
        super();
        this.manual = manual;
        this.swVersion = version;
        this.shellId = idShort;
        this.hosts = hosts;
    }

    @Override
    public List<Submodel> createSubmodel() {
        Submodel basicInfo = new Submodel();
        basicInfo.setDescription(this.getDescription());
        basicInfo.setIdShort("Info&Manuals");
        basicInfo.addSubmodelElement(setManual());
        basicInfo.addSubmodelElement(setVersion());

        Submodel operations = new Submodel();
        operations.setIdShort("Operations");
        operations.addSubmodelElement(this.pingMachinery());

        return List.of(basicInfo, operations);
    }

    private LangStrings getDescription() {
        return new LangStrings("English", "This machine is composed by: " + hosts);
    }

    private File setManual() {
        File file = new File();
        file.setMimeType("File");
        file.setIdShort("Manuals");
        file.setValue(manual);

        return file;
    }

    private Property setVersion() {
        Property version = new Property("Version", swVersion);
        version.setKind(ModelingKind.INSTANCE);

        AASLambdaPropertyHelper.setLambdaValue(version, () -> swVersion, (newVersion) -> {
            this.swVersion = (String) newVersion;
            version.setValue(newVersion);
        });
        return version;
    }

    private Operation pingMachinery() {
        Operation pingMachinery = new Operation("PingMachinery");
        pingMachinery.setDescription(this.getDescription());
        pingMachinery.setInputVariables(getUtils().getCustomInputVariables(Map.of(
            "To", ValueType.Integer
        )));

        pingMachinery.setOutputVariables(getUtils().getOutputVariables(1));
        pingMachinery.setWrappedInvokable(getLambdaProvider().pingMachinery(hosts));

        return pingMachinery;
    }
}
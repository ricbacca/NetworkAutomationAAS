package ovs_aas.AssetShells.AbstractShell;

import java.util.List;

import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;

import ovs_aas.Infrastructure.ModelProvider;

public interface ShellInstance {

    void createAndStartServlet();

    int getPORT();

    String getREGISTRYPATH();

    AssetAdministrationShell getShell();

    ModelProvider getModelProvider();

    List<Submodel> getSubmodels();

    void createDescriptors();

    void addSubmodels();

    AssetAdministrationShell createShell(String idShort, String pathId, AssetKind kind);

    void createModelProvider();

}
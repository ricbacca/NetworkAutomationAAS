package ovs_aas.Infrastructure;

import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;

import java.util.List;

public interface UploadAAS {
    void registerAASSMDescriptors(Integer PORT, List<Submodel> submopdels, AssetAdministrationShell shell);
}
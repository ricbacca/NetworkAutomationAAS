package ovs_aas.AssetShells;

import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;

import ovs_aas.AssetShells.AbstractShell.AbstractShellInstance;
import ovs_aas.Submodels.NetworkInfrastructureSubmodel;

public class NetworkInfrastructure extends AbstractShellInstance {
    public NetworkInfrastructure(Integer PORT, String idShort, String pathId, AssetKind kind) {
        super(PORT);

        this.shell = this.createShell(idShort, pathId, kind);
        this.createSubmodels();
    }

    private void createSubmodels() {
        this.submodels.addAll(new NetworkInfrastructureSubmodel().createSubmodel());
    }
}
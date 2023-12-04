package ovs_aas.AssetShells;

import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;

import ovs_aas.AssetShells.AbstractShell.AbstractShellInstance;
import ovs_aas.Submodels.Controller1;
import ovs_aas.Submodels.Controller2;

public class NetworkControlPlane extends AbstractShellInstance {

    public NetworkControlPlane(Integer PORT, String idShort, String pathId, AssetKind kind) {
        super(PORT);

        this.shell = this.createShell(idShort, pathId, kind);
        this.createSubmodels();
    }

    private void createSubmodels() {
        this.submodels.addAll(new Controller1().createSubmodel());
        this.submodels.addAll(new Controller2().createSubmodel());
    }
}
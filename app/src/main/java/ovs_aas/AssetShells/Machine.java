package ovs_aas.AssetShells;

import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;

import ovs_aas.AssetShells.AbstractShell.AbstractShellInstance;
import ovs_aas.Submodels.Machinery;
import java.util.List;

public class Machine extends AbstractShellInstance {
    String manual;
    String version;
    List<String> hosts;

    public Machine(Integer PORT, String idShort, String pathId, AssetKind kind, String manual, String version, List<String> hosts) {
        super(PORT);

        this.manual = manual;
        this.version = version;
        this.hosts = hosts;
        this.shell = this.createShell(idShort, pathId, kind);
        this.createSubmodels();
    }

    private void createSubmodels() {
        this.submodels.addAll(new Machinery(this.manual, this.version, this.getShell().getIdShort(), hosts).createSubmodel());
    }
    
}

package ovs_aas.AssetShells.AbstractShell;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.parts.Asset;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.metamodel.map.identifier.Identifier;
import ovs_aas.Infrastructure.ModelProvider;
import ovs_aas.Infrastructure.UploadAAS;
import ovs_aas.Infrastructure.UploadAASImpl;

public abstract class AbstractShellInstance implements ShellInstance {
    private final String REGISTRYPATH = "http://localhost:8082/registry/";
    
    private final String ACCESS_CONTROL_ALLOW_ORIGIN = "*";
    private final String CONTEXT_PATH = "/";

    protected AssetAdministrationShell shell;
    protected List<Submodel> submodels;
    protected ModelProvider modelProvider;
    protected UploadAAS descriptorsManager;
    protected final int PORT;

    public AbstractShellInstance(int PORT) {
        this.PORT = PORT;
        this.submodels = new LinkedList<>();
    }

    @Override
    public void createAndStartServlet() {
        this.addSubmodels();
        this.createModelProvider();
        this.createDescriptors();
    }

    @Override
    public int getPORT() {
        return PORT;
    }

    @Override
    public String getREGISTRYPATH() {
        return REGISTRYPATH;
    }

    @Override
    public AssetAdministrationShell getShell() {
        return shell;
    }

    @Override
    public ModelProvider getModelProvider() {
        return modelProvider;
    }

    @Override
    public List<Submodel> getSubmodels() {
        return submodels;
    }

    @Override
    public void createDescriptors() {
        this.descriptorsManager = new UploadAASImpl(this.getREGISTRYPATH());
        this.descriptorsManager.registerAASSMDescriptors(this.PORT, this.submodels, this.shell);
    }

    @Override
    public void addSubmodels() {
        this.submodels.forEach(el -> this.shell.addSubmodel(el));
    }

    @Override
    public AssetAdministrationShell createShell(String idShort, String pathId, AssetKind kind) {
        IIdentifier id = new Identifier(IdentifierType.IRI, pathId);
        Asset ovsAsset = new Asset(idShort, id, kind);
        
        return new AssetAdministrationShell(idShort, id, ovsAsset);
    }

    @Override
    public void createModelProvider() {
        this.modelProvider = new ModelProvider(
            this.PORT, 
            this.ACCESS_CONTROL_ALLOW_ORIGIN, 
            this.CONTEXT_PATH,
            this.shell, 
            this.submodels);
        this.modelProvider.startLocalHTTPServer(shell.getIdShort());
    }
}

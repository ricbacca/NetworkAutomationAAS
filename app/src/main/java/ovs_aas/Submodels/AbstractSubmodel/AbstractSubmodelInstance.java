package ovs_aas.Submodels.AbstractSubmodel;

import ovs_aas.Shell.ShellUtils;
import ovs_aas.Submodels.RyuApi.RestRequestRyu;
import ovs_aas.Submodels.Utils.LambdaProvider;
import ovs_aas.Submodels.Utils.Utils;

public abstract class AbstractSubmodelInstance implements SubmodelInstance {
    private Utils utils;
    private LambdaProvider lambdaProvider;
    private ShellUtils shellUtils;
    private RestRequestRyu controller;

    public AbstractSubmodelInstance() {
        this.utils = new Utils();
        this.controller = new RestRequestRyu();
        this.shellUtils = new ShellUtils();
        this.lambdaProvider = new LambdaProvider(utils, shellUtils, controller);
    }

    public Utils getUtils() {
        return utils;
    }

    public LambdaProvider getLambdaProvider() {
        return lambdaProvider;
    }

    public ShellUtils getShellUtils() {
        return shellUtils;
    }

    public RestRequestRyu getController() {
        return controller;
    }

    
}

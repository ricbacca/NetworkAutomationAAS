// Copyright 2023 riccardo.bacca@studio.unibo.it
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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

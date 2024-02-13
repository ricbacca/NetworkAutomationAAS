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

package ovs_aas.Submodels;

import ovs_aas.RyuController.Controller;
import ovs_aas.Submodels.Utils.Utils;

/**
 * Abstract class used to remove some repeated code along all Submodel Implementations.
 */
public abstract class AbstractSubmodel implements ISubmodel {
    private Utils utils;
    private Controller ryuController;

    public AbstractSubmodel() {
        this.utils = new Utils();
        this.ryuController = new Controller();
    }

    public Utils getUtils() {
        return utils;
    }

    public Controller getRyuController() {
        return ryuController;
    }

}

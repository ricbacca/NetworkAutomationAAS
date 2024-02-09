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

package ovs_aas.AssetShells.AbstractShell;

import java.util.List;

import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;

import ovs_aas.Infrastructure.ModelProvider;

public interface IShell {

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
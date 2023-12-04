// Copyright 2023 riccardo
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

package ovs_aas.Submodels.Utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.basyx.submodel.metamodel.api.qualifier.haskind.ModelingKind;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.operation.OperationVariable;

public class Utils {
    public static final int AGGREGATE_FLOWS_OUTPUT = 3;
    public static final int ALL_FLOWS_OUTPUT = 9;
    public static final int GET_ROLE_OUTPUT = 2;
    public static final int SET_ROLE_OUTPUT = 1;
    public static final int GET_FIREWALL_RULES = 6;


    public List<OperationVariable> getCustomInputVariables(Map<String, ValueType> values) {
        List<OperationVariable> rList = new LinkedList<>();

        values.forEach((k, v) -> {
            Property prop = new Property(k, v);
            prop.setKind(ModelingKind.TEMPLATE);
            rList.add(new OperationVariable(prop));
        });
        return rList;
    }

    public List<OperationVariable> getSimpleInputVariables(int n) {
        return Collections.nCopies(n, new Property("Input", ValueType.String)).
                stream().
                peek(el -> el.setKind(ModelingKind.TEMPLATE)).
                map(el -> new OperationVariable(el)).
                collect(Collectors.toList());
    }

    public List<OperationVariable> getOutputVariables(int n) {
        return Collections.nCopies(n, new Property("Output", ValueType.String)).
            stream().
            peek(el -> el.setKind(ModelingKind.TEMPLATE)).
            map(el -> new OperationVariable(el)).
            collect(Collectors.toList());
    }

    public List<OperationVariable> getInOutVariables(int n) {
        return Collections.nCopies(n, new Property("InOut", ValueType.String)).
            stream().
            peek(el -> el.setKind(ModelingKind.TEMPLATE)).
            map(el -> new OperationVariable(el)).
            collect(Collectors.toList());
    }

    public Boolean getOrElse(Object val) {
        return val == null ? false : (boolean) val;
    }
}

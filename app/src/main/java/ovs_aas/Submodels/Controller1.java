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

package ovs_aas.Submodels;

import java.util.List;
import java.util.Map;

import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.operation.Operation;
import ovs_aas.Submodels.AbstractSubmodel.AbstractSubmodelInstance;
import ovs_aas.Submodels.RyuApi.RyuApiEnum;
import ovs_aas.Submodels.Utils.Utils;

public class Controller1 extends AbstractSubmodelInstance {
    public Controller1() {
        super();
    }

    @Override
    public List<Submodel> createSubmodel() {
        Submodel cnt1Submodel = new Submodel();

		cnt1Submodel.setIdShort("Controller1");
        cnt1Submodel.setDescription(new LangStrings("Italian", getShellUtils().ovsVersion()));

        cnt1Submodel.addSubmodelElement(aggregateFlowStatsCNT1());
        cnt1Submodel.addSubmodelElement(allFlowStatsCNT1());
        cnt1Submodel.addSubmodelElement(getRoleCNT1());
        cnt1Submodel.addSubmodelElement(setRoleCNT1());
        cnt1Submodel.addSubmodelElement(setFirewallRules());
        cnt1Submodel.addSubmodelElement(getFirewallRules());
        cnt1Submodel.addSubmodelElement(deleteFirewallRule());

		return List.of(cnt1Submodel);
    }

    private Operation aggregateFlowStatsCNT1() {
        Operation aggregateFlows = new Operation("AggregateFlows");
        aggregateFlows.setOutputVariables(getUtils().getOutputVariables(Utils.AGGREGATE_FLOWS_OUTPUT));
        aggregateFlows.setWrappedInvokable(getLambdaProvider().getAggregateStats(RyuApiEnum.CONTROLLER1_GETAGGREGATEFLOWSTATS));

        return aggregateFlows;
    }


    private Operation allFlowStatsCNT1() {
        Operation allFlowStats = new Operation("AllFlowStats");

        allFlowStats.setOutputVariables(getUtils().getOutputVariables(Utils.ALL_FLOWS_OUTPUT));
        allFlowStats.setWrappedInvokable(getLambdaProvider().getAllFlowStats(RyuApiEnum.CONTROLLER1_GETALLFLOWSTATS));

        return allFlowStats;
    }

    private Operation getRoleCNT1() {
        Operation getRole = new Operation("GetRole");

        getRole.setOutputVariables(getUtils().getOutputVariables(Utils.GET_ROLE_OUTPUT));
        getRole.setWrappedInvokable(getLambdaProvider().getRole(RyuApiEnum.CONTROLLER1_GETROLE));

        return getRole;
    }

    private Operation setRoleCNT1() {
        Operation setRole = new Operation("SetRole");
        setRole.setInputVariables(getUtils().getCustomInputVariables(
            Map.of("Role", ValueType.String)
        ));
        setRole.setOutputVariables(getUtils().getOutputVariables(Utils.SET_ROLE_OUTPUT));
        setRole.setWrappedInvokable(getLambdaProvider().setRole(RyuApiEnum.CONTROLLER1_SETROLE));

        return setRole;
    }

    private Operation setFirewallRules() {
        Operation setRule = new Operation("SetFirewallRules");
        setRule.setDescription(new LangStrings("English", "Source and Destination between 1 and 6. Add rules for both ways."));
        setRule.setInputVariables(getUtils().getCustomInputVariables(Map.of(
            "Source", ValueType.Integer,
            "Destination", ValueType.Integer
        )));
        setRule.setOutputVariables(getUtils().getOutputVariables(1));
        setRule.setWrappedInvokable(getLambdaProvider().setFirewallRule(RyuApiEnum.CONTROLLER1_FIREWALL_RULES));

        return setRule;
    }

    private Operation getFirewallRules() {
        Operation getRules = new Operation("GetFirewallRules");
        getRules.setOutputVariables(getUtils().getOutputVariables(Utils.GET_FIREWALL_RULES));

        getRules.setWrappedInvokable(getLambdaProvider().getFirewallRules(RyuApiEnum.CONTROLLER1_FIREWALL_RULES));

        return getRules;
    }

    private Operation deleteFirewallRule() {
        Operation deleteRule = new Operation("DeleteFirewallRules");
        deleteRule.setInputVariables(getUtils().getCustomInputVariables(Map.of("RuleId", ValueType.Integer)));
        deleteRule.setOutputVariables(getUtils().getOutputVariables(1));

        deleteRule.setWrappedInvokable(getLambdaProvider().deleteFirewallRules(RyuApiEnum.CONTROLLER1_FIREWALL_RULES));

        return deleteRule;
    }
}
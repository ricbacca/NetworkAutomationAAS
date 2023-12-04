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

package ovs_aas.Submodels.RyuApi;

public enum RyuApiEnum {
    CONTROLLER1("http://localhost:8080"),
    CONTROLLER2("http://localhost:9090"),

    CONTROLLER1_GETALLSWITCHES(CONTROLLER1.url + "/stats/switches"),
    CONTROLLER2_GETALLSWITCHES(CONTROLLER2.url + "/stats/switches"),

    CONTROLLER1_GETAGGREGATEFLOWSTATS(CONTROLLER1.url + "/stats/aggregateflow/1"),
    CONTROLLER2_GETAGGREGATEFLOWSTATS(CONTROLLER2.url + "/stats/aggregateflow/2"),

    CONTROLLER1_GETALLFLOWSTATS(CONTROLLER1.url + "/stats/flow/1"),
    CONTROLLER2_GETALLFLOWSTATS(CONTROLLER2.url + "/stats/flow/2"),

    CONTROLLER1_GETROLE(CONTROLLER1.url + "/stats/role/1"),
    CONTROLLER2_GETROLE(CONTROLLER2.url + "/stats/role/2"),

    CONTROLLER1_SETROLE(CONTROLLER1.url + "/stats/role"),
    CONTROLLER2_SETROLE(CONTROLLER2.url + "/stats/role"),

    CONTROLLER1_FIREWALL_ENABLE(CONTROLLER1.url + "/firewall/module/enable/0000000000000001"),
    CONTROLLER2_FIREWALL_ENABLE(CONTROLLER2.url + "/firewall/module/enable/0000000000000002"),

    CONTROLLER1_FIREWALL_RULES(CONTROLLER1.url + "/firewall/rules/0000000000000001"),
    CONTROLLER2_FIREWALL_RULES(CONTROLLER2.url + "/firewall/rules/0000000000000002");

    public final String url;

    private RyuApiEnum(String label) {
        this.url = label;
    }
}
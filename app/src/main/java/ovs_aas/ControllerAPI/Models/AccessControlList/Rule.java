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

package ovs_aas.ControllerAPI.Models.AccessControlList;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {
    List<Rules> rules = new ArrayList<>();
    public List<Rules> getRules() {
        return rules;
    }
    public void setRules(List<Rules> rules) {
        this.rules = rules;
    }
    @Override
    public String toString() {
        return "Rule [rules=" + rules + "]";
    }
}

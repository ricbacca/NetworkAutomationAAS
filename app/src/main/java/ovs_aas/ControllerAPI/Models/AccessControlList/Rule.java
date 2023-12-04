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

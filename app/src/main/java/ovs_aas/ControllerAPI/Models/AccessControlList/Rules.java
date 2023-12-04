package ovs_aas.ControllerAPI.Models.AccessControlList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rules {
        int rule_id;
        int priority;
        String dl_type;
        String nw_src;
        String nw_dst;
        String actions;
        public int getRule_id() {
            return rule_id;
        }
        public void setRule_id(int rule_id) {
            this.rule_id = rule_id;
        }
        public int getPriority() {
            return priority;
        }
        public void setPriority(int priority) {
            this.priority = priority;
        }
        public String getDl_type() {
            return dl_type;
        }
        public void setDl_type(String dl_type) {
            this.dl_type = dl_type;
        }
        public String getNw_src() {
            return nw_src;
        }
        public void setNw_src(String nw_src) {
            this.nw_src = nw_src;
        }
        public String getNw_dst() {
            return nw_dst;
        }
        public void setNw_dst(String nw_dst) {
            this.nw_dst = nw_dst;
        }
        public String getActions() {
            return actions;
        }
        public void setActions(String actions) {
            this.actions = actions;
        }
        @Override
        public String toString() {
            return "Rules [rule_id=" + rule_id + ", priority=" + priority + ", dl_type=" + dl_type + ", nw_src="
                    + nw_src + ", nw_dst=" + nw_dst + ", actions=" + actions + "]";
        }
    }

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
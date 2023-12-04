package ovs_aas;

import org.eclipse.basyx.aas.metamodel.api.parts.asset.AssetKind;

import java.util.List;
import ovs_aas.AssetShells.Machine;
import ovs_aas.AssetShells.NetworkControlPlane;
import ovs_aas.AssetShells.NetworkInfrastructure;
import ovs_aas.AssetShells.AbstractShell.ShellInstance;
import ovs_aas.ControllerAPI.Controller.ControllerClient;

public class App {
    private final static String manual = "https://www.plattform-i40.de/IP/Redaktion/EN/Downloads/Publikation/Details_of_the_Asset_Administration_Shell_Part1_V3.pdf?__blob=publicationFile&v=1";
    private final static String version = "1.0.0";
    private final static ControllerClient client = new ControllerClient();
    public static void main(String[] args) {
        waitForServer();

        ShellInstance networkInfrastructure = new NetworkInfrastructure(
            6001, 
            "Network Infrastructure", 
            "org.unibo.aas.networkInfrastructure", 
            AssetKind.INSTANCE);

        ShellInstance networkControlPlane = new NetworkControlPlane(
            6002, 
            "Network Control Plane", 
            "org.unibo.aas.networkControlPlane", 
            AssetKind.INSTANCE);

        ShellInstance machineOne = new Machine(
            6003,
            "Machine1",
            "org.unibo.aas.machineOne",
            AssetKind.INSTANCE,
            manual,
            version,
            List.of("Host1", "Host2"));

        ShellInstance machineTwo = new Machine(
            6004,
            "Machine2",
            "org.unibo.aas.machineTwo",
            AssetKind.INSTANCE,
            manual,
            version,
            List.of("Host3", "Host4"));

        ShellInstance machineThree = new Machine(
            6005,
            "Machine3",
            "org.unibo.aas.machineThree",
            AssetKind.INSTANCE,
            manual,
            version,
            List.of("Host5", "Host6"));

        networkInfrastructure.createAndStartServlet();
        networkControlPlane.createAndStartServlet();
        machineOne.createAndStartServlet();
        machineTwo.createAndStartServlet();
        machineThree.createAndStartServlet();
    }

    private static void waitForServer() {
        System.out.print("Waiting for Registry at localhost:8082");
        while(!client.isServerAvailable()) {
            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
    }
}
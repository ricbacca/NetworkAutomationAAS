package ovs_aas.ControllerAPI.Models;

import org.eclipse.basyx.submodel.metamodel.api.qualifier.haskind.ModelingKind;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregateFlowStats {
    int flow_count;
    int packet_count;
    int byte_count;

    public int getFlow_count() {
        return flow_count;
    }
    public void setFlow_count(int flow_count) {
        this.flow_count = flow_count;
    }
    public int getPacket_count() {
        return packet_count;
    }
    public void setPacket_count(int packet_count) {
        this.packet_count = packet_count;
    }
    public int getByte_count() {
        return byte_count;
    }
    public void setByte_count(int byte_count) {
        this.byte_count = byte_count;
    }

    public SubmodelElement[] formatResult() {
        SubmodelElement flowCount = new Property("FlowCount", ValueType.Integer);
        SubmodelElement packetCount = new Property("PacketCount", ValueType.Integer);
        SubmodelElement byteCount = new Property("ByteCount", ValueType.Integer);

        flowCount.setKind(ModelingKind.TEMPLATE);
        packetCount.setKind(ModelingKind.TEMPLATE);
        byteCount.setKind(ModelingKind.TEMPLATE);

        flowCount.setValue("Flows: " + this.flow_count);
        packetCount.setValue("Packets: " + this.packet_count);
        byteCount.setValue("Bytes: " + this.byte_count);

        return new SubmodelElement[] {
            flowCount, packetCount, byteCount
        };
    }
}
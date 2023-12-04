package ovs_aas.ControllerAPI.Models;

import org.eclipse.basyx.submodel.metamodel.api.qualifier.haskind.ModelingKind;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.SubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.valuetype.ValueType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    String generation_id;
    String role;
    
    public String getGeneration_id() {
        return generation_id;
    }
    public void setGeneration_id(String generation_id) {
        this.generation_id = generation_id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public SubmodelElement[] formatResult() {
        SubmodelElement role = new Property("Role", ValueType.String);
        SubmodelElement generationId = new Property("GenerationId", ValueType.Integer);

        role.setKind(ModelingKind.TEMPLATE);
        generationId.setKind(ModelingKind.TEMPLATE);

        role.setValue("Role: " + this.role);
        generationId.setValue("GenerationId: " + this.generation_id);

        return new SubmodelElement[] {
            role, generationId
        };
    }
}

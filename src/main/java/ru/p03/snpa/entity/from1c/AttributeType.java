package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.ClsAttributeType;

public class AttributeType {
    @SerializedName("cls_attribute_type")
    private ClsAttributeType[] clsAttributeTypes;

    public ClsAttributeType[] getClsAttributeTypes() {
        return clsAttributeTypes;
    }

    public void setClsAttributeTypes(ClsAttributeType[] clsAttributeTypes) {
        this.clsAttributeTypes = clsAttributeTypes;
    }
}

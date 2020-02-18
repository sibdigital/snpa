package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.ClsAttributeValue;

public class AttributeValue {
    @SerializedName("cls_attribute_value")
    private ClsAttributeValue[] clsAttributeValues;

    public ClsAttributeValue[] getClsAttributeValues() {
        return clsAttributeValues;
    }

    public void setClsAttributeValues(ClsAttributeValue[] clsAttributeValues) {
        this.clsAttributeValues = clsAttributeValues;
    }
}

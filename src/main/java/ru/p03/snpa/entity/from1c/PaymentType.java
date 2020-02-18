package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.ClsPaymentType;

public class PaymentType {

    @SerializedName("cls_payment_type")
    ClsPaymentType[] clsPaymentTypes;

    public ClsPaymentType[] getClsPaymentTypes() {
        return clsPaymentTypes;
    }

    public void setClsPaymentTypes(ClsPaymentType[] clsPaymentTypes) {
        this.clsPaymentTypes = clsPaymentTypes;
    }
}

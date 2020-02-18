package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.ClsLifeSituation;

public class LifeSituation {

    @SerializedName("cls_life_situation")
    ClsLifeSituation[] clsLifeSituations;

    public ClsLifeSituation[] getClsLifeSituations() {
        return clsLifeSituations;
    }

    public void setClsLifeSituations(ClsLifeSituation[] clsLifeSituations) {
        this.clsLifeSituations = clsLifeSituations;
    }
}

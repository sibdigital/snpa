package ru.p03.snpa.entity.from1c;

import com.google.gson.annotations.SerializedName;
import ru.p03.snpa.entity.ClsAction;

public class Action {

    @SerializedName("cls_action")
    private ClsAction[] clsActions;

    public ClsAction[] getClsActions() {
        return clsActions;
    }

    public void setClsActions(ClsAction[] clsActions) {
        this.clsActions = clsActions;
    }
}

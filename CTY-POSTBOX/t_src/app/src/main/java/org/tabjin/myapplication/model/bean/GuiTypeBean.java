package org.tabjin.myapplication.model.bean;

import java.io.Serializable;

public class GuiTypeBean implements Serializable {

    private int guiType;
    private int guicellCount;

    public int getGuiType() {
        return guiType;
    }

    public void setGuiType(int guiType) {
        this.guiType = guiType;
    }

    public int getGuicellCount() {
        return guicellCount;
    }

    public void setGuicellCount(int guicellCount) {
        this.guicellCount = guicellCount;
    }

}

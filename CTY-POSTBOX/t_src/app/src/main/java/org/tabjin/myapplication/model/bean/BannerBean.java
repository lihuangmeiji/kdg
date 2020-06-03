package org.tabjin.myapplication.model.bean;

import com.google.gson.annotations.SerializedName;

public class BannerBean {


    /**
     * gui_no : 310016010
     * path :
     * update_at : 2019-02-14T08:14:01.000+0000
     * id : 178
     * type : 8
     * create_at : 2019-02-14T08:14:01.000+0000
     * configure_id : 9
     * content : 轮播广告视频1
     */

    @SerializedName("gui_no")
    private String guiNo;
    @SerializedName("path")
    private String path;
    @SerializedName("update_at")
    private String updateAt;
    @SerializedName("id")
    private int id;
    @SerializedName("type")
    private int type;
    @SerializedName("create_at")
    private String createAt;
    @SerializedName("configure_id")
    private int configureId;
    @SerializedName("content")
    private String content;

    public String getGuiNo() {
        return guiNo;
    }

    public void setGuiNo(String guiNo) {
        this.guiNo = guiNo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getConfigureId() {
        return configureId;
    }

    public void setConfigureId(int configureId) {
        this.configureId = configureId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

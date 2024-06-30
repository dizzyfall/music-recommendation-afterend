package com.dzy.model.enums.songlisttags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:37
 */
public enum SceneEnum {

    ALL(-100, "全部"),
    POP(1, "流行"),
    CPOP(2, "国风");

    private final Integer sceneId;
    private final String sceneTagName;

    SceneEnum(Integer sceneId, String sceneTagName) {
        this.sceneId = sceneId;
        this.sceneTagName = sceneTagName;
    }

    public static String getSceneTagNameById(Integer sceneId) {
        if (sceneId == null) {
            return null;
        }
        for (SceneEnum scene : SceneEnum.values()) {
            if (sceneId.equals(scene.getSceneId())) {
                return scene.getSceneTagName();
            }
        }
        return null;
    }

    public Integer getSceneId() {
        return sceneId;
    }

    public String getSceneTagName() {
        return sceneTagName;
    }

}

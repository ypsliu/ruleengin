package cn.rongcapital.ruleengine.model;

/**
 * Created by lilupeng on 16/11/5.
 *
 */
public enum MatchRequestMethod {
    REQUEST("request"),
    BIZTYPECODE_BIZCODE("bizTypeCode_bizCode"),
    BIZTYPECODE_BIZCODE_RULESETCODE_RULESETVERSION("bizTypeCode_bizCode_ruleSetCode_ruleSetVersion");

    private String method;

    /**
     *
     * @param method
     */
    private MatchRequestMethod(String method) {
        this.method = method;
    }

    /**
     *
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     *
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }
}

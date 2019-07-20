package cn.rongcapital.ruleengine.model;

import java.util.Map;

/**
 * Created by lilupeng on 16/11/9.
 *
 */
public class RuleSetEntity {

    private RuleSet ruleSet;
    private Map<String, String> params;
    private AcceptanceResults acceptanceResult;
    private ScoreResults scoreResult;
    private TextResults textResult;

    /**
     * keep this for jackson json to serialize/deserialize
     *
     */
    public RuleSetEntity() {
    }

    /**
     *
     * @param ruleSet
     * @param params
     * @param acceptanceResult
     * @param scoreResult
     * @param textResult
     *
     */
    public RuleSetEntity(RuleSet ruleSet, Map<String, String> params, AcceptanceResults acceptanceResult, ScoreResults scoreResult, TextResults textResult) {
        this.ruleSet = ruleSet;
        this.params = params;
        this.acceptanceResult = acceptanceResult;
        this.scoreResult = scoreResult;
        this.textResult = textResult;
    }

    @Override
    public String toString() {
        return "RuleEntity{" +
                "ruleSet=" + ruleSet +
                ", params=" + params +
                ", acceptanceResult=" + acceptanceResult +
                ", scoreResult=" + scoreResult +
                ", textResult=" + textResult +
                '}';
    }

    public RuleSet getRuleSet() {
        return ruleSet;
    }

    public void setRuleSet(RuleSet ruleSet) {
        this.ruleSet = ruleSet;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public AcceptanceResults getAcceptanceResult() {
        return acceptanceResult;
    }

    public void setAcceptanceResult(AcceptanceResults acceptanceResult) {
        this.acceptanceResult = acceptanceResult;
    }

    public ScoreResults getScoreResult() {
        return scoreResult;
    }

    public void setScoreResult(ScoreResults scoreResult) {
        this.scoreResult = scoreResult;
    }

    public TextResults getTextResult() {
        return textResult;
    }

    public void setTextResult(TextResults textResult) {
        this.textResult = textResult;
    }

}

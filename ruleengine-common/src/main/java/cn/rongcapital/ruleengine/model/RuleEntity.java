package cn.rongcapital.ruleengine.model;

import java.util.Map;

/**
 * Created by Kruce on 16/11/1.
 *
 */
public class RuleEntity {

    private Rule rule;
    private Map<String, String> params;
    private AcceptanceResults acceptanceResult;
    private ScoreResults scoreResult;
    private TextResults textResult;
    private int sortedId;

    /**
     * keep this for jackson json to serialize/deserialize
     *
     */
    public RuleEntity() {
    }

    /**
     *
     * @param rule
     * @param params
     * @param acceptanceResult
     * @param scoreResult
     * @param textResult
     * @param sortedId
     */
    public RuleEntity(Rule rule, Map<String, String> params, AcceptanceResults acceptanceResult, ScoreResults scoreResult, TextResults textResult, int sortedId) {
        this.rule = rule;
        this.params = params;
        this.acceptanceResult = acceptanceResult;
        this.scoreResult = scoreResult;
        this.textResult = textResult;
        this.sortedId = sortedId;
    }

    @Override
    public String toString() {
        return "RuleEntity{" +
                "rule=" + rule +
                ", params=" + params +
                ", acceptanceResult=" + acceptanceResult +
                ", scoreResult=" + scoreResult +
                ", textResult=" + textResult +
                ", sortedId=" + sortedId +
                '}';
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
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

    public int getSortedId() {
        return sortedId;
    }

    public void setSortedId(int sortedId) {
        this.sortedId = sortedId;
    }
}

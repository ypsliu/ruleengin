
function rebindRemoveParamBtnClick() {
	$("#ruleStepsBox #ruleDatas .removeParamBtn").off("click");
	$("#ruleStepsBox #ruleDatas .removeParamBtn").click(function() {
		$(this).parent().parent().remove();
	});
}

function rebindRemoveSegmentBtnClick() {
	$("#ruleStepsBox #ruleDatas .removeSegmentBtn").off("click");
	$("#ruleStepsBox #ruleDatas .removeSegmentBtn").click(function() {
		$(this).parent().parent().remove();
	});
}

function rebindRemoveVarBtnClick() {
	$("#ruleStepsBox #ruleDatas .removeVarBtn").off("click");
	$("#ruleStepsBox #ruleDatas .removeVarBtn").click(function() {
		$(this).parent().parent().remove();
	});
}

function initSteps() {
	for (var index = 1; index < nextStepIndex; index++) {
		// each step
		if ($("#ruleStepsBox #ruleStep-" + index).size() == 0) {
			// removed
			continue;
		}
		// add condition button
		$("#ruleStepsBox #ruleStep-" + index + " #addConditionBtn").off("click");
		$("#ruleStepsBox #ruleStep-" + index + " #addConditionBtn").click(function() {
			var i = $(this).data("index");
			addStepCondition(i);
			// rebind
			rebindRemoveConditionBtnClick(i);
		});
		// remove step button
		$("#ruleStepsBox #ruleStep-" + index + " #removeStepBtn").off("click");
		$("#ruleStepsBox #ruleStep-" + index + " #removeStepBtn").click(function() {
			if (stepCount > 1) {
				var i = $(this).data("index");
				if (confirm("确定要删除这个步骤吗？")) {
					$("#ruleStepsBox #ruleStep-" + i).remove();
					stepCount--;
					initSteps();
				}
			}
		});
		// disable remove button when the count less then 2
		if (stepCount <= 1) {
			$("#ruleStepsBox #ruleStep-" + index + " #removeStepBtn").addClass("am-disabled");
		} else {
			$("#ruleStepsBox #ruleStep-" + index + " #removeStepBtn").removeClass("am-disabled");
		}
		// stepComment
		$("#ruleStepsBox #ruleStep-" + index + " #comment").off("blur");
		$("#ruleStepsBox #ruleStep-" + index + " #comment").blur(function() {
			var value = $(this).val();
			var i = $(this).data("index");
			if (value) {
				$("#ruleStepsBox #ruleStep-" + i + " #stepComment").text(value);
			} else {
				$("#ruleStepsBox #ruleStep-" + i + " #stepComment").text("步骤" + i);
			}
		});
		// move up button
		$("#ruleStepsBox #ruleStep-" + index + " #moveUpBtn").off("click");
		$("#ruleStepsBox #ruleStep-" + index + " #moveUpBtn").click(function() {
			moveStepUp($(this).data("index"));
		});
		// move down button
		$("#ruleStepsBox #ruleStep-" + index + " #moveDownBtn").off("click");
		$("#ruleStepsBox #ruleStep-" + index + " #moveDownBtn").click(function() {
			moveStepDown($(this).data("index"));
		});
	}
}

function moveStepUp(index) {
	var preStep = null;
	var thisStep = null;
	$("#ruleStepsBox .ruleStep").each(function(i, s) {
		var current = $(s);
		if (current.data("index") == index) {
			thisStep = current;
			return false;
		} else {
			preStep = current;
		}
	});
	if (preStep && thisStep) {
		// can move up, move it
		thisStep.after(preStep.clone());
		preStep.remove();
		// initialize the accordion
		$.AMUI.accordion.init();
		// reinitialize the steps
		initSteps();
	}
}

function moveStepDown(index) {
	var nextStep = null;
	var thisStep = null;
	$("#ruleStepsBox .ruleStep").each(function(i, s) {
		var current = $(s);
		if (current.data("index") == index) {
			thisStep = current;
			return true; // continue
		}
		if (thisStep) {
			nextStep = current;
			return false;
		}
	});
	if (nextStep && thisStep) {
		// can move down, move it
		nextStep.after(thisStep.clone());
		thisStep.remove();
		// initialize the accordion
		$.AMUI.accordion.init();
		// reinitialize the steps
		initSteps();
	}
}

function rebindRemoveConditionBtnClick(index) {
	$("#ruleStepsBox #ruleStep-" + index + " .removeConditionBtn").off("click");
	$("#ruleStepsBox #ruleStep-" + index + " .removeConditionBtn").click(function() {
		$(this).parent().parent().remove();
	});
}

function getRuleVars() {
	var vars = [];
	// params
	$("#ruleStepsBox #ruleDatas input[name='paramName']").each(function(index, input) {
		var v = $(this).val();
		if (v && $.inArray(v, vars) == -1) {
			vars.push(v);
		}
	});
	// vars
	$("#ruleStepsBox #ruleDatas input[name='varName']").each(function(index, input) {
		var v = $(this).val();
		if (v && $.inArray(v, vars) == -1) {
			vars.push(v);
		}
	});
	return vars;
}

function getRuleDesign() {
	var data = {
		params: [],
		segments: [],
		vars: [],
		steps: []
	};
	// params
	$("#ruleStepsBox #ruleDatas input[name='paramName']").each(function(index, input) {
		var v = $(this).val();
		if (v && $.inArray(v, data.params) == -1) {
			data.params.push(v);
		}
	});
	// segments
	var segmentKeys = [];
	var segmentValues = [];
	$("#ruleStepsBox #ruleDatas input[name='segmentKey']").each(function(index, input) {
		segmentKeys.push($(this).val());
	});
	$("#ruleStepsBox #ruleDatas input[name='segmentValue']").each(function(index, input) {
		segmentValues.push($(this).val());
	});
	for (var i = 0; i < segmentKeys.length; i++) {
		data.segments.push({
			key: segmentKeys[i],
			value: segmentValues[i]
		});
	}
	// vars
	var varNames = [];
	var varTypes = [];
	var varParams = [];
	$("#ruleStepsBox #ruleDatas input[name='varName']").each(function(index, input) {
		varNames.push($(this).val());
	});
	$("#ruleStepsBox #ruleDatas select[name='varType']").each(function(index, input) {
		varTypes.push($(this).val());
	});
	$("#ruleStepsBox #ruleDatas input[name='varParams']").each(function(index, input) {
		varParams.push($(this).val());
	});
	for (var i = 0; i < varNames.length; i++) {
		data.vars.push({
			name: varNames[i],
			type: varTypes[i],
			params: varParams[i]
		});
	}
	// steps
	$("#ruleStepsBox .ruleStep").each(function(i, s) {
		var step = {
			conditions: [],
			trueResult: {},
			falseResult: {}
		};
		// each step
		var index = $(this).data("index");
		// comment
		step.comment = $("#ruleStepsBox #ruleStep-" + index + " #comment").val();
		// relation type
		step.relType = $("#ruleStepsBox #ruleStep-" + index + " #conditionsRelationType").val();
		// condition.varName
		var conditionVarNames = [];
		$("#ruleStepsBox #ruleStep-" + index + " select[name='varName']").each(function() {
			conditionVarNames.push($(this).val());
		});
		var conditionVarOperators = [];
		$("#ruleStepsBox #ruleStep-" + index + " select[name='varOperator']").each(function() {
			conditionVarOperators.push($(this).val());
		});
		var conditionVarValues = [];
		$("#ruleStepsBox #ruleStep-" + index + " input[name='varValue']").each(function() {
			conditionVarValues.push($(this).val());
		});
		for (var i = 0; i < conditionVarNames.length; i++) {
			step.conditions.push({
				varName: conditionVarNames[i],
				operator: conditionVarOperators[i],
				value: conditionVarValues[i]
			});
		}
		var matchType = $("#rule-editor-form #match_type").val();
		// trueResult
		step.trueResult.trace = $("#ruleStepsBox #ruleStep-" + index + " #trueTrace").val();
		if ("ACCEPTANCE" == matchType) {
			step.trueResult.result = $("#ruleStepsBox #ruleStep-" + index + " input[name='trueResult-" + index + "']:checked").val();
		} else {
			step.trueResult.result = $("#ruleStepsBox #ruleStep-" + index + " input[name='trueResult-" + index + "']").val();
		}
		// falseResult
		step.falseResult.trace = $("#ruleStepsBox #ruleStep-" + index + " #falseTrace").val();
		if ("ACCEPTANCE" == matchType) {
			step.falseResult.result = $("#ruleStepsBox #ruleStep-" + index + " input[name='falseResult-" + index + "']:checked").val();
		} else {
			step.falseResult.result = $("#ruleStepsBox #ruleStep-" + index + " input[name='falseResult-" + index + "']").val();
		}
		// falseReturnType
		step.falseResult.returnType = $("#ruleStepsBox #ruleStep-" + index + " input[name='returnType-" + index + "']:checked").val();
		// finally, push the step to steps
		data.steps.push(step);
	});
	return data;
}

function addRuleParam(param) {
	var value = "";
	if (param != undefined && param != null) {
		value = "value='" + param + "'";
	}
	var row = $("<tr>")
		.append($("<td>").append($("<input name='paramName' type='text' " + value +  ">")))
		.append($("<td>").append($("<button type='button' " 
				+ " class='am-btn am-btn-xs am-btn-danger removeParamBtn'>" 
				+ "<span class='am-icon-minus'></span></button>")));
	$("#ruleDatas #paramsTable").append(row);
}

function addRuleSegment(segment) {
	var key = "";
	var value = "";
	if (segment != undefined && segment != null) {
		key = "value='" + segment.key + "'";
		value = "value=\"" + segment.value + "\"";
	}
	var row = $("<tr>")
		.append($("<td>").append($("<input name='segmentKey' type='text' " + key + ">")))
		.append($("<td>").append($("<input name='segmentValue' type='text' " + value + ">")))
		.append($("<td>").append($("<button type='button' " 
				+ " class='am-btn am-btn-xs am-btn-danger removeSegmentBtn'>" 
				+ "<span class='am-icon-minus'></span></button>")));
	$("#ruleDatas #segmentsTable").append(row);
}

function addRuleVar(ruleVar) {
	var v = {
		name: "",
		type: "RULES",
		params: ""
	};
	if (ruleVar != undefined && ruleVar != null) {
		v.name = "value='" + ruleVar.name + "'";
		v.type = ruleVar.type;
		v.params = "value=\"" + ruleVar.params + "\"";
	}
	var row = $("<tr>")
		.append($("<td>").append($("<input name='varName' type='text' " + v.name +">")))
		.append($("<td>").append($("<select name='varType'>" 
				+ "<option value='RULES' " + (v.type == "RULES" ? "selected" : "") + ">调用规则</option>" 
				+ "<option value='PLUGINS' " + (v.type == "PLUGINS" ? "selected" : "") + ">调用插件</option>" 
				+ "<option value='LET' " + (v.type == "LET" ? "selected" : "") + ">直接赋值</option>" 
				+ "</select>")))
		.append($("<td>").append($("<input name='varParams' type='text' " + v.params + ">")))
		.append($("<td>").append($("<button type='button' " 
				+ " class='am-btn am-btn-xs am-btn-danger removeVarBtn'>" 
				+ "<span class='am-icon-minus'></span></button>")));
	$("#ruleDatas #varsTable").append(row);
}

function addStepCondition(stepIndex, condition) {
	var c = {
		varName: "",
		operator: "EQ",
		value: ""
	};
	if (condition != undefined && condition != null) {
		c.varName = condition.varName;
		c.operator = condition.operator;
		c.value = "value=\"" + condition.value + "\"";
	}
	// rule vars
	var vars = getRuleVars();
	// varName select
	var varNameSelect = $("<select name='varName'>");
	if (vars) {
		$.each(vars, function(i, v) {
			varNameSelect.append($("<option value='" + v + "' " + (c.varName == v ? "selected" : "") + ">" + v + "</option>"));
		});
	}
	// varOperator select
	var varOperatorSelect = $("<select name='varOperator'>");
	varOperatorSelect.append($("<option value='EQ'" + (c.operator == "EQ" ? "selected" : "") + ">等于</option>"));
	varOperatorSelect.append($("<option value='NE'" + (c.operator == "NE" ? "selected" : "") + ">不等于</option>"));
	varOperatorSelect.append($("<option value='LT'" + (c.operator == "LT" ? "selected" : "") + ">小于</option>"));
	varOperatorSelect.append($("<option value='LE'" + (c.operator == "LE" ? "selected" : "") + ">小于等于</option>"));
	varOperatorSelect.append($("<option value='GT'" + (c.operator == "GT" ? "selected" : "") + ">大于</option>"));
	varOperatorSelect.append($("<option value='GE'" + (c.operator == "GE" ? "selected" : "") + ">大于等于</option>"));
	varOperatorSelect.append($("<option value='IN'" + (c.operator == "IN" ? "selected" : "") + ">子集</option>"));
	// row
	var row = $("<tr>")
		.append($("<td>").append(varNameSelect))
		.append($("<td>").append(varOperatorSelect))
		.append($("<td>").append($("<input name='varValue' type='text' " + c.value + ">")))
		.append($("<td>").append($("<button type='button' " 
				+ " class='am-btn am-btn-xs am-btn-danger removeConditionBtn'>" 
				+ "<span class='am-icon-minus'></span></button>")));
	$("#ruleStepsBox #ruleStep-" + stepIndex + " #conditionsTable").append(row);
}

function changeStepResults(matchType, index) {
	// true results
	$("#ruleStepsBox .trueResultBox").each(function(i, c) {
		var box = $(this);
		var thisIndex = box.data("index");
		if (index == undefined || index == null || thisIndex == index) {
			box.html("");
			if ("ACCEPTANCE" == matchType) {
				box.append($('<label><input type="radio" name="trueResult-' + 
						thisIndex + '" value="ACCEPT" checked> 通过</label>'));
				box.append($('<label><input type="radio" name="trueResult-' + 
						thisIndex + '" value="CAREFUL"> 审慎</label>'));
				box.append($('<label><input type="radio" name="trueResult-' + 
						thisIndex + '" value="REJECT"> 拒绝</label>'));
			} else {
				box.append($("<input type='text' name='trueResult-" + thisIndex + "' placeholder='该步骤的结果评分'>"));
			}
		}
	});
	// false results
	$("#ruleStepsBox .falseResultBox").each(function(i, c) {
		var box = $(this);
		var thisIndex = box.data("index");
		if (index == undefined || index == null || thisIndex == index) {
			box.html("");
			if ("ACCEPTANCE" == matchType) {
				box.append($('<label><input type="radio" name="falseResult-' + 
						thisIndex + '" value="ACCEPT"> 通过</label>'));
				box.append($('<label><input type="radio" name="falseResult-' + 
						thisIndex + '" value="CAREFUL"> 审慎</label>'));
				box.append($('<label><input type="radio" name="falseResult-' + 
						thisIndex + '" value="REJECT" checked> 拒绝</label>'));
			} else {
				box.append($("<input type='text' name='falseResult-" + thisIndex + "' placeholder='该步骤的结果评分'>"));
			}
		}
	});
}

function showStepResults(matchType, index, trueResult, falseResult) {
	// result value
	var trueValue = "";
	if (trueResult != undefined && trueResult != null) {
		trueValue = trueResult;
	}
	var falseValue = "";
	if (falseResult != undefined && falseResult != null) {
		falseValue = falseResult;
	}
	if ("ACCEPTANCE" == matchType) {
		if ("" == trueValue) {
			trueValue = "ACCEPT";
		}
		if ("" == falseValue) {
			falseValue = "REJECT";
		}
		$("#ruleStepsBox #ruleStep-" + index + " input[name='trueResult-" + index + "']").each(function(i, c) {
			var radio = $(this);
			if (radio.val() == trueValue) {
				radio.attr("checked", true);
			}
		});
		$("#ruleStepsBox #ruleStep-" + index + " input[name='falseResult-" + index + "']").each(function(i, c) {
			var radio = $(this);
			if (radio.val() == falseValue) {
				radio.attr("checked", true);
			}
		});
	} else {
		if ("" != trueValue) {
			$("#ruleStepsBox #ruleStep-" + index + " input[name='trueResult-" + index + "']").val(trueValue);
		}
		if ("" != falseValue) {
			$("#ruleStepsBox #ruleStep-" + index + " input[name='falseResult-" + index + "']").val(falseValue);
		}
	}
}



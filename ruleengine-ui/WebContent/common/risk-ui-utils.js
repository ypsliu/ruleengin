
var RestClient = {
		
	baseUrl: "/api/v1",
	
	doAjax: function(url, type, async, data, success, error, complete) {
		var errorFun = error;
		if (errorFun == null) {
			errorFun = function(result) {
				showErrorPage(result);
			};
		}
		$.ajax({
			url: this.baseUrl + url,
			type: type,
			contentType: "application/json",
			data: data ? JSON.stringify(data) : null,
			dataType: "json",
			processData: false,
			success: success,
			error: errorFun,
			complete: complete,
			async: async
		});
	},

	syncGet: function(url, success, error, complete) {
		this.doAjax(url, "GET", false, null, success, error, complete);
	},

	get: function(url, success, error, complete) {
		this.doAjax(url, "GET", true, null, success, error, complete);
	},
	
	syncPost: function(url, data, success, error, complete) {
		this.doAjax(url, "POST", false, data, success, error, complete);
	},
	
	post: function(url, data, success, error, complete) {
		this.doAjax(url, "POST", true, data, success, error, complete);
	},
	
	syncPut: function(url, data, success, error, complete) {
		this.doAjax(url, "PUT", false, data, success, error, complete);
	},
	
	put: function(url, data, success, error, complete) {
		this.doAjax(url, "PUT", true, data, success, error, complete);
	},
	
	syncDel: function(url, success, error, complete) {
		this.doAjax(url, "DELETE", false, null, success, error, complete);
	},
	
	del: function(url, success, error, complete) {
		this.doAjax(url, "DELETE", true, null, success, error, complete);
	}
	
}

function showErrorPage(result) {
	if (result == null || result.status == null || result.status == 200 || result.status == 201) {
		// no error
		return;
	}
	// TODO
	alert(result.status);
}

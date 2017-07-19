(function() {

	var module = angular.module("myApp");

	module.controller("ReadCompanyCtrl", ReadCompanyCtrlCtor);

	function ReadCompanyCtrlCtor(mainCompanyService, $state) {
		var company = {};
		var self = this;
        //Read company
		var promise = mainCompanyService.readCompany();
		promise.then(function(resp) {

			self.company = resp.data;
			compId = self.company.id;

		}, function(err) {
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				self.error = err.data;
		});

	}

})();
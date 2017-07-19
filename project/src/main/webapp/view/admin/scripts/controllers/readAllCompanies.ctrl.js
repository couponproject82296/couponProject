(function() {

	var module = angular.module("myApp");

	module.controller("ReadAllCompaniesCtrl", ReadAllCompaniesCtrlCtor);

	function ReadAllCompaniesCtrlCtor(mainAdminService, $state) {
		this.companies = [];
		var self = this;
        //Read all companies function
		var promise = mainAdminService.readAllCompanies();
		promise.then(function(resp) {

			self.companies = resp.data;

		}, function(err) {
			//Redirection if the user reached an internal page without performing a login.
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				self.error = err.data;
		});
        
		//Table sorting
		this.order = "";
		this.goUp = false;

		this.setOrder = function(category) {
			this.goUp = (this.order != category) ? false : !this.goUp;
			this.order = category;

		}

	}

})();
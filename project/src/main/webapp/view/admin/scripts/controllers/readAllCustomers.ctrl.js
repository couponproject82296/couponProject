(function() {

	var module = angular.module("myApp");

	module.controller("ReadAllCustomersCtrl", ReadAllCustomersCtrlCtor);

	function ReadAllCustomersCtrlCtor(mainAdminService, $state) {
		this.customers = [];
		var self = this;
		//Read all customers function
		var promise = mainAdminService.readAllCustomers();
		promise.then(function(resp) {

			self.customers = resp.data;

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
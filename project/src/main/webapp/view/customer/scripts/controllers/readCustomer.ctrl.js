(function() {

	var module = angular.module("myApp");

	module.controller("ReadCustomerCtrl", ReadCustomerCtrlCtor);

	function ReadCustomerCtrlCtor(mainCustomerService, $state) {
		this.customer = {};
		var self = this;
        
		var promise = mainCustomerService.readCustomer();
		promise.then(function(resp) {

			self.customer = resp.data;

		}, function(err) {
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				self.error = err.data;
		});

	}

})();
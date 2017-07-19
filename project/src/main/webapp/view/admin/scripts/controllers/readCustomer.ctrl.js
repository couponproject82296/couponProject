(function() {

	var module = angular.module("myApp");

	module.controller("ReadCustomerCtrl", ReadCustomerCtrlCtor);

	function ReadCustomerCtrlCtor(mainAdminService, $log, $state) {
		var customer = {};
		var self = this;
		self.failure = false;
		
		//Reading all the customers in order to display them in a table.
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

		this.giveId = function(id) {
            //Read customer method
			var promiseGet = mainAdminService.readCustomer(id);
			promiseGet.then(function(resp) {

				self.customer = resp.data;

			}, function(err) {
				//Redirection if the user reached an internal page without performing a login.
				if (err.status == 401)
					$state.go("errorUnauthorized");
				self.error = err;
				self.failure = true;
				//Refreshing after exception is thrown
				function refresh() {
					location.reload(true);
				}
				//A timer, in order to refresh after the 6 seconds exception pop-up. 
				setTimeout(refresh, 6001);

			});

		}
	}

})();
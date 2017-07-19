(function() {

	var module = angular.module("myApp");

	module.controller("ReadCompanyCtrl", ReadCompanyCtrlCtor);

	function ReadCompanyCtrlCtor(mainAdminService, $log, $state) {
		var company = {};
		var self = this;
		self.failure = false;
		
		//Reading all the companies in order to display them in a table.
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
		
		

		this.giveId = function(id) {
			
			var promiseGet = mainAdminService.readCompany(id);
			promiseGet.then(function(resp) {
				
				self.company = resp.data;

			}, function(err) {
				//Redirection if the user reached an internal page without performing a login.
				if (err.status == 401)
					$state.go("errorUnauthorized");

				self.error = err.status;
				
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
(function() {

	var module = angular.module("myApp");

	module.controller("UpdateCompanyCtrl", UpdateCompanyCtrlCtor);

	function UpdateCompanyCtrlCtor(mainAdminService, $scope, $state) {
		this.companies = [];
		var self = this;
		self.edit = false;
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
				self.error = err.status;

		});
        //Conditions to enable buttons switching.
		$scope.editing = false;
		$scope.editEntry = function(c) {
			$scope.editing = !$scope.editing;
		}
        //Refresh method being called upon need.
		function refresh() {
			location.reload(true);
			$state.go("updateCompany");
		}
        //Refreshing after cancellation, gives a clean view if data was changed in the input fields.
		this.refreshAfterCanceling = function() {
			setTimeout(refresh, 1);
		}
		//An email pattern that will help in maintaining a right data structure(used with ng-pattern).
		$scope.emailRegExp = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
        
		//Triggers updateCompany method
		this.pressUpdateToSave = function(index) {
			var promiseUpdate = mainAdminService
					.updateCompany(this.companies[index]);
			promiseUpdate.then(function(resp) {

				companies = resp.data;

			}, function(err) {

				self.error = err.data;
				self.failure = true;
				setTimeout(refresh, 6001);
			});

		}
		//Table sorting
		this.order = "";
		this.goUp = false;

		this.setOrder = function(category) {
			this.goUp = (this.order != category) ? false : !this.goUp;
			this.order = category;

		}

	}

})();
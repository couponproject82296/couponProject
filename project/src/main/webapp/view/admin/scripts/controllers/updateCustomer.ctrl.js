(function() {

	var module = angular.module("myApp");

	module.controller("UpdateCustomerCtrl", UpdateCustomerCtrlCtor);

	function UpdateCustomerCtrlCtor(mainAdminService, $state, $scope) {
		this.customers = [];
		var self = this;
		self.edit = false;
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
				self.error = err.status;
		});
		//Conditions to switch buttons.
		$scope.editing = false;
		$scope.editEntry = function(c) {
			$scope.editing = !$scope.editing;
		}
		//Refresh method being called upon need.
		function refresh() {
			location.reload(true);
			$state.go("updateCustomer");
		}
		//Refreshing after cancellation, gives a clean view if data was changed in the input fields.
		this.refreshAfterCanceling = function() {
			setTimeout(refresh, 1);
		}
		//Triggers updateCustomer method
		this.pressUpdateToSave = function(index) {

			var promiseUpdate = mainAdminService
					.updateCustomer(this.customers[index]);
			promiseUpdate.then(function(resp) {

				customers = resp.data;

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
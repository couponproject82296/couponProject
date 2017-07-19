(function() {

	var module = angular.module("myApp");

	module.controller("CreateCustomerCtrl", CreateCustomerCtrlCtor);

	function CreateCustomerCtrlCtor(mainAdminService, javaBeansCtors, $scope,
			$state, $ngConfirm) {

		this.success = false;
		this.failure = false;
		this.newCustomer = {};
		//Create customer method:
		this.createCustomer = function() {

			if (this.newCustomer == undefined
					|| this.newCustomer.id == undefined
					|| this.newCustomer.custName == undefined
					|| this.newCustomer.password == undefined) {
				this.success = false;
				this.failure = true;
				return;
			}
			this.success = false;
			this.failure = false;
			var self = this;

			var promisePost = mainAdminService.createCustomer(this.newCustomer);
			promisePost
					.then(
							function(resp) {

								$scope.newCustomerId = resp.data;
								self.success = true;
								self.failure = false;
								//Angular confirm in order to display a message after creating a new customer.
								$ngConfirm({
									autoClose : 'No|10000',
									theme : 'dark',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Redirection!',
									content : 'The id of the new customer is: <strong>{{newCustomerId}}</strong>.<br />Would you like to be transferred to see the new customer under all the customers?',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-orange',
											action : function(button) {
												/*After process completed if the user chooses yes, he will be redirected to view
												 * an updated list of all the customers.
												 */
												$state.go("readAllCustomers");
											}
										},
										No : function(button) {
											//If he chooses no, the current page will be reloaded.
											location.reload();
											$state.go("createCustomer");
										},
									}
								});

							}, function(err) {
								//Redirection if the user reached an internal page without performing a login.
								if (err.status == 401)
									$state.go("errorUnauthorized");
								//In any other exception a message will be displayed
								self.error = err.data;
								self.success = false;
								self.failure = true;
							});

		}
		 //A password pattern that will help in maintaining a right data structure(used with ng-pattern).
		$scope.passwordRegExp = /^[A-Za-z0-9._%+-]/;

	}

})();
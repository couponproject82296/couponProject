(function() {

	var module = angular.module("myApp");

	module.controller("CreateCompanyCtrl", CreateCompanyCtrlCtor);

	function CreateCompanyCtrlCtor(mainAdminService, javaBeansCtors, $scope,
			$state, $ngConfirm) {

		this.success = false;
		this.failure = false;
		this.newCompany = {};
        //Create company
		this.createCompany = function() {

			if (this.newCompany == undefined || this.newCompany.id == undefined
					|| this.newCompany.compName == undefined
					|| this.newCompany.password == undefined
					|| this.newCompany.email == undefined) {
				this.success = false;
				this.failure = true;
				return;

			}
			this.success = false;
			this.failure = false;
			var self = this;

			var promisePost = mainAdminService.createCompany(this.newCompany);
			promisePost
					.then(
							function(resp) {

								$scope.newCompanyId = resp.data;

								self.success = true;
								self.failure = false;
                                //Angular confirm in order to display a message after creating a new company.
								$ngConfirm({
									autoClose : 'No|10000',
									theme : 'dark',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Redirection!',
									content : 'The id of the new company is: <strong>{{newCompanyId}}</strong>.<br />Would you like to be transferred to see the new company under all the companies?',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-orange',
											action : function(button) {
												/*After process completed if the user chooses yes, he will be redirected to view
												 * an updated list of all the companies.
												 */
												
												$state.go("readAllCompanies");
											}
										},
										No : function(button) {
											//If he chooses no, the current page will be reloaded.
											location.reload();
											$state.go("createCompany");
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
        //A password and email pattern that will help in maintaining a right data structure(used with ng-pattern).
		$scope.passwordRegExp = /^[A-Za-z0-9._%+-]/;
		$scope.emailRegExp = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$/;
	}

})();
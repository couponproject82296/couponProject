(function() {

	var module = angular.module("myApp");

	module.controller("DeleteCompanyCtrl", DeleteCompanyCtrlCtor);

	function DeleteCompanyCtrlCtor(mainAdminService, $state, $ngConfirm) {
		this.companies = [];
		var self = this;

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
       //Delete company method
		this.giveIdForDeletion = function(index) {
			var company = this.companies[index];
			//Angular confirm in order to display a message after deleting a company.
			$ngConfirm({
				autoClose : 'No|10000',
				theme : 'dark',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
				title : 'Warning!',
				content : 'Are you sure you want to permanently delete the selected company?',
				buttons : {
					Yes : {
						text : 'Yes',
						btnClass : 'btn-orange',
						action : function(button) {
							//This line execution triggers ngCconfirm.
							mainAdminService.deleteCompany(company.id);
							$ngConfirm({
								theme : 'bootstrap',
								animation : 'top',
								closeAnimation : 'scale',
								title : 'Deleted!',
								content : '',
								boxWidth : '30%',
								useBootstrap : false,
								onClose : function() {
									location.reload();
									$state.go("deleteCompany");
								},
							});

						}
					},
					No : function(button) {

					},
				}
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
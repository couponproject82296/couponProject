(function() {

	var module = angular.module("myApp");

	module.controller("DeleteCustomerCtrl", DeleteCustomerCtrlCtor);

	function DeleteCustomerCtrlCtor(mainAdminService, $state, $ngConfirm) {
		this.customers = [];
		var self = this;

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
		 //Delete customer method
		this.giveIdForDeletion = function(index) {
			var customer = this.customers[index];
			//Angular confirm in order to display a message after deleting a customer.
			$ngConfirm({
				autoClose : 'No|10000',
				theme : 'dark',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
				title : 'Warning!',
				content : 'Are you sure you want to permanently delete the selected customer?',
				buttons : {
					Yes : {
						text : 'Yes',
						btnClass : 'btn-orange',
						action : function(button) {
							//This line execution triggers ngConfirm.
							mainAdminService.deleteCustomer(customer.id);
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
									$state.go("deleteCustomer");
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
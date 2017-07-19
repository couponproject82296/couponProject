(function() {

	var module = angular.module("myApp");

	module.controller("ReadAllCouponsCtrl", ReadAllCouponsCtrlCtor);

	function ReadAllCouponsCtrlCtor(mainCompanyService, $state,$scope) {
		this.coupons = [];
		var self = this;
        //Read all coupons.
		var promise = mainCompanyService.readAllCoupons();
		promise.then(function(resp) {

			self.coupons = resp.data;

		}, function(err) {
			//Redirection if the user reached an internal page without performing a login.
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				self.error = err.data;
		});
        //Table sorting.
		this.order = "";
		this.goUp = false;

		this.setOrder = function(category) {
			this.goUp = (this.order != category) ? false : !this.goUp;
			this.order = category;

		}
		

	}

})();
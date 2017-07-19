(function() {

	var module = angular.module("myApp");

	module.controller("ReadAllPurchasedCouponsCtrl",
			ReadAllPurchasedCouponsCtrlCtor);

	function ReadAllPurchasedCouponsCtrlCtor(mainCustomerService, $state) {
		this.coupons = [];
		var self = this;
        //Reading purchased coupons
		var promise = mainCustomerService.readAllPurchasedCoupons();
		promise.then(function(resp) {

			self.coupons = resp.data;

		}, function(err) {
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				self.error = err.data;
		});
        //Table sorting
		this.order = "";
		this.goUp = false;

		this.setOrder = function(category) {
			this.goUp = (this.order != category) ? false : !this.goUp;
			this.order = category;

		}

	}

})();
(function() {

	var module = angular.module("myApp");

	module.controller("ReadAllPurchasedCouponsByTypeCtrl",
			ReadAllPurchasedCouponsByTypeCtrlCtor);

	function ReadAllPurchasedCouponsByTypeCtrlCtor(mainCustomerService, $state) {
		this.couponsType = [];
		var self = this;
		//Read purchased coupons by price
		this.giveType = function() {
			var promiseGet = mainCustomerService
					.readAllPurchasedCouponsByType(this.type);

			promiseGet.then(function(resp) {

				self.couponsType = resp.data;

			}, function(err) {
				if (err.status == 401)
					$state.go("errorUnauthorized");
				else
					self.error = err.data;
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
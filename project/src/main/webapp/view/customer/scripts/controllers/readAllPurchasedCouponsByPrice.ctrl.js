(function() {

	var module = angular.module("myApp");

	module.controller("readAllPurchasedCouponsByPriceCtrl",
			readAllPurchasedCouponsByPriceCtrlCtor);

	function readAllPurchasedCouponsByPriceCtrlCtor(mainCustomerService, $state) {
		this.couponsPrice = [];
		var self = this;
        //Read purchased coupons by price
		this.givePrice = function() {
			var promiseGet = mainCustomerService
					.readAllPurchasedCouponsByPrice(this.price);

			promiseGet.then(function(resp) {

				self.couponsPrice = resp.data;

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
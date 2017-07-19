(function() {

	var module = angular.module("myApp");

	module.controller("ReadAllCouponsByPriceCtrl",
			ReadAllCouponsByPriceCtrlCtor);

	function ReadAllCouponsByPriceCtrlCtor(mainCompanyService, $state) {
		this.couponsPrice = [];
		var self = this;
		//Read coupons by price.
		this.givePrice = function() {
			var promiseGet = mainCompanyService
					.readAllCouponsByPrice(this.price);
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
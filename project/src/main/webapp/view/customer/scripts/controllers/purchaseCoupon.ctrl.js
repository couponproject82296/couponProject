(function() {

	var module = angular.module("myApp");

	module.controller("PurchaseCouponCtrl", PurchaseCouponCtrlCtor);

	function PurchaseCouponCtrlCtor(mainCustomerService, $state) {
		this.coupons = [];
		var self = this;
        //Reading all Unpurchased coupons (a customized method).
		var promise = mainCustomerService.readAllUnpurchasedCoupons();
		promise.then(function(resp) {

			self.coupons = resp.data;

		}, function(err) {
			//Refreshing after cancellation, gives a clean view if data was changed in the input fields.
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				self.error = err.data;
		});
        //Purchase coupon
		this.buyMe = function(index) {
			var coupon = this.coupons[index];
			var promise1 = mainCustomerService.purchaseCoupon(coupon);
			promise1.then(function(resp) {
			location.reload();
			$state.go("purchaseCoupon");
			}, function(err) {
				//Refreshing after cancellation, gives a clean view if data was changed in the input fields.
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
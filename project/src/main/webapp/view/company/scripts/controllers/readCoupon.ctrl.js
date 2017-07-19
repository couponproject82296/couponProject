(function() {

	var module = angular.module("myApp");

	module.controller("ReadCouponCtrl", ReadCouponCtrlCtor);

	function ReadCouponCtrlCtor(mainCompanyService, $state) {
		var coupon = {};
		this.coupons = [];
		var self = this;
		self.failure = false;
		//Reading all the current logged company coupons to display them in a table.
		var promise = mainCompanyService.readAllCoupons();
		promise.then(function(resp) {

			self.coupons = resp.data;

		}, function(err) {
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				self.error = err.data;
		});
		
		//Read coupon
		this.giveId = function(id) {
			var promiseGet = mainCompanyService.readCoupon(id);
			promiseGet.then(function(resp) {

				self.coupon = resp.data;

			}, function(err) {
				if (err.status == 401)
					$state.go("errorUnauthorized");
				self.error = err;
				self.failure = true;
				function refresh() {
					location.reload(true);
				}
				setTimeout(refresh, 6001);
			});

		}
	}

})();
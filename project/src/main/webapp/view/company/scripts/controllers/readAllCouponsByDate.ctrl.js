(function() {

	var module = angular.module("myApp");

	module.controller("ReadAllCouponsByDateCtrl", ReadAllCouponsByDateCtrlCtor);

	function ReadAllCouponsByDateCtrlCtor(mainCompanyService, $state) {
		this.couponsDate = [];
		var self = this;
        //Read coupons by end date.
		this.giveDate = function() {
			var promiseGet = mainCompanyService
					.readAllCouponsByDate(this.endDate);
			promiseGet.then(function(resp) {

				self.couponsDate = resp.data;

			}, function(err) {
				//Redirection if the user reached an internal page without performing a login.
				if (err.status == 401)
					$state.go("errorUnauthorized");
				else
					self.error = err.data;
			});

		}
		//Table sorting.
		this.order = "";
		this.goUp = false;

		this.setOrder = function(category) {
			this.goUp = (this.order != category) ? false : !this.goUp;
			this.order = category;

		}
        /*Preventing from searching expired coupons(will return an empty result.
         *expired coupons were deleted by the daily thread).
         *Used by min(in the HTML).
         */
		this.today = new Date();
	}

})();
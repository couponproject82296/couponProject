(function() {

	var module = angular.module("myApp");

	module.controller("UpdateCouponCtrl", UpdateCouponCtrlCtor);

	function UpdateCouponCtrlCtor(mainCompanyService, $scope, $state) {
		this.coupons = [];
		var self = this;
		self.edit = false;
		self.failure = false;
		//Reading all the current logged company coupons to display them in a table.
		var promise = mainCompanyService.readAllCoupons();
		promise.then(function(resp) {

			self.coupons = resp.data;
            //Converting from string to date object
			this.convertToDate = function() {
				for (var i = 0; i < self.coupons.length; i++) {
					var convertedEndDate = new Date(self.coupons[i].endDate);
					self.coupons[i].endDate = convertedEndDate;
				}
			}

			convertToDate();

		}, function(err) {
			if (err.status == 401)
				$state.go("errorUnauthorized");
			else
				alert(err.data);
		});
		//Conditions to enable buttons switching.
		$scope.editing = false;
		$scope.editEntry = function(c) {
			$scope.editing = !$scope.editing;
		}
		//Refresh method being called upon need.
		function refresh() {
			location.reload(true);
			$state.go("updateCoupon");
		}
		//Refreshing after cancellation, gives a clean view if data was changed in the input fields.
		this.refreshAfterCanceling = function() {
			setTimeout(refresh, 1);
		}
		//Triggers updateCoupon method
		this.pressUpdateToSave = function(index) {
			var promiseUpdate = mainCompanyService
					.updateCoupon(this.coupons[index]);
			promiseUpdate.then(function(resp) {

				coupons = resp.data;

			}, function(err) {

				self.error = err.data;
				self.failure = true;
				setTimeout(refresh, 6001);

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
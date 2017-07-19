(function() {

	var module = angular.module("myApp");

	module.controller("DeleteCouponCtrl", DeleteCouponCtrlCtor);

	function DeleteCouponCtrlCtor(mainCompanyService, $ngConfirm, $state) {
		this.coupons = [];
		var self = this;
		//Reading all coupons, displaying them in a table.
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

		this.giveIdForDeletion = function(index) {
			var coupon = this.coupons[index];

			$ngConfirm({
				autoClose : 'No|10000',
				theme : 'dark',
				animation : 'rotateYR',
				closeAnimation : 'scale',
				animationSpeed : 500,
				boxWidth : '30%',
				useBootstrap : false,
				title : 'Warning!',
				content : 'Are you sure you want to permanently delete the selected coupon?',
				buttons : {
					Yes : {
						text : 'Yes',
						btnClass : 'btn-orange',
						action : function(button) {
							mainCompanyService.deleteCoupon(coupon.id);
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
									$state.go("deleteCoupon");
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
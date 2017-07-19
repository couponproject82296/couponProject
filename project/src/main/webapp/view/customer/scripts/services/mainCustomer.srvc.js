(function() {

	var module = angular.module("myApp");

	module.service("mainCustomerService", mainCustomerServiceCtor);

	function mainCustomerServiceCtor($q, $http) {

		// Purchase Coupon()
		this.purchaseCoupon = function(purchasedCoupon) {
			return $http.post('http://localhost:8080/project/webapi/customer/coupon/', purchasedCoupon);
		}

		// Read Customer()
		this.readCustomer = function() {
			return $http.get('http://localhost:8080/project/webapi/customer/customer/');
		}

		// Read All Purchased Coupons()
		this.readAllPurchasedCoupons = function() {
			return $http.get('http://localhost:8080/project/webapi/customer/coupons/');
		}

		// Read All Purchased Coupons By Type()
		this.readAllPurchasedCouponsByType = function(myType) {
			return $http.get('http://localhost:8080/project/webapi/customer/coupons/type/' + myType);
		}

		// Read All Purchased Coupons By Price()
		this.readAllPurchasedCouponsByPrice = function(myPrice) {
			return $http.get('http://localhost:8080/project/webapi/customer/coupons/price/' + myPrice);
		}
		
		// The following method will serve the Purchase Coupon page:
		
		// Read All Unpurchased Coupons()
		this.readAllUnpurchasedCoupons = function() {
			return $http.get('http://localhost:8080/project/webapi/customer/unpurchasedCoupons/');
		}

	}

})();
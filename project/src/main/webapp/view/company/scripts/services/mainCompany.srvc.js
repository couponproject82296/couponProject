(function() {

	var module = angular.module("myApp");

	module.service("mainCompanyService", mainCompanyServiceCtor);

	function mainCompanyServiceCtor($http) {

		// Create Coupon()
		this.createCoupon = function(coupon) {
			return $http.post('http://localhost:8080/project/webapi/company/coupon/', coupon);
		}

		// Read Company()
		this.readCompany = function() {
			return $http.get('http://localhost:8080/project/webapi/company/company/');
		}

		// Read Coupon()
		this.readCoupon = function(idNum) {
			return $http.get('http://localhost:8080/project/webapi/company/coupon/' + idNum);
		}

		// Read All Coupons()
		this.readAllCoupons = function() {
			return $http.get('http://localhost:8080/project/webapi/company/coupons/');
		}

		// Read All Coupons By Type()
		this.readAllCouponsByType = function(myType) {
			return $http.get('http://localhost:8080/project/webapi/company/coupons/type/' + myType);
		}

		// Read All Coupons By Price()
		this.readAllCouponsByPrice = function(myPrice) {
			return $http.get('http://localhost:8080/project/webapi/company/coupons/price/' + myPrice);
		}

		// Read All Coupons By Date()
		this.readAllCouponsByDate = function(myDate) {

			// Converting to a date object:
			var requestDate = new Date(myDate);
			
			// Converting to a String in the proper format ddMMyyyy:
		    var formatDate = padDate(requestDate.getDate()) +
		                  padDate(1 + requestDate.getMonth()) +
		                  padDate(requestDate.getFullYear());

			function padDate(i) {
			    return (i < 10) ? "0" + i : "" + i;
			}
			
			return $http.get('http://localhost:8080/project/webapi/company/coupons/date/' + formatDate);
			
		}

		// Update Coupon()
		this.updateCoupon = function(updatedCoupon) {
			return $http.put('http://localhost:8080/project/webapi/company/coupon/'+ updatedCoupon.id, updatedCoupon);
		}

		// Delete Coupon()
		this.deleteCoupon = function(idNumDC) {
			return $http.delete('http://localhost:8080/project/webapi/company/coupon/' + idNumDC);
		}

	}

})();
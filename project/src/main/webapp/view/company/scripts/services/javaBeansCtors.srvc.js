(function() {

	var module = angular.module("myApp");

	module.service("javaBeansCtors", javaBeansCtorsCtor);

	function javaBeansCtorsCtor() {

		this.Coupon = function(id, title, startDate, endDate, amount, type,
				message, price, image) {
			this.id = id;
			this.title = title;
			this.startDate = startDate;
			this.endDate = endDate;
			this.amount = amount;
			this.type = type;
			this.message = message;
			this.price = price;
			this.image = image;
		}

	}

})();
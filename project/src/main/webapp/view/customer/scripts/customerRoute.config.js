(function() {

	var module = angular.module("myApp");

	module.config([ '$locationProvider', function($locationProvider) {
		$locationProvider.hashPrefix('');
	} ]);

	// router config
	module.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider.state("purchaseCoupon", {
			url : "/purchaseCoupon",
			templateUrl : "templates/purchaseCoupon.html",
			controller : "PurchaseCouponCtrl as pc"
		}).state("readCustomer", {
			url : "/readCustomer",
			templateUrl : "templates/readCustomer.html",
			controller : "ReadCustomerCtrl as rc"
		}).state("readAllPurchasedCoupons", {
			url : "/readAllPurchasedCoupons",
			templateUrl : "templates/readAllPurchasedCoupons.html",
			controller : "ReadAllPurchasedCouponsCtrl as rapc"
		}).state("readAllPurchasedCouponsByType", {
			url : "/readAllPurchasedCouponsByType",
			templateUrl : "templates/readAllPurchasedCouponsByType.html",
			controller : "ReadAllPurchasedCouponsByTypeCtrl as rapct"
		}).state("readAllPurchasedCouponsByPrice", {
			url : "/readAllPurchasedCouponsByPrice",
			templateUrl : "templates/readAllPurchasedCouponsByPrice.html",
			controller : "readAllPurchasedCouponsByPriceCtrl as rapcp"
		}).state("errorUnauthorized", {
			url : "/errorUnauthorized",
			templateUrl : "templates/error401.html"
		}).state("errorNotFound", {
			url : "/errorNotFound",
			templateUrl : "templates/error404.html",
		});

		$urlRouterProvider.when("", "/readAllPurchasedCoupons");
		$urlRouterProvider.otherwise("/errorNotFound");
	});

})();
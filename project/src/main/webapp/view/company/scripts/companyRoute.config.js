(function() {

	var module = angular.module("myApp");

	module.config([ '$locationProvider', function($locationProvider) {
		$locationProvider.hashPrefix('');
	} ]);

	// router config
	module.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider.state("createCoupon", {
			url : "/createCoupon",
			templateUrl : "templates/createCoupon.html",
			controller : "CreateCouponCtrl as cc"
		}).state("readCompany", {
			url : "/readCompany",
			templateUrl : "templates/readCompany.html",
			controller : "ReadCompanyCtrl as rc"
		}).state("readCoupon", {
			url : "/readCoupon",
			templateUrl : "templates/readCoupon.html",
			controller : "ReadCouponCtrl as rcp"
		}).state("readAllCoupons", {
			url : "/readAllCoupons",
			templateUrl : "templates/readAllCoupons.html",
			controller : "ReadAllCouponsCtrl as rac"
		}).state("readAllCouponsByType", {
			url : "/readAllCouponsByType",
			templateUrl : "templates/readAllCouponsByType.html",
			controller : "ReadAllCouponsByTypeCtrl as ract"
		}).state("readAllCouponsByPrice", {
			url : "/readAllCouponsByPrice",
			templateUrl : "templates/readAllCouponsByPrice.html",
			controller : "ReadAllCouponsByPriceCtrl as racp"
		}).state("readAllCouponsByDate", {
			url : "/readAllCouponsByDate",
			templateUrl : "templates/readAllCouponsByDate.html",
			controller : "ReadAllCouponsByDateCtrl as racd"
		}).state("updateCoupon", {
			url : "/updateCoupon",
			templateUrl : "templates/updateCoupon.html",
			controller : "UpdateCouponCtrl as uc"
		}).state("deleteCoupon", {
			url : "/deleteCoupon",
			templateUrl : "templates/deleteCoupon.html",
			controller : "DeleteCouponCtrl as dc"
		}).state("errorUnauthorized", {
			url : "/errorUnauthorized",
			templateUrl : "templates/error401.html"
		}).state("errorNotFound", {
			url : "/errorNotFound",
			templateUrl : "templates/error404.html",
		});

		$urlRouterProvider.when("", "/readAllCoupons");
		$urlRouterProvider.otherwise("/errorNotFound");
	});

})();
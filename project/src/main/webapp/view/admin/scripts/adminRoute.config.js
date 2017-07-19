(function() {

	var module = angular.module("myApp");

	module.config([ '$locationProvider', function($locationProvider) {
		$locationProvider.hashPrefix('');
	} ]);

	// router config
	module.config(function($stateProvider, $urlRouterProvider) {

		$stateProvider.state("createCompany", {
			url : "/createCompany",
			templateUrl : "templates/createCompany.html",
			controller : "CreateCompanyCtrl as cc"
		}).state("readCompany", {
			url : "/readCompany",
			templateUrl : "templates/readCompany.html",
			controller : "ReadCompanyCtrl as rc"
		}).state("readAllCompanies", {
			url : "/readAllCompanies",
			templateUrl : "templates/readAllCompanies.html",
			controller : "ReadAllCompaniesCtrl as rac"
		}).state("updateCompany", {
			url : "/updateCompany",
			templateUrl : "templates/updateCompany.html",
			controller : "UpdateCompanyCtrl as uc"
		}).state("deleteCompany", {
			url : "/deleteCompany",
			templateUrl : "templates/deleteCompany.html",
			controller : "DeleteCompanyCtrl as dco"
		}).state("createCustomer", {
			url : "/createCustomer",
			templateUrl : "templates/createCustomer.html",
			controller : "CreateCustomerCtrl as ccu"
		}).state("readCustomer", {
			url : "/readCustomer",
			templateUrl : "templates/readCustomer.html",
			controller : "ReadCustomerCtrl as rcu"
		}).state("readAllCustomers", {
			url : "/readAllCustomers",
			templateUrl : "templates/readAllCustomers.html",
			controller : "ReadAllCustomersCtrl as racu"
		}).state("updateCustomer", {
			url : "/updateCustomer",
			templateUrl : "templates/updateCustomer.html",
			controller : "UpdateCustomerCtrl as ucu"
		}).state("deleteCustomer", {
			url : "/deleteCustomer",
			templateUrl : "templates/deleteCustomer.html",
			controller : "DeleteCustomerCtrl as dcu"
		}).state("errorUnauthorized", {
			url : "/errorUnauthorized",
			templateUrl : "templates/error401.html"
		}).state("errorNotFound", {
			url : "/errorNotFound",
			templateUrl : "templates/error404.html",
		});

		$urlRouterProvider.when("", "/readAllCompanies");
		$urlRouterProvider.otherwise("/errorNotFound");
	});

})();
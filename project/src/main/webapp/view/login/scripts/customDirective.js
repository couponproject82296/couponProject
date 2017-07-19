(function() {

	var module = angular.module("myApp");

	module.controller("couponImagesCtrl",['$scope' ,function ($scope) {
		$scope.mycoupons= [
			'https://tinyurl.com/ybfcdlmy',
			'https://tinyurl.com/ybodcn48',
			'https://tinyurl.com/yaxhnxve',
			'https://tinyurl.com/yckmwwub',
			'https://tinyurl.com/y9hjbwbs',
			'https://tinyurl.com/hj8qdcs'
		];
	}]);


	
	module.directive('ylCouponsImages' , [function() {
		
		return {
			restrict: 'E',
			scope: {
				mycoupons: '='
			},
			template: '<img ng-src="{{mycoupons[random]}}" class="center" ></img>' ,
			controller: function($scope) {			
				$scope.random = Math.floor(Math.random() * $scope.mycoupons.length);		
			}
		};
	}]);


})();
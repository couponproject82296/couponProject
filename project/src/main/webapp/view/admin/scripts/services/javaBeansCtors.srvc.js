(function() {

	var module = angular.module("myApp");

	module.service("javaBeansCtors", javaBeansCtorsCtor);

	function javaBeansCtorsCtor() {

		this.Company = function(id, compName, password, email) {
			this.id = id;
			this.compName = compName;
			this.password = password;
			this.email = email;
		}

		this.Customer = function(id, custName, password) {
			this.id = id;
			this.custName = custName;
			this.password = password;
		}

	}

})();
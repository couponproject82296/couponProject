(function() {

	var module = angular.module("myApp");

	module.service("mainAdminService", mainAdminServiceCtor);

	function mainAdminServiceCtor($q, $http) {

		// Create Company()
		this.createCompany = function(company) {
			return $http.post('http://localhost:8080/project/webapi/admin/company/', company);
		}

		// Read Company()
		this.readCompany = function(idNum) {
			return $http.get('http://localhost:8080/project/webapi/admin/company/' + idNum);
		}
		
		// Read All Companies()
		this.readAllCompanies = function() {	
			return $http.get('http://localhost:8080/project/webapi/admin/companies/');
		}

		// Update Company()
		this.updateCompany = function(updatedCompany) {
			return $http.put('http://localhost:8080/project/webapi/admin/company/'+ updatedCompany.id, updatedCompany);
		}

		// Delete Company()
		this.deleteCompany = function(idNumDC) {
			return $http.delete('http://localhost:8080/project/webapi/admin/company/' + idNumDC);
		}

		// Create Customer()
		this.createCustomer = function(customer) {
			return $http.post('http://localhost:8080/project/webapi/admin/customer/', customer);
		}

		// Read Customer()
		this.readCustomer = function(idNumCu) {
			return $http.get('http://localhost:8080/project/webapi/admin/customer/' + idNumCu);
		}

		// Read All Customers()
		this.readAllCustomers = function() {
			return $http.get('http://localhost:8080/project/webapi/admin/customers/');
		}

		// Update Customer()
		this.updateCustomer = function(updatedCustomer) {
			return $http.put('http://localhost:8080/project/webapi/admin/customer/'+ updatedCustomer.id, updatedCustomer);
		}

		// Delete Customer()
		this.deleteCustomer = function(idNumDCU) {
			return $http.delete('http://localhost:8080/project/webapi/admin/customer/' + idNumDCU);
		}

	}

})();
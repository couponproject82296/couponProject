(function() {

	var module = angular.module("myApp");

	module.controller("CreateCouponCtrl", CreateCouponCtrlCtor);

	function CreateCouponCtrlCtor(mainCompanyService, javaBeansCtors, $scope,
			$state, $ngConfirm) {

		this.success = false;
		this.failure = false;
		this.newCoupon = {};
		
		
		
		//image to base64
		
		//click n browse
		
	    $scope.data = {}; //init variable

	    var fileSelect = document.createElement('input'); //input it's not displayed in html, will be  triggered it form other elements
	    fileSelect.type = 'file';


	    $scope.click = function() { //activate function to begin input file on click
	      fileSelect.click();
	    }

	    fileSelect.onchange = function() { //set callback to action after choosing file
	      var f = fileSelect.files[0],
	        r = new FileReader();

	      r.onloadend = function(e) { //callback after files finish loading
	        $scope.data.b64 = e.target.result;
	        $scope.img = e.target.result
	        $scope.$apply();
	        var content = e.target.result;
			sendFileToCloudVision(content.replace('data:image/jpeg;base64,', ''));    
        
	      }
	      r.readAsDataURL(f); //once defined all callbacks, begin reading the file
	    };
	    
	    
	    
	    
	    
		//drag N Drop

		  var dropzone = document.getElementById('dropzone');
  
		  dropzone.ondrop = function(e) {
			  e.preventDefault();
			  this.className = 'dropzone marginLeft1_5';
			     var f = e.dataTransfer.files[0],
			     r = new FileReader();
			      r.onloadend = function(e) { //callback after files finish loading
				  $scope.data.b64 = e.target.result;
				  $scope.img = e.target.result
				  $scope.$apply();
				  content = e.target.result;
				  sendFileToCloudVision(content.replace('data:image/jpeg;base64,', ''));     
			      }
			      r.readAsDataURL(f); //once defined all callbacks, begin reading the file     
		  };
		  
		  var content = {};

		  //Google cloud vision api:
		  var apiKey = 'AIzaSyDPVujbTdgVAbeUQf6gtEvsEFOWVQNf88Y'; //Api key

		  var CV_URL = 'https://vision.googleapis.com/v1/images:annotate?key=' + apiKey; //URL to approach vision api.	  
		  
		   //Default detection category 
		  var type = 'LABEL_DETECTION';
		  
          //Switch case for detection categories messages
		  
		  $scope.message = function (mytype) {
			  type = mytype;
			  switch(type) {
			    case "LANDMARK_DETECTION":
			        $scope.detectionMessage = "Detect popular natural and man-made structures within an image";
			        break;
			    case "TYPE_UNSPECIFIED":
			    	$scope.detectionMessage = "Detect Unspecified feature type.";
			        break;
			    case "FACE_DETECTION":
			    	$scope.detectionMessage = "Detect multiple faces within an image, along with the associated key" +
			    			" facial attributes like emotional state or wearing headwear. Facial Recognition is not supported.";
			        break;
			    case "LOGO_DETECTION":
			    	$scope.detectionMessage = "Detect popular product logos within an image.";
			        break;
			    case "LABEL_DETECTION":
			    	$scope.detectionMessage = "Detect broad sets of categories within an image," +
			    			" ranging from modes of transportation to animals.";
			        break;
			    case "TEXT_DETECTION":
			    	$scope.detectionMessage = "Detect and extract text within an image, with support " +
			    			"for a broad range of languages, along with support for automatic language identification.";
			        break;
			    case "SAFE_SEARCH_DETECTION":
			    	$scope.detectionMessage = "Detect explicit content like adult content or violent content within an image.";
			        break;
			    case "IMAGE_PROPERTIES":
			    	$scope.detectionMessage = "Detect general attributes" +
			    			" of the image, such as dominant colors and appropriate crop hints.";
			        break;
			    default:
			    	$scope.detectionMessage = "";
			}			  
		  }
  
		  
		  function sendFileToCloudVision (content) {
			
			  // Strip out the file prefix when you convert to JSON.
			  var request = {
			    requests: [{
			      image: {
			        content: content
			      },
			      features: [{
			        type: type,
			        maxResults: 200
			      }]
			    }]
			  };

			  $('#results').text('Loading...');
			  $.post({
			    url: CV_URL,
			    data: JSON.stringify(request),
			    contentType: 'application/json'
			  }).fail(function (jqXHR, textStatus, errorThrown) {
			    $('#results').text('ERRORS: ' + textStatus + ' ' + errorThrown);
			  }).done(displayJSON);
			}
		  
          //Displays the results.
		  function displayJSON (data) {
		    var contents = JSON.stringify(data, null, 4);
		    $('#results').text(contents);
		    var evt = new Event('results-displayed');
		    evt.results = contents;
		    document.dispatchEvent(evt);
		  }
			  
		  //Changes the view of the drop box.
		  dropzone.ondragover = function () {
			  this.className = 'dropzone dragover marginLeft1_5';
			  return false
		  };
		  
		  dropzone.ondragleave = function () {
			  this.className = 'dropzone marginLeft1_5';
			  return false
		  };
		
		
        //Create coupon
		this.createCoupon = function() {
			this.newCoupon.image = $scope.data.b64; //saves the base 64 converted image to the coupon object image property.
			
			if(this.newCoupon == undefined || this.newCoupon.id == undefined
					|| this.newCoupon.title == undefined
					|| this.newCoupon.startDate == undefined
					|| this.newCoupon.endDate == undefined
					|| this.newCoupon.amount == undefined
					|| this.newCoupon.type == undefined
					|| this.newCoupon.message == undefined
					|| this.newCoupon.price == undefined
					|| this.newCoupon.image == undefined) {
				this.success = false;
				this.failure = true;
				return;
			}
			this.success = false;
			this.failure = false;
			var self = this;
			var promisePost = mainCompanyService.createCoupon(this.newCoupon);
			promisePost
					.then(
							function(resp) {

								$scope.newCouponId = resp.data;//Assigning the new coupon id returned from the function in order to display it in ngConfirm.
								$scope.newCouponImage = self.newCoupon.image;//Assigning it to the $scope to display it in ngConfirm.
								self.success = true;
								self.failure = false;
								//Angular confirm in order to display a message after creating a new company.
								$ngConfirm({
									autoClose : 'No|10000',
									theme : 'dark',
									animation : 'rotateYR',
									closeAnimation : 'scale',
									animationSpeed : 500,
									boxWidth : '30%',
									useBootstrap : false,
									title : 'Redirection!',
									content : 'The id of the new coupon is: <strong>{{newCouponId}}</strong>  <img data-ng-src="{{newCouponImage}}" width="120px" height="90px"/>.<br />Would you like to be transferred to see the new coupon under all the coupons?',
									scope : $scope,
									buttons : {
										Yes : {
											text : 'Yes',
											btnClass : 'btn-orange',
											action : function(button) {
												$state.go("readAllCoupons"); //After coupon created, choosing yes will redirect to user view all the coupons.
											}
										},
										No : function(button) {
											location.reload();
											$state.go("createCoupon");
										},
									}
								});

							}, function(err) {
								//Redirection if the user reached an internal page without performing a login.
								if (err.status == 401)
									$state.go("errorUnauthorized");
								self.error = err.data;
								self.success = false;
								self.failure = true;
							});

		}
        /*
         * As part of the rules determined in the system core about coupons start and
         * end date.start date will always be tomorrow's date(starting from the next business day).
         * And end date will not precede start date. 
         */
		this.tomorrow = new Date();
		this.tomorrow.setDate(this.tomorrow.getDate() + 1);

		this.checkErr = function(startDate, endDate) {
			this.errMessage = '';
			if (startDate == null) {
				this.errMessage = 'Please enter start date first and make sure start date precedes end date';
			}
			if (new Date(this.tomorrow) > new Date(endDate)) { //Will display a formatted date
				var monthNames = [ "January", "February", "March", "April",
						"May", "June", "July", "August", "September",
						"October", "November", "December" ];
				var day = this.tomorrow.getDate();
				var monthIndex = this.tomorrow.getMonth();
				var year = this.tomorrow.getFullYear();

				this.errMessage2 = 'End Date should be greater than tomorrow\'s date['
						+ day + ' ' + monthNames[monthIndex] + ' ' + year + ']';
			}

		}
		

		

	}
	



})();
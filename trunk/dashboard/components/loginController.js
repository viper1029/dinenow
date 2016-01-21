angular.module("app.security.login", [])
    .controller("LoginController", function($rootScope, $scope, $cookieStore, $http, $state, authResource) {

        $scope.user = {
            email: "admin@admin.com",
            password: "admin@123"
        },


            $scope.authInfo,
            $scope.userLoggedIn,

            $scope.logIn = function(info) {
                $scope.loginLoading = !0,
                    setTimeout(function() {

                        authResource.login(info).success(function(payload) {

                            console.log(payload.data);

                            if ($scope.loginLoading = !1, $scope.authInfo = payload.data, $scope.authInfo) {
                                $cookieStore.put("authToken", $scope.authInfo.access_token);
                                $http.defaults.headers.common.Authorization = "Bearer " + $cookieStore.get("authToken");
                                //   $http.defaults.headers.common.Authentication = 'Basic '+ $cookieStore.get("authToken");
                                console.log("ccccc"+$scope.userLoggedIn+":::::::::"+$scope.authInfo.role);
                                console.log($scope.authInfo);
                                if($scope.authInfo.role == "OWNER"){
                                    $cookieStore.put("authRestaurantID", $scope.authInfo.restaurant.id);
                                    $rootScope.restaurantID = $scope.authInfo.restaurant.id;
                                    var restaurantLocation = JSON.stringify($scope.authInfo.restaurant.location);
                                    $rootScope.globalRestaurantLocation = $scope.authInfo.restaurant.location;
                                }

                                $cookieStore.put("authUsername", $scope.authInfo.user.firstName);
                                $rootScope.globalUser = $scope.authInfo.user.firstName;
                                $cookieStore.put("authUserID", $scope.authInfo.user.id);
                                $cookieStore.put("customerStripe", $scope.authInfo.user.customerStripe);

                                $cookieStore.put("restaurantLocation", restaurantLocation);


                                $scope.userLoggedIn = payload.data,
                                    $state.go( ($scope.userLoggedIn.role == "OWNER" )? "app.owner.orders" : "app.admin.restaurant.list");
                            } else alertify.error("Oops! Username or Password is incorrect")
                        }).error(function() {
                            alertify.error("Oops! Something went wrong. Can not connect to server, please try again."), $scope.loginLoading = !1
                        })
                    }, 500)
            }
    });
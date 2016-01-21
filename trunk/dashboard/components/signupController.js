angular.module("app.security.signup", []).controller("SignupController", ["$scope", "$cookieStore", "$http", "$state", "authResource", function($scope, $cookieStore, $http, $state, authResource) {

    $scope.user = {}, $scope.signUp = function(info) {
        authResource.signup(info).success(function() {
            $state.go("app.security.login", {}, {
                reload: !0
            }), alertify.success("Sign Up Successfully")
        }).error(function() {
            alertify.error("Oops! Please try again")
        })
    }
}]);
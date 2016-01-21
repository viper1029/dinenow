
angular.module("app.admin.user.manage", [])
    .controller("UserManageController", function($scope, $state, usersResource) {
        $scope.init = function() {
            var itemId = $state.params.userId;
            usersResource.getById(itemId).success(function(payload) {
                console.log(payload);
                $scope.user = payload.data.restaurantuser;
            }).error(function() {
                alertify.error("Oops! Please try again")
            })
        }, $scope.init(),
            $scope.save = function() {
                $state.params.userId;
                console.log($scope.user);
                usersResource.addOrUpdate($scope.user).success(function() {
                    alertify.success("Updated Successfully"), $state.go("app.admin.user.list")
                }).error(function() {
                    alertify.error("Oops! Please try again")
                })
            }
    });
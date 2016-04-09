angular.module("app.owner.menu", [
    "app.owner.menu.service",
    "app.owner.menu.categories",
    "app.owner.menu.items",
    "app.owner.menu.menus",
    "app.owner.menu.modifiers",
    "app.owner.menu.sizes",
    "app.owner.menu.addon"])
    .controller("MenuController", ["$scope", "$state", function($scope, $state) {
        $scope.$state = $state
    }]);

angular.module("app.owner.restaurant.notifications", [])
    .controller("NotificationsController", ["$scope", function() {}]);


angular.module("app.owner", ["app.owner.menu", "app.owner.restaurant", "app.owner.orders"])
    .controller("OwnerController", function($rootScope, $scope, $state, $cookieStore, globalHandleUI) {
        $scope.init = function() {
            $scope.isOnline = !0, $scope.$state = $state,
                $scope.username = $rootScope.globalUser
        }, $scope.init(), globalHandleUI.handleShowHideInfoUserBox(),
            $scope.logOut = function() {
                $cookieStore.remove("authToken"), $cookieStore.remove("authRestaurantID"),
                    $cookieStore.remove("authUsername"), $cookieStore.remove("authUserID"),
                    $cookieStore.remove("customerStripe"), setTimeout(function() {
                    $state.go("app.security.login", {}, {
                        reload: !0
                    })
                }, 300)
            }, $scope.changeStatus = function() {
            $scope.isOnline = !$scope.isOnline
        }
    });

angular.module("app.admin", ["app.admin.orders", "app.admin.restaurant", "app.admin.user", "app.admin.plan"])
    .controller("AdminController", function($rootScope, $scope, $state, $cookieStore, globalHandleUI){
        $scope.init = function() {
            $scope.$state = $state, $scope.username = $rootScope.globalUser
        }, $scope.init(), globalHandleUI.handleShowHideInfoUserBox(),
            $scope.logOut = function() {
                $cookieStore.remove("authToken"),
                    $state.go("app.security.login", {}, {
                        reload: !0
                    })
            }
    });




angular.module("app.admin.user", ["app.admin.user.services", "app.admin.user.manage"])
    .controller("UserController", ["$scope", "$modal", "$state", "$filter", "ngTableParams",
        "usersResource", "restaurantsResource", function($scope, $modal, $state, $filter,
                                                         ngTableParams, usersResource, restaurantsResource) {
            $scope.init = function() {
                console.log(1);
                usersResource.get().success(function(payload) {
                    console.log(payload);
                    $scope.users = payload.data.restaurantusers,
                        console.log($scope.users);
                    $scope.initNgTable()
                }).error(function() {
                    alertify.error("Oops! Please try again")
                }),

                    $scope.initNgTable = function() {
                        $scope.tableParams = new ngTableParams({
                            page: 1,
                            count: 10,
                            sorting: {
                                userFirstName: "asc"
                            }
                        }, {
                            total: $scope.users.length,
                            getData: function($defer, params) {
                                var orderedData = params.sorting() ? $filter("orderBy")
                                ($scope.users, params.orderBy()) : $scope.users;
                                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                            }
                        })
                    }
            },

                $scope.init(),

                $scope.open = function(item) {
                    $scope.list=[];
                    var modalInstance = $modal.open({
                            templateUrl: "scripts/admin/user/views/modal/user.html",
                            resolve: {
                                itemToProcess: function() {
                                    return item
                                }
                            },
                            controller: function($scope, $modalInstance, itemToProcess, usersResource, restaurantsResource) {
                                $scope.itemToProcess = angular.copy(itemToProcess) || {},
                                    setTimeout(function() {
                                        restaurantsResource.get().success(function(payload) {
                                            $scope.restaurants = payload.data.restaurants
                                        })
                                    }, 200)
                                $scope.init = function() {
                                    $scope.itemToProcess.firstName = '',
                                    $scope.itemToProcess.lastName-'',
                                        $scope.itemToProcess.password = "",
                                        $scope.itemToProcess.role = "OWNER",
                                        $scope.email = /^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z.]{2,5}$/,
                                        $scope.itemToProcess.restaurantId = ""
                                },
                                    $scope.init(),
                                    $scope.save = function() {
                                        usersResource.addOrUpdate($scope.itemToProcess).success(function(res) {
                                            console.log(res);
                                            $modalInstance.close($scope.itemToProcess);
                                        }).error(function(err) {
                                            console.log(err);
                                            alertify.error("Oops! Please try again")
                                        })
                                    }, $scope.cancel = function() {
                                    $modalInstance.dismiss("cancel")
                                }
                            }
                        }
                    );
                    modalInstance.result.then(function(data) {
                        data && (alertify.success("Successfully"), $state.go($state.current, {}, {
                            reload: !0
                        }))
                    })
                }, $scope.manage = function(item) {
                $state.go("app.admin.user.manage", {
                    userId: item.id
                })
            }
        }]);
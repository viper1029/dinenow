angular.module("app.admin.restaurant.manage.menu.modifiers", [])
    .controller("AdminModifiersController", function($scope, $state, $stateParams, $modal, adminModifiersResource, ngTableConfig, adminItemsResource, adminAddonResource, adminSizesResource) {
        $scope.init = function() {
            var restaurantID = $stateParams.restaurantId;
            adminModifiersResource.get(restaurantID).success(function(payload) {
                $scope.modifires = payload.data.modifiers,
                    $scope.tableParams = ngTableConfig.config($scope.modifires)
            }), setTimeout(function() {
                adminAddonResource.get(restaurantID).success(function(payload) {
                    $scope.addons = payload.data.addons
                })
            }, 100), setTimeout(function() {
                adminSizesResource.get(restaurantID).success(function(payload) {
                    $scope.sizes = payload.data.sizes
                })
            }, 200), setTimeout(function() {
                adminItemsResource.get(restaurantID).success(function(payload) {
                    $scope.items = payload.data.items
                })
            }, 300)
        }, $scope.init(),
            $scope.open = function(item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/modifiers.html",
                    resolve: {
                        itemToProcess: function() {
                            return item
                        },
                        scope: function() {
                            return $scope
                        }
                    },
                    size: "lg",
                    controller: function($scope, $modalInstance, adminModifiersResource, itemToProcess, scope) {
                        $scope.init = function() {
                            $scope.addons = scope.addons,
                                $scope.sizes = scope.sizes,
                                $scope.items = scope.items,
                                console.log($scope.addons);
                            console.log($scope.items);

                            console.log($scope.sizes);

                            $scope.itemToProcess = angular.copy(itemToProcess) || {},
                                $scope.itemToProcess.id ? setTimeout(function() {
                                    adminModifiersResource.getByID($scope.itemToProcess).success(function(payload) {
                                        $scope.itemToProcess = payload.data.modifier;
                                        var addonDefault = {
                                            price: 0,
                                            availabilityStatus: "AVAILABLE",
                                            isDefault: !1
                                        };
                                        $scope.itemToProcess.addOns.push(addonDefault)
                                        /* $scope.itemToProcess.itemSizes.push({})*/
                                    })
                                }, 500) :
                                    $scope.itemToProcess = {
                                        isSelectMultiple: !1,
                                        minSelection: 0,
                                        addOns: [{
                                            price: 0,
                                            availabilityStatus: "AVAILABLE",
                                            isDefault: !1
                                        }]/*,
                                         itemSizes: [{}]*/
                                    }
                            console.log($scope.itemToProcess);
                        }, $scope.init(),
                            $scope.addItems = function() {
                                var itemDefault = {
                                    price: 0,
                                    availabilityStatus: "AVAILABLE",
                                    isDefault: !1
                                };
                                $scope.itemToProcess.addOns.push(itemDefault)
                            }, /*$scope.addAppliesTo = function() {
                         $scope.itemToProcess.itemSizes.push({})
                         },*/ $scope.deleteItem = function(index) {
                            $scope.itemToProcess.addOns.splice(index, 1)
                        }, $scope.deleteAppliesTo = function(index) {
                            $scope.itemToProcess.itemSizes.splice(index, 1)
                        }, $scope.save = function() {
                            $scope.loadingSave = !0, $scope.itemToProcess.restaurantId = $stateParams.restaurantId || "", $scope.itemToProcess.addOns.length > 1  ? ($scope.itemToProcess.addOns.splice(-1, 1), $scope.itemToProcess.addOns.length > 0  && adminModifiersResource.addOrUpdate($scope.itemToProcess).success(function() {
                                $scope.loadingSave = !1, $modalInstance.close($scope.itemToProcess)
                            }).error(function() {
                                alertify.error("Oops! Please try again")
                            })) : alertify.error("Please add size and price")
                        }, $scope.cancel = function() {
                            $modalInstance.dismiss("cancel")
                        }
                    }
                });
                modalInstance.result.then(function(data) {
                    alertify.success(data.id ? "Updated Successfully!" : "Created Successfully!"), $state.go($state.current, {}, {
                        reload: !0
                    })
                })
            }, $scope["delete"] = function(item) {
            alertify.confirm("Are you sure you want to delete?", function(e) {
                e && adminModifiersResource["delete"](item).success(function() {
                    alertify.success("Delete Successfully")
                }).error(function() {
                    alertify.error("Oops! Please try again")
                })
            })
        }
    });
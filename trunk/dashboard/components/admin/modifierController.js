angular.module("app.admin.restaurant.manage.menu.modifiers", [])
    .controller("AdminModifiersController", function ($scope, $state, $stateParams,
                                                      $modal, adminModifiersResource, ngTableConfig, adminItemsResource,
                                                      adminAddonResource, adminSizesResource) {

        var initModifiers = function () {
            var restaurantID = $stateParams.restaurantId;
            adminModifiersResource.get(restaurantID).then(function (payload) {
                $scope.modifires = payload.data.data.modifiers;
                $scope.tableParams = ngTableConfig.config($scope.modifires);
            });
        };

        var open = function (item) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/modifiers.html",
                resolve: {
                    itemToProcess: function () {
                        return item
                    }
                },
                controller: function ($scope, $modalInstance, adminModifiersResource, itemToProcess) {
                    var init = function () {

                        $scope.itemToProcess = angular.copy(itemToProcess) || {};
                        var restaurantID = $stateParams.restaurantId;

                        var loadAddon = function () {
                            adminAddonResource.getAll(restaurantID).success(function (payload) {
                                $scope.addons = payload.data.addons
                                loadAddonDetails();
                            });
                        };

                        var loadAddonDetails = function () {
                            $scope.itemToProcess.id ?
                                adminModifiersResource.getByID($scope.itemToProcess).success(function (payload) {
                                    $scope.itemToProcess = payload.data.modifier;
                                    $scope.itemToProcess.modifierAddon.push({
                                        price: 0,
                                        isDefault: "false",
                                        addon: {
                                            id: undefined
                                        }
                                    })
                                }) :
                                $scope.itemToProcess = {
                                    isMultipleSelection: !1,
                                    minSelection: undefined,
                                    maxSelection: undefined,
                                    modifierAddon: [{
                                        price: 0,
                                        isDefault: "false",
                                        addon: {
                                            id: undefined
                                        }
                                    }]
                                };
                        };

                        loadAddon();
                    };

                    init();

                    $scope.addModifierAddon = function () {
                        var itemDefault = {
                            name: "",
                            price: 0,
                            isDefault: !1
                        };
                        $scope.itemToProcess.modifierAddon.push(itemDefault)
                    };

                    $scope.deleteModifierAddon = function (index) {
                        $scope.itemToProcess.modifierAddon.splice(index, 1)
                    };

                    $scope.save = function () {
                        $scope.loadingSave = !0;
                        $scope.itemToProcess.restaurantId = $stateParams.restaurantId || "";

                        var splice = $scope.itemToProcess.modifierAddon.length > 1 &&
                            ($scope.itemToProcess.modifierAddon[$scope.itemToProcess.modifierAddon.length - 1].addon === undefined ||
                            $scope.itemToProcess.modifierAddon[$scope.itemToProcess.modifierAddon.length - 1].addon.id === null ||
                            $scope.itemToProcess.modifierAddon[$scope.itemToProcess.modifierAddon.length - 1].addon.id === undefined);

                        if (splice) {
                            $scope.itemToProcess.modifierAddon.splice(-1, 1);
                        }
                        if ($scope.itemToProcess.modifierAddon.length > 0 && ($scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId || ""))) {
                            adminModifiersResource.addOrUpdate($scope.itemToProcess).success(function () {
                                $scope.loadingSave = !1;
                                $modalInstance.close($scope.itemToProcess)
                            }).error(function () {
                                alertify.error("Oops! Please try again")
                            })
                        }
                        else {
                            alertify.error("Please add size and price")
                        }

                    };

                    $scope.cancel = function () {
                        $modalInstance.dismiss("cancel")
                    }
                }
            });
            modalInstance.result.then(function (data) {
                alertify.success(data.id ? "Updated Successfully!" : "Created Successfully!"), $state.go($state.current, {}, {
                    reload: !0
                })
            })
        };

        var deleteModifier = function (modifier) {
            alertify.confirm("Are you sure you want to delete?", function (e) {
                e && adminModifiersResource["delete"](modifier).success(function () {
                    alertify.success("Delete Successfully")
                }).error(function () {
                    alertify.error("Oops! Please try again")
                })
            })
        };

        $scope.uncheck = function (event) {
            if ($scope.item.isDefault == event.target.value)
                $scope.item.isDefault = false
        };

        initModifiers();
        $scope.open = open;
        $scope.deleteModifier = deleteModifier;
    });
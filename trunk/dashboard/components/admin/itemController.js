angular.module("app.admin.restaurant.manage.menu.items", ["flow"])
    .controller("AdminItemsController",
        function ($scope, $filter, $modal, $state,
                  $stateParams, ngTableConfig, adminItemsResource, adminSizesResource, adminAddonResource,
                  adminModifiersResource) {

            var initItems = function () {
                var restaurantID = $stateParams.restaurantId;
                adminItemsResource.get(restaurantID).success(function (payload) {
                    $scope.items = payload.data.items;
                    $scope.tableParams = ngTableConfig.config($scope.items)
                });

                var loadSizes = function () {
                    adminSizesResource.get(restaurantID).success(function (payload) {
                        $scope.listSizes = payload.data.sizes
                    })
                };

                var loadAddons = function () {
                    adminAddonResource.get(restaurantID).success(function (payload) {
                        $scope.listAddons = payload.data.addons
                    })
                };

                var loadModifiers = function () {
                    adminModifiersResource.get(restaurantID).success(function (payload) {
                        $scope.listModifier = payload.data.modifiers
                    })
                };
            };

            var open = function (item) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/admin/restaurant/manage/menu/views/modal/items.html",
                    resolve: {
                        itemToProcess: function () {
                            return item
                        },
                        listSizes: function () {
                            return $scope.listSizes
                        },
                        listAddons: function () {
                            return $scope.listAddons
                        },
                        listModifier: function () {
                            return $scope.listModifier
                        },
                        listItems: function () {
                            return $scope.items
                        }
                    },
                    controller: function ($scope, $modalInstance, adminItemsResource, itemToProcess, listSizes, listAddons, listModifier, listItems, API_IMAGE_UPLOAD) {
                        var init = function () {
                            $scope.tagOptions = {
                                multiple: !0,
                                simple_tags: !0,
                                tags: ["BBQ", "Asia", "Vegetarian", "Western"]
                            },
                                $scope.apiImageUpload = API_IMAGE_UPLOAD,
                                console.log($scope.apiImageUpload),
                                $scope.onShow = {
                                    size: !1,
                                    modifier: !1
                                },
                                $scope.spiceLevel = [1, 2, 3, 4, 5],
                                $scope.orderType = ["All", "Pickup", "Delivery", "Dine In"],
                                $scope.itemToProcess = angular.copy(itemToProcess) || {},
                                $scope.listSizes = angular.copy(listSizes) || {},
                                $scope.listModifier = angular.copy(listModifier) || {},
                                $scope.listAddons = angular.copy(listAddons) || {},
                                $scope.listItems = listItems,
                                $scope.itemToProcess.id ? setTimeout(function () {
                                    adminItemsResource.getByID($scope.itemToProcess).success(function (payload) {
                                        console.log(payload),
                                            $scope.itemToProcess = payload.data.items,
                                            $scope.itemToProcess.sizePrices.push({
                                                size: "",
                                                price: 0
                                            }), $scope.itemToProcess.modifiers.push({
                                            modifier: ""
                                        })
                                    })
                                }, 200) : $scope.itemToProcess = {
                                    availabilityStatus: "AVAILABLE",
                                    spiceLevel: 0,
                                    linkImage: "empty",
                                    price: 0,
                                    sizePrices: [{
                                        size: "",
                                        price: 0
                                    }],
                                    modifiers: [{
                                        modifier: ""
                                    }],
                                    isVegeterian: null
                                }
                        };
                        $scope.init(),
                            $scope.getLinkImage = function (message) {
                                var response = JSON.parse(message);
                                $scope.itemToProcess.linkImage = response.data.link
                            }, $scope.addSizePrice = function () {
                            $scope.itemToProcess.sizePrices.push({
                                size: "",
                                price: 0
                            })
                        }, $scope.addModifier = function () {
                            $scope.itemToProcess.modifiers.push({
                                modifier: ""
                            })
                        }, $scope.createNewModifer = function () {
                            var modalInstance = $modal.open({
                                templateUrl: "scripts/owner/menu/views/modal/modifiers.html",
                                resolve: {
                                    scope: function () {
                                        return $scope
                                    },
                                    listSizes: function () {
                                        return $scope.listSizes
                                    },
                                    listAddons: function () {
                                        return $scope.listAddons
                                    },
                                    listItems: function () {
                                        return $scope.listItems
                                    }
                                },
                                size: "lg",
                                controller: function ($scope, $modalInstance, modifiersResource, scope, listSizes, listAddons, listItems) {
                                    $scope.init = function () {
                                        $scope.addons = angular.copy(listAddons) || {},
                                            $scope.sizes = angular.copy(listSizes) || {},
                                            $scope.items = angular.copy(listItems) || {}, $scope.itemToProcess = {
                                            isSelectMultiple: !1,
                                            minSelection: 0,
                                            addons: [{
                                                price: 0,
                                                availabilityStatus: "AVAILABLE"
                                            }],
                                            items: [{}]
                                        }
                                    }, $scope.init(), $scope.addItems = function () {
                                        var itemDefault = {
                                            price: 0,
                                            availabilityStatus: "AVAILABLE"
                                        };
                                        $scope.itemToProcess.addons.push(itemDefault)
                                    }, $scope.addAppliesTo = function () {
                                        $scope.itemToProcess.items.push({})
                                    }, $scope.deleteItem = function (index) {
                                        $scope.itemToProcess.addons.splice(index, 1)
                                    }, $scope.deleteAppliesTo = function (index) {
                                        $scope.itemToProcess.items.splice(index, 1)
                                    }, $scope.save = function () {
                                        $scope.itemToProcess.addons.length > 1 && $scope.itemToProcess.items.length > 1 ? ($scope.itemToProcess.addons.splice(-1, 1), $scope.itemToProcess.items.splice(-1, 1), $scope.itemToProcess.addons.length > 0 && $scope.itemToProcess.items.length > 0 && modifiersResource.addOrUpdate($scope.itemToProcess).success(function () {
                                            $modalInstance.close($scope.itemToProcess)
                                        }).error(function () {
                                            alertify.error("Oops! Please try again")
                                        })) : alertify.error("Please add size and price")
                                    }, $scope.cancel = function () {
                                        $modalInstance.dismiss("cancel")
                                    }
                                }
                            });
                            modalInstance.result.then(function (data) {
                                var isMatchId = !1;
                                if (data) {
                                    for (var i = 0; i < $scope.modifiers.length; i++)
                                        if ($scope.modifiers[i].id == data.id) {
                                            isMatchId = !0, $scope.modifiers[i] = data, alertify.success("Updated Successfully");
                                            break
                                        }
                                    isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
                                        reload: !0
                                    }))
                                }
                            })
                        }, $scope.save = function () {
                            $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $stateParams.restaurantId || ""), adminItemsResource.addOrUpdate($scope.itemToProcess).success(function () {
                                $modalInstance.close($scope.itemToProcess)
                            }).error(function () {
                                alertify.error("Oops! Please try again")
                            })
                        }, $scope.cancel = function () {
                            $modalInstance.dismiss("cancel")
                        }
                    }
                });
                modalInstance.result.then(function (data) {
                    var isMatchId = !1;
                    if (data) {
                        for (var i = 0; i < $scope.items.length; i++)
                            if ($scope.items[i].id == data.id) {
                                isMatchId = !0, $scope.items[i] = data, alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                                    reload: !0
                                });
                                break
                            }
                        isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
                            reload: !0
                        }))
                    }
                })
            };

            $scope.pause = function (item) {
                item.pauseLoading = !0, item.availabilityStatus = "AVAILABLE" == item.availabilityStatus ? "PAUSED" : "AVAILABLE", setTimeout(function () {
                    adminItemsResource.addOrUpdate(item).success(function () {
                        item.pauseLoading = !1
                    }).error(function () {
                    })
                }, 800)
            };

            var deleteItem = function (item) {
                alertify.confirm("Are you sure you want to delete?", function (e) {
                    e && adminItemsResource["delete"](item).success(function () {
                        $state.go($state.current, {}, {
                            reload: !0
                        });
                        alertify.success("Deleted successfully")
                    }).error(function () {
                        alertify.error("Oops! This item was used on Sub-Menu or Menu. Please check it again before deleting.")
                    })
                })
            };

            initItems();
            $scope.open = open;
            $scope.deleteAddon = deleteAddon;
        });
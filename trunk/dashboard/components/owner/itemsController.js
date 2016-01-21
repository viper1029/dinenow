angular.module("app.owner.menu.items", ["flow", "app.owner.menu.modifiers"])
    .controller("ItemsController", function($rootScope, $scope, $filter, $modal, $state, ngTableConfig, itemsResource, sizesResource, addonResource, modifiersResource, API_IMAGE_UPLOAD) {
        $scope.initList = function() {
            itemsResource.get().success(function(payload) {
                $scope.items = payload.data.items, $scope.tableParams = ngTableConfig.config($scope.items)
            }), setTimeout(function() {
                sizesResource.get().success(function(payload) {
                    $scope.listSizes = payload.data.sizes
                })
            }, 100), setTimeout(function() {
                addonResource.get().success(function(payload) {
                    $scope.listAddons = payload.data.addons
                })
            }, 250), setTimeout(function() {
                modifiersResource.get().success(function(payload) {
                    $scope.listModifier = payload.data.modifiers
                })
            }, 300)
        }, $scope.initList(), $scope.open = function(item) {
            var modalInstance = $modal.open({
                templateUrl: "scripts/owner/menu/views/modal/items.html",
                resolve: {
                    itemToProcess: function() {
                        return item
                    },
                    listSizes: function() {
                        return $scope.listSizes
                    },
                    listAddons: function() {
                        return $scope.listAddons
                    },
                    listModifier: function() {
                        return $scope.listModifier
                    },
                    listItems: function() {
                        return $scope.items
                    }
                },
                size: "lg",
                controller: function($scope, $modalInstance, itemsResource, itemToProcess, listSizes, listAddons, listModifier, listItems, API_IMAGE_UPLOAD) {
                    $scope.init = function() {
                        $scope.initFlowConfig = new Flow({
                            target: API_IMAGE_UPLOAD + "upload",
                            singleFile: !0
                        }), window.flowConfig = $scope.initFlowConfig, $scope.tagOptions = {
                            multiple: !0,
                            simple_tags: !0,
                            tags: ["Asia", "Western", "India", "Japan"]
                        }, $scope.apiImageUpload = API_IMAGE_UPLOAD,
                            $scope.onShow = {
                                size: !1,
                                modifier: !1
                            }, $scope.spiceLevel = [1, 2, 3, 4, 5],
                            $scope.orderType = ["All", "Pickup", "Delivery", "Dine In"],
                            $scope.itemToProcess = angular.copy(itemToProcess) || {},
                            $scope.listSizes = angular.copy(listSizes) || {},
                            $scope.listModifier = angular.copy(listModifier) || {},
                            $scope.listAddons = angular.copy(listAddons) || {},
                            $scope.listItems = listItems, $scope.itemToProcess.id ? setTimeout(function() {
                            itemsResource.getByID($scope.itemToProcess).success(function(payload) {
                                $scope.itemToProcess = payload.data.items, $scope.itemToProcess.sizePrices.push({
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
                    },
                        $scope.init(),
                        $scope.getLinkImage = function(message) {
                            console.log(message);
                            var response = JSON.parse(message);
                            $scope.itemToProcess.linkImage = response.data.link
                        },
                        $scope.addSizePrice = function() {
                            $scope.itemToProcess.sizePrices.push({
                                size: "",
                                price: 0
                            })
                        },
                        $scope.addModifier = function() {
                            $scope.itemToProcess.modifiers.push({
                                modifier: ""
                            })
                        },
                        $scope.reloadModifer = function() {
                            setTimeout(function() {
                                modifiersResource.get().success(function(payload) {
                                    $scope.listModifier = payload.data.modifiers
                                })
                            }, 200)
                        },
                        $scope.createNewModifer = function() {
                            var modalInstance = $modal.open({
                                templateUrl: "scripts/owner/menu/views/modal/modifiers.html",
                                resolve: {
                                    scope: function() {
                                        return $scope
                                    }
                                },
                                size: "lg",
                                controller: function($scope, $modalInstance, modifiersResource, scope) {
                                    $scope.init = function() {
                                        $scope.addons = scope.listAddons,
                                            $scope.sizes = scope.listSizes,
                                            $scope.items = scope.listItems,
                                            $scope.itemToProcess = {
                                                isSelectMultiple: !1,
                                                minSelection: 0,
                                                addOns: [{
                                                    price: 0,
                                                    availabilityStatus: "AVAILABLE",
                                                    isDefault: !1
                                                }],
                                                itemSizes: [{}]
                                            },
                                            $scope.itemToProcess.maxSelection = $scope.itemToProcess.minSelection
                                    }, $scope.init(), $scope.addItems = function() {
                                        var itemDefault = {
                                            price: 0,
                                            availabilityStatus: "AVAILABLE",
                                            isDefault: !1
                                        };
                                        $scope.itemToProcess.addOns.push(itemDefault)
                                    }, $scope.addAppliesTo = function() {
                                        $scope.itemToProcess.itemSizes.push({})
                                    }, $scope.deleteItem = function(index) {
                                        $scope.itemToProcess.addOns.splice(index, 1)
                                    }, $scope.deleteAppliesTo = function(index) {
                                        $scope.itemToProcess.itemSizes.splice(index, 1)
                                    }, $scope.save = function() {
                                        $scope.loadingSave = !0,
                                            $scope.itemToProcess.restaurantId = $rootScope.restaurantID || "",
                                        $scope.itemToProcess.addOns.length > 1 && $scope.itemToProcess.itemSizes.length > 1 && ($scope.itemToProcess.addOns.splice(-1, 1), $scope.itemToProcess.itemSizes.splice(-1, 1), $scope.itemToProcess.addOns.length > 0 && $scope.itemToProcess.itemSizes.length > 0 && modifiersResource.addOrUpdate($scope.itemToProcess).success(function() {
                                            $scope.loadingSave = !1, scope.reloadModifer(), $modalInstance.close($scope.itemToProcess)
                                        }).error(function() {
                                            alertify.error("Oops! Please try again")
                                        }))
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
                        },
                        $scope.itemSave = function() {
                            $scope.loadingSave = !0, $scope.itemToProcess.id || ($scope.itemToProcess.restaurantId = $rootScope.restaurantID || ""),
                                itemsResource.addOrUpdate($scope.itemToProcess).success(function() {
                                    $scope.loadingSave = !1, $modalInstance.close($scope.itemToProcess)
                                }).error(function() {
                                    alertify.error("Oops! Please try again")
                                })
                        },
                        $scope.cancel = function() {
                            $modalInstance.dismiss("cancel")
                        }
                }
            });
            modalInstance.result.then(function(data) {
                var isMatchId = !1;
                if (data) {
                    for (var i = 0; i < $scope.items.length; i++)
                        if ($scope.items[i].id == data.id) {
                            isMatchId = !0, $scope.items[i] = data,
                                alertify.success("Updated Successfully"), $state.go($state.current, {}, {
                                reload: !0
                            });
                            break
                        }
                    isMatchId || (alertify.success("Created Successfully"), $state.go($state.current, {}, {
                        reload: !0
                    }))
                }
            })
        },
            $scope.pause = function(item) {
                item.pauseLoading = !0, item.availabilityStatus = "AVAILABLE" == item.availabilityStatus ? "PAUSED" : "AVAILABLE", setTimeout(function() {
                    itemsResource.addOrUpdate(item).success(function() {
                        item.pauseLoading = !1,
                            alertify.success("AVAILABLE" == item.availabilityStatus ? "Item <i>'" + item.itemName + "' </i> is STARTED!" : "Item <i>'" + item.itemName + "' </i> is PAUSED!")
                    }).error(function() {})
                }, 800)
            },
            $scope["delete"] = function(item) {
                alertify.confirm("Are you sure you want to delete?", function(e) {
                    e && itemsResource["delete"](item).success(function() {
                        $state.go($state.current, {}, {
                            reload: !0
                        }), alertify.success("Deleted successfully")
                    }).error(function() {
                        alertify.error("Oops! This item was used on Sub-Menu or Menu. Please check it again before deleting.")
                    })
                })
            }
    });
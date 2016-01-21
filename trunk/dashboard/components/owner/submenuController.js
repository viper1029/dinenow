angular.module("app.owner.menu.submenu", [])
    .controller("SubmenuController", ["$rootScope", "$scope", "$filter", "$state", "$modal", "ngTableConfig", "categoriesResource", "itemsResource", "submenusResource", function($rootScope, $scope, $filter, $state, $modal, ngTableConfig, categoriesResource, itemsResource, submenusResource) {
        /*$scope.itemsTree = [], $scope.categoriesTree = [], $scope.subMenuList = [],*/
        $scope.submenu = {
            name: "",
            description: "",
            categories: []
        }, $scope.templeTree = {
            categories: [],
            items: []
        }, $scope.getSubMenuList = function() {
            submenusResource.get().success(function(payload) {
                $scope.subMenuList = payload.data.submenus, $scope.tableParams = ngTableConfig.config($scope.subMenuList)
            }).error(function() {})
        }, $scope.getCategoryList = function() {
            categoriesResource.get().success(function(payload) {
                $scope.categoriesTree = payload.data.categories, $scope.templeTree.categories = angular.copy(payload.data)
            }).error(function() {})
        }, $scope.getItemList = function() {
            itemsResource.get().success(function(payload) {
                $scope.itemsTree = payload.data.items, $scope.templeTree.items = angular.copy(payload.data)
            }).error(function() {})
        }, $scope.initUITreeOptions = function() {
            $scope.subMenuOptions = {
                accept: function(sourceNodeScope, destNodesScope) {
                    var sourceValue = sourceNodeScope.$modelValue,
                        desElement = (destNodesScope.$modelValue, sourceNodeScope.$element, destNodesScope.$element),
                        isCategory = sourceValue.name,
                        isExistCategories = $scope.submenu.categories.length > 0,
                        widthParentTree = angular.element("#submenu-area").width(),
                        isDragInChildNode = desElement.width() != widthParentTree;
                    return isCategory && !isDragInChildNode ? !0 : isCategory ? !1 : isExistCategories && isDragInChildNode ? !0 : void 0
                }
            }, $scope.categoryOptions = {
                accept: function() {
                    return !1
                }
            }, $scope.itemOptions = {
                accept: function() {
                    return !1
                }
            }
        }, $scope.init = function() {
            $scope.getSubMenuList(), setTimeout(function() {
                $scope.getCategoryList()
            }, 100), setTimeout(function() {
                $scope.getItemList()
            }, 200), $scope.initNgTable = function(data) {
                $scope.tableParams = new ngTableParams({
                    page: TABLE_OPTIONS.page,
                    count: TABLE_OPTIONS.count
                }, {
                    total: data.length,
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ? $filter("orderBy")(data, params.orderBy()) : data;
                        $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                    }
                })
            }, $scope.addTipText = function() {
                var emptyTree = document.getElementsByClassName("angular-ui-tree-empty");
                if (emptyTree) {
                    var p = document.createElement("p"),
                        textNode = document.createTextNode("Drag and Drop from Categories and Items to here");
                    p.appendChild(textNode), emptyTree[0].appendChild(p), emptyTree[0].className += " empty-tree"
                }
            }
        }, $scope.init(), $scope.initUITreeOptions(), $scope.getActiveItem = function(item) {
            return item.id == $scope.submenu.id ? "item-active" : void 0
        }, $scope.viewDetail = function(item) {
            submenusResource.getByID(item).success(function(payload) {
                $scope.submenu = payload.data.submenus
            }).error(function() {
                alertify.error("Oop! Please try again")
            })
        }, $scope.deleteCategory = function(node, value) {
            if (value.name) {
                var modalInstance = $modal.open({
                    templateUrl: "scripts/owner/menu/views/modal/submenu-confirm.html",
                    backdrop: !1,
                    resolve: {
                        node: function() {
                            return node
                        },
                        value: function() {
                            return value
                        }
                    },
                    controller: function($scope, $modalInstance, node, value) {
                        $scope.itemToProcess = angular.copy(value), $scope["delete"] = function() {
                            node.remove(), alertify.success("Deleted successfully"), $modalInstance.close($scope.itemToProcess)
                        }, $scope.cancel = function() {
                            $modalInstance.dismiss("cancel")
                        }
                    }
                });
                modalInstance.result.then(function(data) {
                    if (data) {
                        var isMatchId = !1;
                        if (data.items.length > 0)
                            for (var i = 0; i < data.items.length; i++) {
                                for (var j = 0; j < $scope.itemsTree.length; j++)
                                    if ($scope.itemsTree[j].id == data.items[i].id) {
                                        isMatchId = !0;
                                        break
                                    }
                                isMatchId || $scope.itemsTree.push(data.items[i])
                            }
                        $scope.tempCategory = angular.copy(data), delete $scope.tempCategory.items;
                        for (var i = 0; i < $scope.categoriesTree.length; i++)
                            if ($scope.categoriesTree[i].id == $scope.tempCategory.id) {
                                isMatchId = !0;
                                break
                            }
                        isMatchId || $scope.categoriesTree.push($scope.tempCategory)
                    }
                })
            } else {
                node.remove();
                for (var isMatchId = !1, i = 0; i < $scope.itemsTree.length; i++)
                    if ($scope.itemsTree[i].id == value.id) {
                        isMatchId = !0;
                        break
                    }
                isMatchId || $scope.itemsTree.push(value)
            }
        }, $scope.syncObject = function(obj) {
            for (var tempObj = angular.copy(obj), temp = {
                categories: [],
                category: {
                    id: "",
                    items: []
                },
                item: {
                    id: "",
                    price: 0
                }
            }, i = 0; i < tempObj.categories.length; i++) {
                temp.category = {
                    id: "",
                    items: []
                }, temp.category.id = tempObj.categories[i].id;
                for (var itemsObj = tempObj.categories[i].items, j = 0; j < itemsObj.length; j++) temp.item = {
                    id: "",
                    price: 0
                }, temp.item.id = itemsObj[j].id, temp.item.price = itemsObj[j].price, temp.category.items.push(temp.item);
                temp.categories.push(temp.category)
            }
            return tempObj.categories = temp.categories, tempObj.restaurantId = $rootScope.restaurantID || "", tempObj
        }, $scope.resetForm = function() {
            $scope.submenu = {
                name: "",
                description: "",
                categories: []
            }, $scope.itemsTree = angular.copy($scope.templeTree.items),
                $scope.categoriesTree = angular.copy($scope.templeTree.categories)
        }, $scope.refreshView = function(data) {
            var isMatchId = !1;
            if (data) {
                for (var i = 0; i < $scope.subMenuList.length; i++)
                    if ($scope.subMenuList[i].id == data.id) {
                        isMatchId = !0, $scope.subMenuList[i] = data, alertify.success("Updated Successfully");
                        break
                    }
                isMatchId || ($scope.subMenuList = [], $scope.resetForm(), alertify.success("Created Successfully")), $scope.getSubMenuList()
            }
        }, $scope.save = function(item) {
            var obj = $scope.syncObject(item);
            submenusResource.addOrUpdate(obj).success(function() {
                $state.go("app.owner.menu.submenu.list", {}, {
                    reload: !0
                }), alertify.success("Successfully")
            }).error(function() {
                alertify.error("Oop! Please try again")
            })
        }, $scope.createSubMenu = function() {
            $state.go("app.owner.menu.submenu.create"), setTimeout(function() {
                $scope.addTipText()
            }, 100), $scope.resetForm()
        }, $scope.editSubMenu = function(item) {
            item.editLoading = !0, setTimeout(function() {
                $state.go("app.owner.menu.submenu.edit"),
                    submenusResource.getByID(item).success(function(payload) {
                        if ($scope.submenu = payload.data.submenus, $scope.itemsTree = angular.copy($scope.templeTree.items), $scope.categoriesTree = angular.copy($scope.templeTree.categories), $scope.submenu.categories.length > 0)
                            for (var i = 0; i < $scope.submenu.categories.length; i++) {
                                for (var j = 0; j < $scope.categoriesTree.length; j++) $scope.categoriesTree[j].id == $scope.submenu.categories[i].id && $scope.categoriesTree.splice(j, 1);
                                for (var j = 0; j < $scope.submenu.categories[i].items.length; j++)
                                    for (var k = 0; k < $scope.itemsTree.length; k++) $scope.submenu.categories[i].items[j].id == $scope.itemsTree[k].id && $scope.itemsTree.splice(k, 1)
                            }
                        item.editLoading = !1
                    }).error(function() {
                        alertify.error("Oop! Please try again")
                    })
            }, 500)
        }, $scope.deleteSubMenu = function(item) {
            alertify.confirm("Are you sure you want to delete?", function(e) {
                e && submenusResource["delete"](item).success(function() {
                    $state.go($state.current, {}, {
                        reload: !0
                    }), alertify.success("Deleted successfully")
                }).error(function() {
                    alertify.error("Oops! Sub-Menu have be used on Menu. Please check again before deleting")
                })
            })
        }
    }]);
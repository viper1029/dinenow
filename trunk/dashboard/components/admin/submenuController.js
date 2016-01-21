angular.module("app.admin.restaurant.manage.menu.submenu", [])
    .controller("AdminSubmenuController", ["$scope", "$filter", "$state", "$stateParams", "$modal", "ngTableConfig", "adminCategoriesResource", "adminItemsResource", "adminSubmenusResource",
        function($scope, $filter, $state, $stateParams, $modal, ngTableConfig,
                 adminCategoriesResource, adminItemsResource, adminSubmenusResource) {
            $scope.templeTree = {
                categories: [],
                items: []
            },
                $scope.itemsTree = [],
                $scope.categoriesTree = [],
                $scope.submenu = {
                    name: "",
                    description: "",
                    categories: []
                }
            $scope.subMenuList = [],
                $scope.restaurantID = $stateParams.restaurantId,
                $scope.getSubMenuList = function() {
                    adminSubmenusResource.get($scope.restaurantID).success(function(payload) {
                        console.log(payload.data.submenus);
                        $scope.subMenuList = payload.data.submenus,
                            $scope.tableParams = ngTableConfig.config($scope.subMenuList)
                    }).error(function() {})
                }, $scope.getCategoryList = function() {
                adminCategoriesResource.get($scope.restaurantID).success(function(payload) {
                    console.log(payload.data);
                    $scope.categoriesTree = payload.data.categories,
                        $scope.containers=payload.data.categories,
                        console.log($scope.categoriesTree);
                    $scope.templeTree.categories = angular.copy(payload.data.categories)
                })
            }, $scope.getItemList = function() {
                adminItemsResource.get($scope.restaurantID).success(function(payload) {
                    console.log(payload);
                    $scope.itemsTree = payload.data.items,
                        $scope.models=payload.data.items,
                        $scope.templeTree.items = angular.copy(payload.data.items)
                })
            },
                $scope.syncObject = function(obj) {
                    console.log(obj);
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
                        }, temp.item.id = itemsObj[j].id, temp.item.price = itemsObj[j].price,
                            temp.category.items.push(temp.item);
                        temp.categories.push(temp.category)
                    }
                    return tempObj.categories = temp.categories, tempObj.restaurantId = $scope.restaurantID || "", tempObj
                },
                $scope.save = function(submenu){
                    var obj = $scope.syncObject(submenu);
                    $scope.isLoadingSave = !0, setTimeout(function() {
                        console.log(obj)
                        adminSubmenusResource.addOrUpdate(obj).success(function() {
                            $scope.isLoadingSave = !1,
                                $state.go("app.admin.restaurant.manage.menu.submenu.list", {}, {
                                    reload: !0
                                }), alertify.success("Successfully")
                        }).error(function() {
                            alertify.error("Oop! Please try again")
                        })
                    }, 500)
                },
                $scope.editSubMenu = function(item) {
                    item.editLoading = !0,
                        setTimeout(function() {
                            $state.go("app.admin.restaurant.manage.menu.submenu.edit"),
                                adminSubmenusResource.getByID(item).success(function(payload) {
                                    if ($scope.submenu = payload.data.submenu,
                                            $scope.itemsTree = angular.copy($scope.templeTree.items),
                                            $scope.categoriesTree = angular.copy($scope.templeTree.categories),
                                        $scope.submenu.categories.length > 0)
                                        for (var i = 0; i < $scope.submenu.categories.length; i++) {
                                            for (var j = 0; j < $scope.categoriesTree.length; j++)
                                                $scope.categoriesTree[j].id == $scope.submenu.categories[i].id && $scope.categoriesTree.splice(j, 1);
                                            for (var j = 0; j < $scope.submenu.categories[i].items.length; j++)
                                                for (var k = 0; k < $scope.itemsTree.length; k++)
                                                    $scope.submenu.categories[i].items[j].id == $scope.itemsTree[k].id && $scope.itemsTree.splice(k, 1)
                                        }
                                    item.editLoading = !1
                                }).error(function() {
                                    alertify.error("Oop! Please try again")
                                })
                        }, 500)
                }, $scope.deleteSubMenu = function(item) {
                alertify.confirm("Are you sure you want to delete?", function(e) {
                    e && adminSubmenusResource["delete"](item).success(function() {
                        $state.go($state.current, {}, {
                            reload: !0
                        }), alertify.success("Deleted successfully")
                    }).error(function() {
                        alertify.error("Oops! Sub-Menu have be used on Menu. Please check again before deleting")
                    })
                })
            },
                $scope.init = function() {
                    $scope.getSubMenuList(),
                        setTimeout(function() {
                            $scope.getCategoryList()
                        }, 100), setTimeout(function() {
                        $scope.getItemList()
                    }, 200),
                        $scope.initNgTable = function(data) {
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
                        },
                        $scope.addTipText = function() {
                            var emptyTree = document.getElementsByClassName("angular-ui-tree-empty");
                            if (emptyTree) {
                                var p = document.createElement("p"),
                                    textNode = document.createTextNode("Drag and Drop from Categories and Items to here");
                                p.appendChild(textNode),
                                    emptyTree[0].appendChild(p),
                                    emptyTree[0].className += " empty-tree"
                            }
                        }
                }, $scope.init(),
                $scope.getActiveItem = function(item) {
                    return item.id == $scope.submenu.id ? "item-active" : void 0
                },
                $scope.createSubMenu = function() {
                    $state.go("app.admin.restaurant.manage.menu.submenu.create")

                },
                $scope.viewDetail = function(item) {
                    submenusResource.getByID(item).success(function(payload) {
                        console.log(payload);
                        $scope.submenu = payload.data.submenus
                    }).error(function() {
                        alertify.error("Oop! Please try again")
                    })
                },
                $scope.dragoverCallback = function(event, index, external, type) {
                    $scope.logListEvent('dragged over', event, index, external, type)
                    // Disallow dropping in the third row. Could also be done with dnd-disable-if.
                    return index < 10
                },

                $scope.dropCallback = function(event, index, item, external, type, allowedType) {
                    $scope.logListEvent('dropped at', event, index, external, type)
                    if (external) {
                        if (allowedType === 'itemType' && !item.label) return false
                        if (allowedType === 'containerType' && !angular.isArray(item))
                            return false
                    }
                    return item
                },

                $scope.logEvent = function(message, event) {
                    console.log(message, '(triggered by the following', event.type, 'event)')
                    console.log(event)
                },

                $scope.logListEvent = function(action, event, index, external, type) {
                    var message = external ? 'External ' : ''
                    message += type + ' element is ' + action + ' position ' + index
                    $scope.logEvent(message, event)
                },


                $scope.$watch('submenu', function(submenu) {
                    $scope.itemToProcessAsJson = angular.toJson(submenu, true)
                }, true)

        }]);

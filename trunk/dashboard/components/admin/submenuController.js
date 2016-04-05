angular.module("app.admin.restaurant.manage.menu.submenu", [])
    .controller("AdminSubmenuController", ["$scope", "$filter", "$state", "$stateParams",
        "$modal", "ngTableConfig", "adminCategoriesResource", "adminItemsResource", "adminSubmenusResource",
        function ($scope, $filter, $state, $stateParams, $modal, ngTableConfig,
                  adminCategoriesResource, adminItemsResource, adminSubmenusResource) {

            $scope.subMenuList = [];

            $scope.init = function () {
                $scope.getSubMenuList(),
                    setTimeout(function () {
                        $scope.getCategoryList()
                    }, 100), setTimeout(function () {
                    $scope.getItemList()
                }, 200),
                    $scope.initNgTable = function (data) {
                        $scope.tableParams = new ngTableParams({
                            page: TABLE_OPTIONS.page,
                            count: TABLE_OPTIONS.count
                        }, {
                            total: data.length,
                            getData: function ($defer, params) {
                                var orderedData = params.sorting() ? $filter("orderBy")(data, params.orderBy()) : data;
                                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()))
                            }
                        })
                    },
                    $scope.addTipText = function () {
                        var emptyTree = document.getElementsByClassName("angular-ui-tree-empty");
                        if (emptyTree) {
                            var p = document.createElement("p"),
                                textNode = document.createTextNode("Drag and Drop from Categories and Items to here");
                            p.appendChild(textNode),
                                emptyTree[0].appendChild(p),
                                emptyTree[0].className += " empty-tree"
                        }
                    }
            };
            $scope.init();
        }]);

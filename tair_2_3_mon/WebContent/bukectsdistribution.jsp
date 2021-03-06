<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Node Info List</title>
<link rel="stylesheet" type="text/css"
	href="./resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="./ux/css/PanelResizer.css" />
<!-- overrides to base library -->
<link rel="stylesheet" type="text/css"
	href="./ux/gridfilters/css/GridFilters.css" />
<link rel="stylesheet" type="text/css"
	href="./ux/gridfilters/css/RangeMenu.css" />
<style type="text/css">
</style>

<!-- GC -->
<!-- LIBS -->
<script type="text/javascript" src="./adapter/ext/ext-base.js"></script>
<!-- ENDLIBS -->

<script type="text/javascript" src="./ext-all.js"></script>

<!-- extensions -->
<script type="text/javascript" src="./ux/gridfilters/menu/RangeMenu.js"></script>
<script type="text/javascript" src="./ux/gridfilters/menu/ListMenu.js"></script>

<script type="text/javascript" src="./ux/gridfilters/GridFilters.js"></script>
<script type="text/javascript" src="./ux/gridfilters/filter/Filter.js"></script>
<script type="text/javascript"
	src="./ux/gridfilters/filter/StringFilter.js"></script>
<script type="text/javascript"
	src="./ux/gridfilters/filter/DateFilter.js"></script>
<script type="text/javascript"
	src="./ux/gridfilters/filter/ListFilter.js"></script>
<script type="text/javascript"
	src="./ux/gridfilters/filter/NumericFilter.js"></script>
<script type="text/javascript"
	src="./ux/gridfilters/filter/BooleanFilter.js"></script>

<script type="text/javascript" src="./ux/ProgressBarPager.js"></script>
<script type="text/javascript" src="./ux/PanelResizer.js"></script>
<script type="text/javascript" src="./ux/PagingMemoryProxy.js"></script>

</head>
<body>
	<script type="text/javascript">
		Ext.onReady(function() {

			Ext.QuickTips.init();

			// configure whether filter query is encoded or not (initially)
			var encode = false;

			// configure whether filtering is performed locally or remotely (initially)
			var local = true;

			store = new Ext.data.JsonStore({
				// store configs
				autoDestroy : true,
				url : 'bucketsdistrbution',
				remoteSort : false,
				sortInfo : {
					field : 'nodeidentifer',
					direction : 'ASC'
				},
				storeId : 'myStore',

				// reader configs
				root : 'root',
				totalProperty : 'totalproperty',
				fields : [ {
					name : 'nodeidentifer',
					type : 'string'
				}, {
					name : 'bucketcount'
				}, {
					name : 'bucketlist',
					type : 'string'
				} ]
			});

			var filters = new Ext.ux.grid.GridFilters({
				// encode and local configuration options defined previously for easier reuse
				encode : encode, // json encode the filter query
				local : local, // defaults to false (remote filtering)
				filters : [ {
					type : 'string',
					dataIndex : 'nodeidentifer'
				},  {
					type : 'numeric',
					dataIndex : 'bucketcount'
				},{
					type : 'string',
					dataIndex : 'bucketlist'
				} ]
			});

			// use a factory method to reduce code while demonstrating
			// that the GridFilter plugin may be configured with or without
			// the filter types (the filters may be specified on the column model
			var createColModel = function(finish, start) {

				var columns = [ {
					dataIndex : 'nodeidentifer',
					header : 'nodeidentifer',
					filterable : true,
					width : 160 ,
					filter : {
						type : 'string'
					// specify disabled to disable the filter menu
					//, disabled: true
					}
				}, {
					dataIndex : 'bucketcount',
					header : 'bucketcount',
					filter : {
						type : 'string'
					},
					width : 80
				}, {
					header : 'bucketlist',
					id : 'bucketlist',
					dataIndex : 'bucketlist'
				} ];

				return new Ext.grid.ColumnModel({
					columns : columns.slice(start || 0, finish),
					defaults : {
						sortable : true
					}
				});
			};

			var grid = new Ext.grid.GridPanel({
				border : true,
				stripeRows : true,
				frame : true,
				height : 600,
				width : 1124,
				store : store,
				colModel : createColModel(11),
				loadMask : true,
				plugins : [ filters ],
				autoExpandColumn : 'bucketlist',
				listeners : {
					render : {
						fn : function() {
							store.load({
								params : {
									start : 0,
									limit : 65535
								}
							});
						}
					}
				}
			});
			
			   function TrafficChange(val){
			        if(val == "alive"){
			            return '<span style="color:green;">' + val + '</span>';
			        }else if(val == "dead"){
			            return '<span style="color:red;">' + val + '</span>';
			        }
			        return val;
			    }
			   
			grid.render('grid-online-list');
			var updateData = function() {
				store.reload();
			}
			var task = {
				run : updateData,
				interval : 60000
			//45 seconds
			}
			var runner = new Ext.util.TaskRunner();
			runner.start(task);

		});
	</script>
	<center>
		<jsp:include page="__common_head.jsp"></jsp:include>
		<p>There is some advanced statistics information more detailed <a href="online_node_area_list.jsp">click here for details</a></p> 
		<div id="grid-online-list"></div>
	</center>

</body>
</html>
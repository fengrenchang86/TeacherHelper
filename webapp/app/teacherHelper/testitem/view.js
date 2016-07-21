
define(["dojo/_base/declare", 
        "dijit/_Widget",
		"dijit/_WidgetBase", 
		"dijit/_TemplatedMixin", 
		"dojo/_base/lang",
		"dojo/dom-form",
		'dojo/string',
		"dojo/on",
		"dojo/ready",
		"dijit/registry",
		"dojo/text!./view.html",
		"dojo/i18n!app/teacherHelper/nls/view",
		"dijit/form/TextBox",
		"dijit/form/Button"], 
	function(declare, Widget, WidgetBase, TemplatedMixin, 
			lang, domForm, String, on, ready, registry,
			template, nls,
			TextBox, Button){
		var rt = declare([WidgetBase, Widget, TemplatedMixin], {
			templateString:template,
			i18n:nls,
//			templateString:dojo.cache("template", "./template.html"),
			postCreate: function(){
				this.inherited(arguments);
				console.log(nls);
				var btnSubmit = new Button({
					label:"刷新"
				});
				btnSubmit.placeAt(this.btnSubmit);
				on(btnSubmit, "click", lang.hitch(this, this.query));
				
			},
			query:function() {
				var _self = this;
				var grade = _self.grade;
				var subject = _self.subject;
				var param = {
					"grade":grade,
					"subject" : subject	
				};
				var xhrArgs = {
					url:"wf/testitem/query",
					handleAs: "json",
					postData : dojo.toJson(param),
					headers:{
						"Content-Type":"application/json"
					},
					load:function(data, ioArgs) {
//						if (data.response == null || data.response.retCode == "E01") {
//							console.log("Logon error?" + data.response.retCode);
//							dg.show();
//						}
						_self.updateGrid(data);
						_self.dataArr = data.response;
						console.log(data);
//						_self.updateGrid(data.response);
					},
					error:function(response) {
						console.log("[listFiles]error:" + response);
					}
				}
				var deferred = dojo.xhrPost(xhrArgs);
				console.log("query done");
			},
			updateGrid:function(dataArray) {
				var _self = this;
				require(['dojo/_base/lang', "dojox/grid/DataGrid", "dojo/data/ItemFileWriteStore", 
				         "dojo/query", 'dojo/domReady!'], 
					function(lang, DataGrid, ItemFileWriteStore,
							query){
					var data = {
							   identifier: 'id',
							   items: []
							};
					
					var dataList = [];
					for (var i = 0; i < dataArray.testitems.length; i++) {
						var obj = {
							index:i,
							idx:dataArray.testitems[i].id,
							subject:dataArray.testitems[i].subjectid,
							grade:dataArray.testitems[i].grade,
							unit:dataArray.testitems[i].unit,
							level:dataArray.testitems[i].level,
							content1:dataArray.testitems[i].contentL1,
							content2:dataArray.testitems[i].contentL2,
							content3:dataArray.testitems[i].contentL3,
							seq:dataArray.testitems[i].seq,
						};
						
						dataList.push(obj);
						data.items.push(lang.mixin({ id: i+1 }, dataList[i]));
					}
					console.log(data.items);
						
					var store = new ItemFileWriteStore({data: data});
					var layout = [[
						{'name': 'ID', 'field': 'index', 'width': '5%'},
						{'name': 'Subject', 'field': 'subject', 'width': '8%'},
						{'name': 'Grade', 'field': 'grade', 'width': '8%'},
						{'name': 'Unit', 'field': 'unit', 'width': '8%'},
						{'name': 'Level', 'field': 'level', 'width': '8%'},
						{'name': 'Content1', 'field':'content1', 'width':'17%'},
						{'name': 'Content2', 'field':'content2', 'width':'20%'},
						{'name': 'Content3', 'field':'content3', 'width':'20%'},
						{'name': 'SEQ', 'field':'seq', 'width':'20%'}
					]];
					
					if (_self.grid != null && _self.grid != undefined) {
						_self.grid.destroy();
					}
							
					_self.grid = new DataGrid({
						id: 'gridDiv',
						store: store,
						structure: layout,
						rowSelector: '20px'},
						document.createElement('div'));
					_self.grid.placeAt("grid_listFile");
					_self.grid.startup();
				
					g_grid = _self.grid;
				});
			},
		});
		ready(function(){
	        // Call the parser manually so it runs after our widget is defined, and page has finished loading
	       // parser.parse();
	    });
		return rt;
});

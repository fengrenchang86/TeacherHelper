
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
		"dojo/text!./gridinput.html",
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
				var btnSubmit = new Button({
					label:"提交"
				});
				btnSubmit.placeAt(this.btnSubmit);
				on(btnSubmit, "click", lang.hitch(this, this.onSubmit));
				
				var btnRefresh = new Button({
					label:"刷新"
				});
				btnRefresh.placeAt(this.btnRefresh);
				on(btnRefresh, "click", lang.hitch(this, this.updateGrid));
				
				
			},
			onSubmit : function() {
				console.log("onSubmit");
				var _self = this;
				var grade = _self.grade;
				var subject = _self.subject;
				var param = {
					list:[]
				};
				var data = _self.grid.store._arrayOfAllItems;
				for (var i = 0; i < data.length; i++) {
					var obj = {
						"contentL1" : data[i].content1[0],
						"contentL2" : data[i].content2[0],
						"contentL3" : data[i].content3[0],
						"grade" : grade,
						"subjectName" : subject,
						"unit" : data[i].unit[0],
						"level" : data[i].level[0]
					}
					if (obj.contentL1 != "") {
						param.list.push(obj);
					}
				}
				console.log(param);
				
				var xhrArgs = {
					url:"wf/testitem/createmulti",
					handleAs: "json",
					postData : dojo.toJson(param),
					headers:{
						"Content-Type":"application/json"
					},
					load:function(data, ioArgs) {
//						
					},
					error:function(response) {
						console.log("[listFiles]error:" + response);
					}
				}
				var deferred = dojo.xhrPost(xhrArgs);
			},
			updateGrid:function() {
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
					var n = 20;
					for (var i = 0; i < n; i++) {
						var obj = {
							index:i,
							subject:"",
							grade:"",
							unit:"",
							level:"",
							content1:"",
							content2:"",
							content3:"",
							seq:"",
						};
						
						dataList.push(obj);
						data.items.push(lang.mixin({ id: i+1 }, dataList[i]));
					}
						
					var store = new ItemFileWriteStore({data: data});
					var layout = [[
						{'name': 'ID', 'field': 'id', 'width': '5%'},
//						{'name': 'Subject', 'field': 'subject', 'width': '8%', 'editable':true},
//						{'name': 'Grade', 'field': 'grade', 'width': '8%', 'editable':true},
						{'name': 'Unit', 'field': 'unit', 'width': '8%', 'editable':true},
						{'name': 'Level', 'field': 'level', 'width': '8%', 'editable':true},
						{'name': 'Content1', 'field':'content1', 'width':'17%', 'editable':true},
						{'name': 'Content2', 'field':'content2', 'width':'20%', 'editable':true},
						{'name': 'Content3', 'field':'content3', 'width':'20%', 'editable':true}
//						{'name': 'SEQ', 'field':'seq', 'width':'20%'}
					]];
					
					if (_self.grid != null && _self.grid != undefined) {
						_self.grid.destroy();
					}
							
					_self.grid = new DataGrid({
						id: 'gridInputDiv',
						store: store,
						structure: layout,
						rowSelector: '20px'},
						document.createElement('div'));
					
					
					_self.grid.placeAt(_self.grid_input);
					_self.grid.startup();
				
					g_gridinput = _self.grid;
				});
			},
		});
		ready(function(){
	        // Call the parser manually so it runs after our widget is defined, and page has finished loading
	       // parser.parse();
	    });
		return rt;
});


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
		"app/teacherHelper/testitem/import",
		"app/teacherHelper/testitem/view",
		"app/teacherHelper/testitem/gridinput",
		"dojo/text!./input.html",
		"dojo/i18n!app/teacherHelper/nls/input",
		"dijit/form/TextBox",
		"dijit/form/Button"], 
	function(declare, Widget, WidgetBase, TemplatedMixin, 
			lang, domForm, String, on, ready, registry,
			ImportTestitem, ViewTestitemDlg, GridInput,
			template, nls,
			TextBox, Button){
		var rt = declare([WidgetBase, Widget, TemplatedMixin], {
			templateString:template,
			i18n:nls,
			impTestitem : null,
//			templateString:dojo.cache("template", "./template.html"),
			postCreate: function(){
				this.inherited(arguments);
				console.log(nls);
				var btnBrowze = new Button({
					label:"浏览考点数据库"
				});
				btnBrowze.placeAt(this.btnBrowze);
				on(btnBrowze, "click", lang.hitch(this, this.onView));
				
				var btnImport = new Button({
					label: "批量导入考点数据"
				});
				btnImport.placeAt(this.btnImport);
				on(btnImport, "click", lang.hitch(this, this.onImport));
				
				var btnInsert = new Button({
					label: "在数据库中添加"
				});
				btnInsert.placeAt(this.btnInsert);
				on(btnInsert, "click", lang.hitch(this, this.onSubmit));
				
				var btnSubmit = new Button({
					label: "继续添加考点"
				});
				btnSubmit.placeAt(this.btnSubmit);
				on(btnSubmit, "click", lang.hitch(this, this.onAdd));
				
				this.impTestitem = new ImportTestitem();
				this.impTestitem.placeAt(this.import_div);
				
				this.viewTestitem = new ViewTestitemDlg();
				this.viewTestitem.placeAt(this.view_div);
				
				this.gridInput = new GridInput();
				this.gridInput.placeAt(this.gridinput_div);
				
				console.log("input.js done postCreate");
//				dojo.parser.parse("import_div");
			//	console.log(this._submitButton);
			},
			handleOnValidStateChange:function(isValid) {
				alert(isValid);
			},
			onAdd:function() {
				console.log("onAdd");
				var grade = dijit.byId("grade").value;
				var subject = dijit.byId("subject").value;
				var unit = dijit.byId("unit").value;
				var content = dijit.byId("content").value;
				var parentContent = dijit.byId("parentContent").value;
				var obj = {
					grade: grade,
					subject : subject,
					unit : unit,
					content: content,
					parentContent : parentContent
				};
				var xhrArgs = {
					url:"/wf/testitem/create",
					handleAs: "json",
					postData: dojo.toJson(obj),
					headers:{
						"Content-Type":"application/json"
					},
					load:function(data) {
						console.log(data);
					},
					error:function(response) {
						console.log("[listFiles]error:" + response);
					}
				}
					var deferred = dojo.xhrPost(xhrArgs);
					console.log("sendRequest done");
			},
			onView:function() {
				console.log("onView");
				var grade = dijit.byId("grade").value;
				console.log("grade:" + grade);
				
				var subject = dijit.byId("subject").value;
				console.log("subject:" + subject);
				
				this.viewTestitem.grade = grade;
				this.viewTestitem.subject = subject;
				dijit.byId("view_div").show();
//				this.viewTestitem.query();
				lang.hitch(this.viewTestitem, this.viewTestitem.query)();
			},
			onImport:function() {
				console.log("onImport");
				
				var grade = dijit.byId("grade").value;
				console.log("grade:" + grade);
				
				var subject = dijit.byId("subject").value;
				console.log("subject:" + subject);
				
				this.impTestitem.grade = grade;
				this.impTestitem.subject = subject;
				
				dijit.byId("import_div").show();
//				this.import_div.show();
			},
			onSubmit:function() {
				console.log("onSubmit");
				
				var grade = dijit.byId("grade").value;
				console.log("grade:" + grade);
				
				var subject = dijit.byId("subject").value;
				console.log("subject:" + subject);
				
				this.gridInput.grade = grade;
				this.gridInput.subject = subject;
				this.gridInput.updateGrid();
				
				dijit.byId("gridinput_div").show();
//				lang.hitch(this.gridInput, this.gridInput.updateGrid)();
				this.gridInput.updateGrid();
			},
			sendRequest:function() {
				var _self = this;
				console.log("sendRequest");
				var type = "1";
				if (dijit.byId('type').get('value') == "TC") {//teacher
					type = 1;
				} else if (dijit.byId('type').get('value') == "ST") {//student
					type = 2;
				}
				
				var obj = {
					username:dojo.byId("username").value,
					password:dojo.byId("password").value,
					email:dojo.byId("email").value,
					nickname:dojo.byId("nickname").value,
					type:type
				};
				var xhrArgs = {
					url:"/wf/User/reg",
					handleAs: "json",
					postData: dojo.toJson(obj),
					headers:{
						"Content-Type":"application/json"
					},
					load:function(data) {
						console.log(data);
					},
					error:function(response) {
						console.log("[listFiles]error:" + response);
					}
				}
				var deferred = dojo.xhrPost(xhrArgs);
				console.log("sendRequest done");
			}
		});
		ready(function(){
	        // Call the parser manually so it runs after our widget is defined, and page has finished loading
	       // parser.parse();
	    });
		return rt;
});

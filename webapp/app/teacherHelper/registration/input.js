
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
		"dojo/text!./input.html",
		"dojo/i18n!app/teacherHelper/nls/input",
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
				var btn = new Button({
					label:nls.register
				});
				btn.placeAt(this.btnContainer);
				on(btn, "click", lang.hitch(this, this.onSubmit));
				
			//	console.log(this._submitButton);
			},
			handleOnValidStateChange:function(isValid) {
				alert(isValid);
			},
			onSubmit:function() {
				console.log("onSubmit");
				this.sendRequest();
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

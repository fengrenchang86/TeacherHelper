
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
		"dojo/io/iframe",
		"dojo/text!./import.html",
		"dojo/i18n!app/teacherHelper/nls/import",
		"dijit/form/TextBox",
		"dijit/form/Button"], 
	function(declare, Widget, WidgetBase, TemplatedMixin, 
			lang, domForm, String, on, ready, registry, iframe,
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
					label:"提交1"
				});
				btnSubmit.placeAt(this.btnSubmit);
				on(btnSubmit, "click", lang.hitch(this, this.onSubmit));
				
			},
			handleResp : function(data) {
				console.log("handleResp:" + data);
				var resp = JSON.parse(data);
				console.log(resp.response.responseStatus);
			},
			handleOnValidStateChange:function(isValid) {
				alert(isValid);
			},
			onSubmit:function() {
				console.log("onSubmit");
				this.sendRequest();
			},
			sendRequest:function() {
				var grade = this.grade;
				var subject = this.subject;
				iframe.send({  
	                form: "form_import",  
	                url: "/ul/",  
	                method: "post",  
	                handleAs: "text",
	                handle : this.handleResp,
	                content: {
	                    "command": "import",
	                    "grade" : grade,
	                    "subject" : subject
	                }
	            });  
				
				console.log("sendRequest done");
			}
		});
		ready(function(){
	        // Call the parser manually so it runs after our widget is defined, and page has finished loading
	       // parser.parse();
	    });
		return rt;
});

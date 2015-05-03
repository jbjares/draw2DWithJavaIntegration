
example.propertypane.PropertyPaneAttribute = Class.extend({
	
	init:function(stateFigure){
	    this.figure = stateFigure;
	},
	
	injectPropertyView: function( domId)
	{

	    var view2 = $("<div class='control-group'>"+
                "   <label class='control-label' for='attributeCodeProperty'>Code </label>"+
                "   <div class='controls'>"+
                "   <textarea id='attributeCodeProperty' style='width:1000px; height:500px;''>"+this.figure.getCode()+"</textarea>"+
                "   </div>"+
                "</div>");
		   var inputCode = view2.find("#attributeCodeProperty");
		   
		   var handlerCode =$.proxy(function(e){
		       e.preventDefault();
		       console.log("inputCode.val() "+inputCode.val());
		       this.figure.setCode(inputCode.val());
		       //app.executeCommand(new example.command.CommandSetCode(this.figure, inputCode.val()));
		   },this);
		   
		   inputCode.change(handlerCode);
		
		   domId.append(view2);

	    
	},

    /**
     * @method
     * called by the framework if the pane has been resized. This is a good moment to adjust the layout if
     * required.
     * 
     */
    onResize: function()
    {
    },
    

    /**
     * @method
     * called by the framework before the pane will be removed from the DOM tree
     * 
     */
    onHide: function()
    {
    }
    
});


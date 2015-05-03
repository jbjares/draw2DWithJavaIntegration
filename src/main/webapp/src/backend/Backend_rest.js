

example.backend.Backend_rest = Class.extend({
	
    definitions : [{
                    name:"DefName01",
                    id:"id01",
                    content:[{"type":"draw2d.shape.state.Start","id":"98fcbeb5-3c72-af82-41e4-7f9bd73660c5","x":63,"y":73,"width":50,"height":50,"userData":null},{"type":"draw2d.shape.state.End","id":"174353d9-f4c6-617d-29d0-aa10601b2d07","x":252,"y":175,"width":50,"height":50,"userData":null},{"type":"draw2d.shape.state.Connection","id":"0c1c81f8-6e35-2a94-f186-b964528fac13","userData":null,"cssClass":"stroke","stroke":2,"color":"#1B1B1B","source":{"node":"98fcbeb5-3c72-af82-41e4-7f9bd73660c5","port":"output0"},"target":{"node":"174353d9-f4c6-617d-29d0-aa10601b2d07","port":"input0"},"router":"draw2d.layout.connection.FanConnectionRouter","label":"label"}]
                 },
                 {
                     name:"DefName02",
                     id:"id02",
                     content:[]
                  }
                ],
    
	init:function(){
      
	},
	
	getActivities: function(successCallback){
		successCallback( [
	            { id:"ReloadDefinitions",
	              parameters:[]
	            }
	           ]);
	},
	
    getPrerequisitVariables: function(successCallback){
        successCallback( [ "currentUser" ]);
    },
    	
    /**
     * @method
     * Called if the selection in the canvas has been changed. You must register this
     * class on the canvas to receive this event.
     * 
     * @param {draw2d.Figure} figure
     */
    getDefinitions : function(successCallback, errorCallback){
        successCallback({ definitions:this.definitions });
    },
    
    create: function(definitionId, successCallback, errorCallback){
    	console.log("create: definitionId "+definitionId);
        this.definitions = $.grep(this.definitions, function(e){ return e.id !== definitionId; });
        var newDef = {name:definitionId, id:definitionId, content:[]};
        
        
        console.log("Calling rest service in create method...");
        // Assign handlers immediately after making the request,
        // and remember the jqxhr object for this request
        	var saveArgs = JSON.stringify(newDef);
	      $.postJSON(getContext()+'/rest/editor/save',JSON.stringify(saveArgs), function (data, status, xhr) {
	    	  console.log( "success" );
	    	});
        
        //result.push(newDef);
        successCallback(newDef.content);
    }, 

    del: function(definitionId, successCallback, errorCallback){
    	console.log("del: definitionId "+definitionId);
        this.definitions = $.grep(this.definitions, function(e){ return e.id !== definitionId; });
        successCallback();
    }, 
    
    
    save: function(definitionId, content, successCallback, errorCallback){
    	
    	
        console.log("Getting initial data...");
        // Assign handlers immediately after making the request,
        // and remember the jqxhr object for this request

        var jqxhr = $.getJSON( getContext()+"/rest/editor/get", function(data) {
        	
			              console.log("Calling rest service in save method..."+data);

			              var newDef = {name:data.name, id:data.id, content:JSON.stringify(content)};
			              var saveArgs = JSON.stringify(newDef);
			              console.log("saveArgs "+ jQuery.parseJSON(saveArgs));
			              $.postJSON(getContext()+"/rest/editor/save",JSON.stringify(saveArgs), function (data, status, xhr) {
			            	  console.log( "success" );
			            	  //loadChart(data);
			            	});
			              
          
        })

          .fail(function() {
            console.log( "error" + errorCallback );
	      	  var newDef = {name:'data', id:'data', content:[]};
	          var saveArgs = JSON.stringify(newDef);
	          $.postJSON(getContext()+'/rest/editor/save',JSON.stringify(saveArgs), function (data, status, xhr) {
	        	  console.log( "success" );
	        	  //loadChart(data);
	        	});
          })        
        
    },
    
    load:  function(definitionId, successCallback, errorCallback){

        var jqxhr = $.getJSON( getContext()+"/rest/editor/get", function(data) {
        		successCallback(data);
        		console.log( "success" );
			})
			.fail(function() {
				console.log( "error" + errorCallback);
			});
        
        

        jqxhr.complete(function(data) {
        	successCallback(data);
        	console.log( "complete" );
        	successCallback(jQuery.parseJSON(data.responseText).content);
            if (jQuery.parseJSON(data.responseText).content == 0) {
                // not found
            } else if (jQuery.parseJSON(data.responseText).content == 1) {
            	successCallback(data.content );
            }        	
        });
        
        //loadChart(null);
       
    }
    
});

function loadChart(data){
	var x = new Array();
	var y = new Array();
	
	if(data!=null){
		for (var i in data){
			console.log('Key is: ' + i + '. Value is: ' + data[i]);
			x.push(i);
			y.push(data[i]);
		}
		
	}else{
		x = ["0","1","2","3","4","5","6","7","8","9"];
		y = ["0","0","0","0","0","0","0","0","0","0"];
	}
	

	
	
    //Get context with jQuery - using jQuery's .get() method.
    var ctx = $("#myChart").get(0).getContext("2d");
    //This will get the first returned node in the jQuery collection.
    var myNewChart = new Chart(ctx);
    
    var data = {
 			labels : x,
 			datasets : [
 				{
 					fillColor : "rgba(220,220,220,0.5)",
 					strokeColor : "rgba(220,220,220,1)",
 					pointColor : "rgba(220,220,220,1)",
 					pointStrokeColor : "#fff",
 					data : y
 				}
 			]
 		}
    
    options = {
			
 			//Boolean - If we show the scale above the chart data			
 			scaleOverlay : false,
 			
 			//Boolean - If we want to override with a hard coded scale
 			scaleOverride : false,
 			
 			//** Required if scaleOverride is true **
 			//Number - The number of steps in a hard coded scale
 			scaleSteps : null,
 			//Number - The value jump in the hard coded scale
 			scaleStepWidth : null,
 			//Number - The scale starting value
 			scaleStartValue : null,

 			//String - Colour of the scale line	
 			scaleLineColor : "rgba(0,0,0,.1)",
 			
 			//Number - Pixel width of the scale line	
 			scaleLineWidth : 1,

 			//Boolean - Whether to show labels on the scale	
 			scaleShowLabels : true,
 			
 			//Interpolated JS string - can access value
 			scaleLabel : "<%=value%>",
 			
 			//String - Scale label font declaration for the scale label
 			scaleFontFamily : "'Arial'",
 			
 			//Number - Scale label font size in pixels	
 			scaleFontSize : 12,
 			
 			//String - Scale label font weight style	
 			scaleFontStyle : "normal",
 			
 			//String - Scale label font colour	
 			scaleFontColor : "#666",	
 			
 			///Boolean - Whether grid lines are shown across the chart
 			scaleShowGridLines : true,
 			
 			//String - Colour of the grid lines
 			scaleGridLineColor : "rgba(0,0,0,.05)",
 			
 			//Number - Width of the grid lines
 			scaleGridLineWidth : 1,	
 			
 			//Boolean - Whether the line is curved between points
 			bezierCurve : true,
 			
 			//Boolean - Whether to show a dot for each point
 			pointDot : true,
 			
 			//Number - Radius of each point dot in pixels
 			pointDotRadius : 3,
 			
 			//Number - Pixel width of point dot stroke
 			pointDotStrokeWidth : 1,
 			
 			//Boolean - Whether to show a stroke for datasets
 			datasetStroke : true,
 			
 			//Number - Pixel width of dataset stroke
 			datasetStrokeWidth : 2,
 			
 			//Boolean - Whether to fill the dataset with a colour
 			datasetFill : true,
 			
 			//Boolean - Whether to animate the chart
 			animation : true,

 			//Number - Number of animation steps
 			animationSteps : 60,
 			
 			//String - Animation easing effect
 			animationEasing : "easeOutQuart",

 			//Function - Fires when the animation is complete
 			onAnimationComplete : null
 			
 		}
    
    new Chart(ctx).Line(data,options);
}
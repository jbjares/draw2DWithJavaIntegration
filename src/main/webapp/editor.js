
function foodLevel(){
  var foodLevel = 0.2; 
  return foodLevel;
}

/**Dont change it in this version**/
function age(){
  var age = 0.0;
  return age;
}
function temperature(){
  var temperature = 50.0;
  return temperature;
}
function oxygenLevel(){
  var oxygenLevel = 0.5;
  return oxygenLevel;
}
/** comment **/
function reproductionRate(temperature){
  var reproductionRate = 0.5;
  reproductionRate = reproductionRate+(temperature/100);
  return reproductionRate;
}

function size(age,temperature,foodLevel,oxygenLevel){+finalAge)/4;
    size = (Math.pow(finalAge,2))+size;
/**
    size = size--age+temperature+foodLevel

    var size = 1;
    var finalAge = 10.0;
    var k = age+temperature+foodLevel;
Math.pow(4,3)
    k = k+finalAge/4;
    Double length = (Math.pow(finalAge,2)+k);
**/
if(oxygenLevel>=0.5){
      size = size*2;
    }else{
      size = size/2;
    } 

    return size;
}

size(age(),temperature(),foodLevel(),oxygenLevel());

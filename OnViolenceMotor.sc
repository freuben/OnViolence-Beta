OnViolenceMotor {var s, <out, basicPath, motor, sensor, <>lowVal, <>highVal, <>leftVal, <>rightVal, bendLow, bendHigh, newMacroArray1, newMacroArray2, arrA, arrB, pivot1, pivot2, step1, step2, thisArray1, thisArray2, <>direction1, <>direction2, oscNode, sensorVal1, sensorVal2, <>sensorLag1, <>sensorLag2, whichPedal, noteLow, noteHigh, <>volMotor, <synth1, <synth2, <>sensorWin, <slider, <window, <>panMotor; 
		
	*new {arg outBus=0, vol=1, panMotor=0, lowVal=43.276000976562, highVal=46.995998382568, leftVal=41.668201446533, rightVal=46.628700256348, pathName, rectArr;
		^super.new.initOnViolenceMotor(outBus, vol, panMotor, lowVal, highVal, leftVal, rightVal, pathName, rectArr);
	}
	
	initOnViolenceMotor {arg outBus, vol, pan, valLow, valHigh, valLeft,valRight,pathName, rectArr;
		var sensorPitch;
		out = outBus;
		volMotor = vol;
		panMotor = pan;
		lowVal = valLow;
		highVal = valHigh;
		leftVal = valLeft;
		rightVal= valRight;
		basicPath = pathName;
		basicPath = basicPath ?? { Document.standardizePath("~/Library/Application Support/SuperCollider/Extensions/FedeClasses/OnViolence/"); };

		s = Server.default;
		sensor = NodeProxy.control(s, 1);
		motor = NodeProxy.audio(s, 2);
		motor.play(out);
		
		bendLow = 0;
		bendHigh = 0;
		
		sensorPitch = (basicPath ++ "/data/sensorPitch.rtf").loadPath;
		newMacroArray1 = sensorPitch[0];	
		newMacroArray2 = sensorPitch[1];
		
		this.readyToStart;
		this.oscRespNode(rectArr);
 
	}
	
	readyToStart {

	sensor.put(0, \lagging, 0, [\num, lowVal, \low, lowVal, \high, highVal, \id, 4, \lag, 0.5/4]);
	sensor.put(1, \lagging, 0,  [\num, rightVal, \low, leftVal, \high, rightVal, \id, 5, \lag, 0.8/4]);

	sensorLag1 = sensor.objects[0];
	sensorLag2 = sensor.objects[1];
	
	}
	
	findPivot1 {arg count; 
		if(arrA[count][2] == 0, {
			pivot1 = arrA[count][0].maxItem;
			},{
			pivot1 = arrA[count][0].minItem;
		}); 
	}
	
	findPivot2 {arg count; 
		if(arrB[count][2] == 0, {
			pivot2 = arrB[count][0].maxItem;
			},{
			pivot2 = arrB[count][0].minItem;
		}); 
	}

	pedalOn {arg num=0;
		
		this.pedalLag(num);
		
		arrA = newMacroArray1[num-1];
		arrB = newMacroArray2[num-1];
		
		whichPedal = 1;
		
		step1 = 0;
		step2 = 0;
		
		this.findPivot1(step1);
		this.findPivot2(step2);
		
		thisArray1 = arrA[step1];
		thisArray2 = arrB[step2];
		
		direction1 = arrA[step1][2];
		direction2 = arrB[step2][2];
		
		if(direction1 == 0, {
		noteLow = thisArray1[1][0];
		motor.put(0, \motor, 0, [\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volMotor, \pan, panMotor]);
		synth1 =  motor.objects[0];
		}, {
		noteLow = thisArray1[1][1];
		motor.put(0, \motor, 0, [\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volMotor, \pan, panMotor]);
		synth1 =  motor.objects[0];
		});
		
		if(direction2 == 0, {
		noteHigh = thisArray2[1][0];
		motor.put(1, \motor, 0, [\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volMotor, \pan, panMotor]);
		synth2 =  motor.objects[1];
		}, {
		noteHigh = thisArray2[1][1];
		motor.put(1, \motor, 0, [\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volMotor, \pan, panMotor]);
		synth2 =  motor.objects[1];
		});
	}	

	pedalOff {
		 whichPedal = 0;
		 synth1.set(\gates, 0);
		 synth2.set(\gates, 0);
	} 

	sensor1 {arg val;
		var val2;
		
		if(val == 63, {val2 = 64}, {val2 = val});
		
		if(whichPedal == 1, {
		if(direction1 == 0, {	
			if(val2 >= pivot1, {	
				step1 = step1 + 1;
				if( arrA[step1].notNil, {
					direction1 = arrA[step1][2];
					thisArray1 = arrA[step1];
					this.sliderPivot;
					this.findPivot1(step1);
				});
			});
		}, {
			if(val2 <= pivot1, {		
				step1 = step1 + 1;
				if( arrA[step1].notNil, {
					direction1 = arrA[step1][2];
					thisArray1 = arrA[step1];
					this.sliderPivot;
					this.findPivot1(step1);
				});
			});
		});
		
		noteLow = val.linexp(thisArray1[0][0],thisArray1[0][1],thisArray1[1][0],thisArray1[1][1]);
		synth1.set(\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320););
		});
	
	}

	sensor2 {arg val;
		var val2;
		
		if(val == 63, {val2 = 64}, {val2 = val});
		
		if(whichPedal == 1, {
		if(direction2 == 0, {	
			if(val2 >= pivot2, {	
				step2 = step2 + 1;
				if( arrB[step2].notNil, {
					direction2 = arrB[step2][2];
					thisArray2 = arrB[step2];
					this.findPivot2(step2);
				});	
			});
		}, {
			if(val2 <= pivot2, {	
				step2 = step2 + 1;
				if( arrB[step2].notNil, {
					direction2 = arrB[step2][2];
					thisArray2 = arrB[step2];
					this.findPivot2(step2);
				});	
			});
		});
		
		noteHigh = val.linexp(thisArray2[0][0],thisArray2[0][1],thisArray2[1][0],thisArray2[1][1]);
		synth2.set(\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320););
		});
	}

	bend1 {arg val;
 		bendLow = val.linlin(0,127, -0.5,0.5);
		if(whichPedal == 1, {synth1.set(\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320));}); 
	}

	bend2 {arg val;
		bendHigh = val.linlin(0,127, -0.5,0.5);
		if(whichPedal == 1, {synth2.set(\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320));});
	}
		
	pedalLag {arg numPedal=1;	
		var lagArray;
		//pedal lags
		lagArray = [[0.5, 0.8], [0.45714285714286, 0.71428571428571], [0.41428571428571, 0.62857142857143], [0.37142857142857, 0.54285714285714], [0.32857142857143, 0.45714285714286], [0.28571428571429, 0.37142857142857], [0.24285714285714, 0.28571428571429], [0.2, 0.2]]/4;
			
		case 
		{numPedal < 5} {
		sensorLag1.set(\lag, lagArray[0][0]);
		sensorLag2.set(\lag, lagArray[0][1]);
		}
		{(numPedal >= 5).and(numPedal < 12)} {
		sensorLag1.set(\lag, lagArray[numPedal-4][0]);
		sensorLag2.set(\lag, lagArray[numPedal-4][1]);
		};
		
		}
	
	oscRespNode {arg rectArr;
	var cambio, oldNum1 = 0, oldNum2 = 0;
	
	rectArr ?? {rectArr = [-40, 260, 30, 360]};
	window = Window("Sensor", (rectArr+40).asRect, border:false).background_(Color.white);
	sensorWin = window.front;
	sensorWin.alwaysOnTop = true;
	slider = LevelIndicator(sensorWin, Rect(20,sensorWin.bounds.asArray.last-rectArr.last/2,rectArr[2],rectArr[3]));
	slider.numTicks = 3;
	slider.numMajorTicks = 3; 

	//OSCresponderNode listens to id 4 and 5 
	oscNode = OSCresponderNode(s.addr,'/tr',{arg time,responder,msg;
		
		case
		{msg[2] == 4} {
		if(oldNum1 != msg[3], {
		sensorVal1 = msg[3];
		{slider.value = sensorVal1.linlin(0,127,0,1);}.defer;
		this.sensor1(sensorVal1);
		this.sensor2(sensorVal1);
		oldNum1 = msg[3]
		});
		}
	{msg[2] == 5} {
		if(oldNum2 != msg[3], {
		if(oldNum2 != msg[3], {
		if([0,1,2,3].includes(msg[3].linlin(0,127,127,0)), {cambio = [0,1].choose; 'cambio cambio'.postln}); 
		if(cambio == 0, {
		sensorVal2 = msg[3].linlin(0,127,0, 63).round(1);
		this.bend1(sensorVal2);
		this.bend2(sensorVal2);
		}, {
		sensorVal2 = msg[3].linlin(0,127,127,64).round(1);
		this.bend1(sensorVal2);
		this.bend2(sensorVal2);
		});
		oldNum2 = msg[3]});
		});
		}
	}).add;	
	}
	
	sliderPivot {
	{
	slider.critical = 1;
	0.15.yield;
	slider.critical = 0;
	}.fork(AppClock);	
	}
	
	vol {arg amp=1;
	motor.set(\amp, amp);	
	}
	
	closeWin {
	if(window.notNil, {window.close});	
	}
	
	hideWin {
	if(window.notNil, {window.visible = false});	
	}
	
	showWin {
	if(window.notNil, {window.visible = true});		
	}

	*initClass {
	
	SynthDef.writeOnce(\motor, {arg freq=440, amp=0.2, out=0, gates=1, lag=0.1, globamp=1.0, pan=0;
	var signal;
	signal = RLPF.ar(LFPulse.ar(freq/16, 0.2), 100, 0.1).clip2(0.4) 
	* EnvGen.kr(Env.asr(0.1,1,0.3), gates, doneAction: 2);
	Out.ar(out, Pan2.ar((signal*0.8), pan, amp*globamp));
	});
	
	SynthDef.writeOnce(\lagging,{arg num=45, rate=500, low=20, high=60, id=0, lag=0.2;
	var values;
	values = Ramp.kr(num.linlin(low, high, 0, 127), lag).round(1);
	SendTrig.kr(Impulse.kr(rate),id,values);
	});
	
	}

}
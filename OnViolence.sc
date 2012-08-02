OnViolence {var <>audioIn, s, <>ampIn, midiOut, funcRec, d, newMacroArray, newMacroArray2, amplitude, <>tempo=176, countGlob, <>stepGlob, notes, <>pedalOffGlob, <>pedalOnGlob, gap, <>piano, osc, oscMax, <>sensorVal1=0, <>sensorVal2=63, <>grito1, <>grito2,<>grito3,<>grito4, <>motor, <>metal, <>wagner1,<>wagner2, <>wagner3,<>wagner4,<>buxtahude,<>parsifal, pedalOn, pedalOff, tempoLock, metalOn, sensor, sensor1, sensor2, sensorLag1, sensorLag2, bend1, bend2, lowVal=43.276000976562, highVal=46.995998382568, leftVal=41.668201446533,basicPath, rightVal=46.628700256348, <>algoVersion, <>rrandArray, differ, tempoStart=0,stepTempo=0,wagner1MIDITime, wagner1MIDINotes, wagner1MIDIVel, wagner1MIDIEnd,wagner2MIDITime, wagner2MIDINotes, wagner2MIDIVel, wagner2MIDIEnd,wagner3MIDITime, wagner3MIDINotes, wagner3MIDIVel, wagner3MIDIEnd,wagner4MIDITime, wagner4MIDINotes, wagner4MIDIVel, wagner4MIDIEnd,document, <>bufferArr, <>randBuffArr, <>volArr, <>panArr;
	
	*new {arg audioIn = 0, volArr, panArr;
		^super.new.initOnViolence(audioIn, volArr, panArr);
	}

	initOnViolence {arg in, arrVol, arrPan;
		s = Server.default;
		audioIn = in;
		
		volArr = arrVol;
		volArr ?? {volArr = [1.0, 1.5, 0.5, 0.9, 1.0, 1.0, 0.2, 0.2, 0.2, 0.2, 1.0, 0.9];}; //volMetal, volScream1, volScream2, volScream3, volScream4, volMotor, volWagner1, volWagner2, volWagner3, volWagner4, volBuxta, volParsifal
		
		panArr = arrPan;
		panArr ?? {panArr = [0, -0.5, 0.5, 0, -1, 1, 0.25, -0.75, -0.25, 0.75, 0, 0];}; //panMetal, panScream1, panScream2, panScream3, panScream4, panMotor, panWagner1, panWagner2, panWagner3, panWagner4, panBuxta, panParsifal
		
		basicPath =  Document.standardizePath("~/Library/Application Support/SuperCollider/Extensions/FedeClasses/OnViolence/");
		
		s.makeBundle(nil, {
		bufferArr = [
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/metal/metal_bang.scpv"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/metal/metal_bang2.scpv"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/metal/metal_bang3.scpv"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/scream/hitlerSpeech.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/scream/screaming.scpv", 0, (9.37*2)*44100),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_kom.scpv"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_komkom.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_gate.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_shepardspipe.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_gatetrans1.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_gatetrans2.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_transformation.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_nun_achte_wohl.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_heho.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_sol_ich_lauchen.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_preludeII.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_ihr_schonen_kinder.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_lausch.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_van_bade_kehrt.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_preludeII.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_auf_as_tan.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_konig_merke_hei.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/tristan_verrater_ha.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_vergeh.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_weh_hoho.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_met_diesem.aif"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/buxtahude/buxtahude_left.scpv"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/buxtahude/buxtahude_right.scpv"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_actIII_left.scpv"),
		Buffer.read(s, basicPath ++ "/OnViolenceSamples/wagner/parsifal_actIII_right.scpv")
		];		
		
		//algoscore
		
		//sensorPitch = (basicPath ++ "/data/sensorPitch.rtf").loadPath;		
//		newMacroArray = sensorPitch[0];
//
//		newMacroArray2 = sensorPitch[1];
		
		
		rrandArray = Array.fill(124, {rrand(1,10)}); //select random samples;
		
		s.sync;
		
		"Buffers Allocated".postln;
		
		this.selectFiles(Array.fill(26, {rrand(1,10)}););
		
		});
		
	}
	
	selectFiles {arg arrayVersion;
		var sampleNumbers, sampleVersion, pathFolder, condition;
		pathFolder = basicPath ++ "/OnViolenceSamples/randomSamples/";
		sampleNumbers = [81, 85, 88, 89, 93, 98, 99, 102, 104, 107, 108, 110, 111, 112, 116, 117, 118, 120, 121, 122, 123]; 
		algoVersion = arrayVersion; //26 items
		sampleVersion = algoVersion.copyRange(0, 20);
		sampleVersion.do({|item, index| rrandArray.put(sampleNumbers[index]-1, item)});
		
		condition = Condition.new(true);
		randBuffArr = Array.fill(rrandArray.size,0);
		
		{
		"Loading Random Buffers".postln;
		rrandArray.do({|item,index| 
		var path;
		
		condition.test = false;
		condition.signal;
		
		path = (pathFolder ++ (index+1).asString ++ "ruidito" ++ item.asString ++ ".scpv");
			
		if((index+1) != 50, {
		".".post;
	
		randBuffArr[index] = Buffer.read(s, path, 
		
		action: {	
		condition.test = true;
		condition.signal;
		}
		);
		},{	
		condition.test = true;
		condition.signal;
		});
		
		condition.wait;
		
		});
		"\rBuffers Allocated".postln;
		}.fork;

	}
	
	funcPlay {arg soundFile = 1, time=0.1, rate = 1, amp = 1.0;
	var ampScream, scream, ratioDown = 0.7, adjScream;
	ampScream = amp.linlin(0,0.1,0.25,0.75);
	scream = ampScream.nearestInList([ 0.25, 0.35, 0.41666666666667, 0.5, 0.55, 0.75 ]);
	adjScream = (soundFile-70).linlin(1,53,1,3.0);
	
		case 
		{([10, 25, 38, 44] ++ (51,52..70) ++ [74, 78, 83, 84, 90, 91, 92, 95, 96, 97, 100, 101, 119]).includes(soundFile)} {
		{
	(time*rate).yield;
	grito4.spawn([\recBuf, randBuffArr[soundFile-1].bufnum, \rate, rate, \amp, scream*volArr[4], \thresh, 0.5, \ratioDown, ratioDown, \ratioUp, 0.9, \gates, 1, \pan, panArr[4]]);  //pvplayMono
	}.fork;
		}
		{[1, 71].includes(soundFile)} {
		{
	(time*rate).yield;
	grito3.spawn([\recBuf, randBuffArr[soundFile-1].bufnum, \rate, rate, \amp, 0.9*volArr[3], \thresh, 0.5, \ratioDown, 0.6, \ratioUp, 0.9, \pan, panArr[3]]);  //pvplayBet
	}.fork;
		}
		{[2,4,7,11,15,20,36,72,79,82,88,104,107,113, 120].includes(soundFile)} {
		{
	(time*rate).yield;
	grito3.spawn([\recBuf, randBuffArr[soundFile-1].bufnum, \rate, rate, \amp, scream*volArr[3]*adjScream, \thresh, 0.5, \ratioDown, 0.6, \ratioUp, 0.9, \pan, panArr[3]]);  //pvplayBet
	}.fork;
		}
		{[8,14,18,21,27,29,30,31,33,75,76].includes(soundFile)} {
		{
	(time*rate).yield;
	grito2.spawn([\recBuf, randBuffArr[soundFile-1].bufnum, \rate, rate, \amp, scream*volArr[2]*adjScream, \thresh, 0.5, \ratioDown, ratioDown, \ratioUp, 0.9, \recBuf2, bufferArr[4].bufnum, \jefeBuf, bufferArr[3].bufnum, \startPos, rrand(0, 820865), \osciOff, rrand(1.25, 0.75), \startOff, rrand(0,403)], \pan, panArr[3]);  //pvplayGrito
	}.fork;
		
		}
		{[80,86,89,105,121,123].includes(soundFile)} {
		{
	(time*rate).yield;
	grito2.spawn([\recBuf, randBuffArr[soundFile-1].bufnum, \rate, rate, \amp, scream*volArr[2]*adjScream, \thresh, 0.5, \ratioDown, ratioDown, \ratioUp, 0.9, \recBuf2, bufferArr[5].bufnum, \jefeBuf, bufferArr[3].bufnum, \startPos, rrand(0, 820865), \osciOff, rrand(1.25, 0.75), \startOff, rrand(0,2021), \pan, panArr[2]]);  //pvplayGrito wagner/jefe
	}.fork;
		
		}
		{[94,99,103,106,108,109,111,112,114,115,116,117,118].includes(soundFile)} {
		{
	(time*rate).yield;
	grito2.spawn([\recBuf, randBuffArr[soundFile-1].bufnum, \rate, rate, \amp, scream*volArr[2]*adjScream, \thresh, 0.5, \ratioDown, ratioDown, \ratioUp, 0.9, \recBuf2, bufferArr[5].bufnum, \jefeBuf, bufferArr[6].bufnum, \startPos, rrand(0, 2072700), \osciOff, rrand(1.25, 0.75), \startOff, rrand(0,2021), \pan, panArr[2]]);  //pvplayGrito wagner/wagner
	}.fork;
		
		}
		{[3,5,6,9,12,13,16,17,19,22,23,24,26,28,32,34,35,37,39,40,41,42,43,45,46,47,48,49,73,77,81,85,87,93,98,102,110,122,124].includes(soundFile)} {
		{
	(time*rate).yield;
	grito1.spawn([\recBuf, randBuffArr[soundFile-1].bufnum, \rate, rate, \amp, scream*volArr[1]*adjScream, \thresh, 0.5, \ratioDown, ratioDown, \ratioUp, 0.9, \pan, panArr[1]]);  //pvplayVoc
	}.fork;
		};
	
	}
		
	arrayTimes {var arr, b, c, newEinTimes, something, neew;
		
	arr = (0,1..254)*(60/176);
	
	b = [ 0.01140589569161, 2.796485260771, 2.944126984127, 3.2375736961451, 7.1833560090703,11.47, 13.908616780045, 14.24, 16.49, 16.95, 18.120, 18.855, 19.485, 21.24, 23.505, 24.150, 24.86, 25.625, 26.17, 28.9, 31.27, 32.03, 36.93, 42.5, 47.535, 53.48, 55.47, 56.5, 59.35, 60.155, 60.66, 61.475, 62.06, 62.75, 66.16, 67.605, 68.485, 70.805, 72.57, 79.195, 85.285];
	
	c = b.nearestInMinList(arr);
	
	d = b - c;
	
	d[1] = d[1] + 0.34090909090909;
	
	d = d ++ [d[22], d[23], d[24], d[25], d[39], d[40], d[22], d[23], 0];
	
	//times for second section
	newEinTimes = #[0.3, 0.645, 0.86, 3.6, 3.935, 5.34, 5.965, 7.71, 9.64, 10.135, 11.1, 13.515, 13.955, 15.64, 16.31, 16.905, 17.05, 18.355, 19.855, 20.29, 20.72, 21.055, 21.67, 24.83, 26.4, 27.02, 27.34, 28.38, 29.435, 30.99, 31.575, 35.105, 37.225, 38.825, 39.19, 39.53, 40.525, 41.05, 41.46, 42.48, 43.16, 43.89, 44.425, 45.13, 45.98, 48.545, 50.2, 54.485, 56.14, 59.015, 59.46, 61.445, 63.78, 65.125, 69.295, 74.745];
	
	something = (0,1..230)*(60/176);
	neew = newEinTimes.nearestInMinList(something);
	
	differ = newEinTimes-neew;
	
	}
	
	startNodes {arg pianoOut = 0, metalOut=0, grito1Out = 0, grito2Out = 0, grito3Out=0, grito4Out=2, motorOut=3, wagner1Out=0, wagner2Out=0,wagner3Out=0,wagner4Out=0,buxtaOut=0, parsifalOut=0;
		
	piano = NodeProxy.audio(s, 2);
	piano.play(pianoOut);
	
	metal = NodeProxy.audio(s, 2);
	metal.play(metalOut); 
	
	grito1 = NodeProxy.audio(s, 2);
	grito1.play(grito1Out);
	
	grito2 = NodeProxy.audio(s, 2);
	grito2.play(grito2Out);
	
	grito3 = NodeProxy.audio(s, 2);
	grito3.play(grito3Out);
	
	grito4 = NodeProxy.audio(s, 2);
	grito4.play(grito4Out);
	
	wagner1 = NodeProxy.audio(s, 2);
	wagner1.play(wagner1Out); 
	
	wagner2 = NodeProxy.audio(s, 2);
	wagner2.play(wagner2Out);
	
	wagner3 = NodeProxy.audio(s, 2);
	wagner3.play(wagner3Out); 
	
	wagner4 = NodeProxy.audio(s, 2);
	wagner4.play(wagner4Out); 
	
	buxtahude = NodeProxy.audio(s, 2);
	buxtahude.play(buxtaOut);
	
	parsifal = NodeProxy.audio(s, 2);
	parsifal.play(parsifalOut);
	
	//motor = NodeProxy.audio(s, 1);
	//motor.play(motorOut); 
	//
	//sensor = NodeProxy.control(s, 1);
	
	this.arrayTimes;
	}

	startMIDI {var step2, step3, findPivot, findPivot2, pivot, pivot2, thisArray, thisArray2, direction, direction2,synth1, synth2, a, b, noteLow, noteHigh, bendLow, bendHigh,whichPedal; 
	var count = 0, oldX=0, oldY=0, first;
	var pedalOffOld, pedalOnOld,pedalSwitch; 
		
	bendLow = 0;
	bendHigh = 0;
	
	
	//if(midiOut.notNil, {
	//midiOut.control(0,81,63);
	//midiOut.control(0,82,63);
	//});
	
	findPivot = {arg count; 
		if( a[count][2] == 0, {
			pivot = a[count][0].maxItem;
			},{
			pivot = a[count][0].minItem;
		}); 
	};
	
	findPivot2 = {arg count; 
		if( b[count][2] == 0, {
			pivot2 = b[count][0].maxItem;
			},{
			pivot2 = b[count][0].minItem;
		}); 
	};
	
	pedalOn = {arg glissStep=0;
			a = newMacroArray[(glissStep-1).postln];
			b = newMacroArray2[glissStep-1];
			
			whichPedal = 1;
			
			step2 = 0;
			step3 = 0;
			
			findPivot.value(step2);
			findPivot2.value(step3);
			
			thisArray = a[step2];
			thisArray2 = b[step3];
			
			direction = a[step2][2];
			direction2 = b[step3][2];
			
			if(direction == 0, {
			noteLow = thisArray[1][0];
			motor.put(0, \motor, 0, [\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volArr[5]]);
			synth1 =  motor.objects[0];
			//if(midiOut.notNil, {
	//		midiOut.control(0,83,thisArray[0][0]);
	//		});
			}, {
			noteLow = thisArray[1][1];
			motor.put(0, \motor, 0, [\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volArr[5]]);
			synth1 =  motor.objects[0];
			//if(midiOut.notNil, {
	//		midiOut.control(0,83,thisArray[0][1]);
	//		});
			});
			
			if(direction2 == 0, {
			noteHigh = thisArray2[1][0];
			motor.put(1, \motor, 0, [\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volArr[5]]);
			synth2 =  motor.objects[1];
			//if(midiOut.notNil, {
	//		midiOut.control(0,84,thisArray2[0][0]);
	//		});
			}, {
			noteHigh = thisArray2[1][1];
			motor.put(1, \motor, 0, [\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320), \out, 0, \amp, volArr[5]]);
			synth2 =  motor.objects[1];
			//if(midiOut.notNil, {
	//		midiOut.control(0,84,thisArray2[0][1]);
	//		});
			});
	};	
	
	pedalOff = {
		 whichPedal = 0;
		 synth1.set(\gates, 0);
		 synth2.set(\gates, 0);
	}; 
	
	sensor1 = {arg val;
			var val2;
			if(val == 63, {val2 = 64}, {val2 = val});
			if(whichPedal == 1, {
			if(direction == 0, {	
				if(val2 >= pivot, {	
					
					step2 = step2 + 1;
					
					if( a[step2].notNil, {
						direction = a[step2][2];
						thisArray = a[step2];
						'PIVOT1'.postln;
						findPivot.value(step2);
					});
					
					
				});
			}, {
			
				if(val2 <= pivot, {	
					
					step2 = step2 + 1;
					
					if( a[step2].notNil, {
						direction = a[step2][2];
						thisArray = a[step2];
						'PIVOT1'.postln;
						findPivot.value(step2);
					});
		
					
				});
			});
			
			noteLow = val.linexp(thisArray[0][0],thisArray[0][1],thisArray[1][0],thisArray[1][1]);
			synth1.set(\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320););
			});
		
	};
	
	sensor2 = {arg val;
			var val2;
			if(val == 63, {val2 = 64}, {val2 = val});
			
				if(whichPedal == 1, {
			if(direction2 == 0, {	
				if(val2 >= pivot2, {	
					
					step3 = step3 + 1;
					
					if( b[step3].notNil, {
						direction2 = b[step3][2];
						thisArray2 = b[step3];
						'PIVOT2'.postln;
						findPivot2.value(step3);
					});
					
					
				});
			}, {
			
				if(val2 <= pivot2, {	
					
					step3 = step3 + 1;
					
					if( b[step3].notNil, {
						direction2 = b[step3][2];
						thisArray2 = b[step3];
						'PIVOT2'.postln;
						findPivot2.value(step3);
					});
		
					
				});
			});
			
			noteHigh = val.linexp(thisArray2[0][0],thisArray2[0][1],thisArray2[1][0],thisArray2[1][1]);
			synth2.set(\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320););
			});
	};
	
	bend1 = {arg val;
	 	bendLow = val.linlin(0,127, -0.5,0.5);
		if(whichPedal == 1, {synth1.set(\freq, (noteLow+bendLow).midicps.expexp(110, 1760, 55, 56320));}); 
	};
	
	bend2 = {arg val;
		bendHigh = val.linlin(0,127, -0.5,0.5);
		if(whichPedal == 1, {synth2.set(\freq, (noteHigh+bendHigh).midicps.expexp(110, 1760, 55, 56320));});
	};


	MIDIIn.program = { arg src, chan, prog; 
	
	//pedals (prog0 is pedalOff, prog5 is pedalOn for beringer) (prog1 is pedalOff, prog2 is pedalOn for eobody)
	
	case
	{prog == 1} {
	//counts each time pedalOff is played and stores a value for swiching between pedals
	pedalOffGlob = pedalOffGlob + 1;
	//makes sure if you press pedalOff twice that it ignores the pedal function
	if(pedalOffGlob != pedalOffOld, {
	'PedalOff '.post; pedalOffGlob.postln; 
	this.pedalOffFunc; 
	});
	pedalOffOld = pedalOffGlob;	
	pedalSwitch = 0;

	//stepTempo is so that the computer waits 6.2 secs to start tracking tempo
	if(stepTempo == 0, {Routine({1.do({6.2.yield; tempoStart = 1;
	//start taking the tempo for metal bangs
	piano.set(\micOn, 0, \impOn, 1, \initImpOn, 0);
	"START TEMPO TRACKING".postln;
	});}).play });
	if(stepTempo < 2, {stepTempo  = (stepTempo + 1).postln});}
	
	{prog == 2} {
	//counts each time pedalOff is played 
	//the boolean is so that when pedalOff is pressed before, then it counts, otherwise it doesn't.
	//at pedalOn 16 it ignores this rule 
	if((pedalSwitch == 0).or(pedalOnGlob == 16), {
	pedalOnGlob = pedalOnGlob + 1;
	});
	//makes sure if you press pedalOn twice that it ignores the pedal function
	if(pedalOnGlob != pedalOnOld, {
	'PedalOn '.post; pedalOnGlob.postln;
	this.pedalOnFunc;
	});
	pedalOnOld = pedalOnGlob;
	pedalSwitch = 1;
	
	};
	
	//reads pedal numbers and arranges them
	case
	{(0..16).includes(pedalOnGlob)} {pedalOffGlob = pedalOnGlob}
	{(17..70).includes(pedalOnGlob)} {pedalOffGlob = pedalOnGlob-1};

	};

	//midi controller instead of sensors
	//MIDIIn.control = { arg src, chan, num, val; 
	//
	//	if( num == 27, {sensor.set(\lag, val.linlin(0,127,0.2,0.8).postln;)});
	//	
	//};
	
	}
	
	keyboardPedal {var pedalOnOld, pedalOffOld,pedalSwitch;
	
	 document = Document.new("");
	 document.keyDownAction_({arg doc, key, modifiers, keycode;
	
	//replace keyboard letters a and q for pedals
	case
	{keycode == 97} {
	//counts each time pedalOff is played and stores a value for swiching between pedals
	pedalOffGlob = pedalOffGlob + 1;
	//makes sure if you press pedalOff twice that it ignores the pedal function
	if(pedalOffGlob != pedalOffOld, {
	'PedalOff '.post; pedalOffGlob.postln; 
	this.pedalOffFunc; 
	});
	pedalOffOld = pedalOffGlob;	
	pedalSwitch = 0;

	//stepTempo is so that the computer waits 6.2 secs to start tracking tempo
	if(stepTempo == 0, {Routine({1.do({6.2.yield; tempoStart = 1;
	//start taking the tempo for metal bangs
	piano.set(\micOn, 0, \impOn, 1, \initImpOn, 0);
	"START TEMPO TRACKING".postln;
	});}).play });
	if(stepTempo < 2, {stepTempo  = (stepTempo + 1).postln});}
	
	{keycode == 113} {
	//counts each time pedalOff is played 
	//the boolean is so that when pedalOff is pressed before, then it counts, otherwise it doesn't.
	//at pedalOn 16 it ignores this rule 
	if((pedalSwitch == 0).or(pedalOnGlob == 16), {
	pedalOnGlob = pedalOnGlob + 1;
	});
	//makes sure if you press pedalOn twice that it ignores the pedal function
	if(pedalOnGlob != pedalOnOld, {
	'PedalOn '.post; pedalOnGlob.postln;
	this.pedalOnFunc;
	});
	pedalOnOld = pedalOnGlob;
	pedalSwitch = 1;
	
	};
	
	//reads pedal numbers and arranges them
	case
	{(0..16).includes(pedalOnGlob)} {pedalOffGlob = pedalOnGlob}
	{(17..70).includes(pedalOnGlob)} {pedalOffGlob = pedalOnGlob-1};

	});
	
	}	
	
	keyboardPedalOff {
	document.keyDownAction_(nil);
	document.close;
	}
	
	pedalOnFunc {	
		//pedal up
	
		piano.set(\lock, 1);
		tempoLock = 1;
		gap = 1;
		
		case
		{pedalOnGlob < 16} {pedalOn.value(pedalOnGlob);}
		{(pedalOnGlob >= 17).and(pedalOnGlob <= 36)} {pedalOn.value(pedalOnGlob-1);};
		//send tempo to computer2
//		{pedalOnGlob >= 37} {midiOut.songPtr( tempo )};
		
		case 
		{pedalOnGlob == 5} {
		sensorLag1.set(\lag, 0.45714285714286);
		sensorLag2.set(\lag, 0.71428571428571);
		}
		{pedalOnGlob == 6} {
		sensorLag1.set(\lag, 0.41428571428571);
		sensorLag2.set(\lag, 0.62857142857143);
		}
		{pedalOnGlob == 7} {
		sensorLag1.set(\lag, 0.37142857142857);
		sensorLag2.set(\lag, 0.54285714285714);
		}
		{pedalOnGlob == 8} {
		sensorLag1.set(\lag, 0.32857142857143);
		sensorLag2.set(\lag, 0.45714285714286);
		}
		{pedalOnGlob == 9} {
		sensorLag1.set(\lag, 0.28571428571429);
		sensorLag2.set(\lag, 0.37142857142857);
		}
		{pedalOnGlob == 10} {
		sensorLag1.set(\lag, 0.24285714285714);
		sensorLag2.set(\lag, 0.28571428571429);
		}
		{pedalOnGlob == 11} {
		sensorLag1.set(\lag, 0.2);
		sensorLag2.set(\lag, 0.2);
		}
		{pedalOnGlob == 17} {
		stepGlob = 51;
		grito4.set(\gates, 1, \dec, sensorVal2.linlin(0,127,0.4,0.9));
		this.funcPlay(stepGlob.postln, 0, (tempo/176), (amplitude));
		}
		{(pedalOnGlob > 17).and(pedalOnGlob <= 36)} {
		stepGlob = stepGlob + 1;
		grito4.set(\gates, 1, \dec, sensorVal2.linlin(0,127,0.4,0.9));
		this.funcPlay(stepGlob.postln, 0, (tempo/176), (amplitude));
		}
		{pedalOnGlob == 38} {
		stepGlob = 81;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		}
		{pedalOnGlob == 39} {
		stepGlob = 85;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(2*(60/176))), (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(3*(60/176))), (tempo/176), (amplitude));		}
		{pedalOnGlob == 40} {
		stepGlob = 88;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(5*(60/176))), (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(6*(60/176))), (tempo/176), (amplitude));		}
		{pedalOnGlob == 41} {
		stepGlob = 93;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		}
		{pedalOnGlob == 42} {
		stepGlob = 98;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(3*(60/176))), (tempo/176), (amplitude));
		}
		{pedalOnGlob == 43} {
		stepGlob = 102;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		}
		{pedalOnGlob == 44} {
		stepGlob = 104;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(1*(60/176))), (tempo/176), (amplitude));
		}
		{pedalOnGlob == 45} {
		stepGlob = 107;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(2*(60/176))), (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(3*(60/176))), (tempo/176), (amplitude));
		
		//trigger wagner 
		wagner1MIDITime = [ 0, 0.47406, 0.98773, 1.44011, 1.92125, 2.4123, 2.62227, 2.85021, 3.07428, 3.33516, 3.56156, 3.79795, 4.01052, 4.27658, 4.48633, 4.71209, 4.92802, 5.18882, 5.4036, 5.63429, 5.85886, 6.12091, 6.59246, 7.06605, 8.02378, 8.93659, 10.76226, 12.70385, 14.07918, 14.30404, 14.58499, 15.48375 ];
wagner1MIDINotes = [ 62, 67, 67, 67, 67, 65, 67, 65, 63, 65, 63, 62, 65, 63, 65, 63, 62, 63, 62, 60, 63, 62, 55, 67, 69, 70, 69, 67, 66, 64, 66, 67 ];
wagner1MIDIVel = [ 59, 70, 68, 73, 67, 67, 70, 67, 68, 70, 71, 70, 63, 73, 62, 67, 68, 73, 68, 66, 68, 77, 73, 78, 76, 80, 70, 73, 74, 67, 73, 77 ];
wagner1MIDIEnd = 0.96253;
		Routine({1.do({
			(1.0227272727273*(176/tempo)).yield;
			this.wagnerAlgo(wagner1,11,panArr[6],wagner1MIDITime*(176/tempo), wagner1MIDINotes,wagner1MIDIVel,wagner1MIDIEnd,7,0,12,(volArr[6]*0.8)); //starts wagner pharse2, 7 mult,startPos 0,midiOffset 12, and tristan_prelude buffer. tenor_phrase2
			});}).play;
		}
		{pedalOnGlob == 46} {
		stepGlob = 110;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(2*(60/176))), (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(4*(60/176))), (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(6*(60/176))), (tempo/176), (amplitude));		}
		{pedalOnGlob == 47} {
		stepGlob = 116;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(5*(60/176))), (tempo/176), (amplitude));
		
		//trigger wagner 
		wagner4MIDITime = [ 0, 0.45672, 0.9387, 1.39656, 1.84804, 2.30743, 2.51905, 2.77375, 2.99785, 3.2396, 3.47991, 3.70894, 3.9021, 4.16349, 4.39157, 4.62057, 4.84529, 5.09588, 5.33645, 5.54926, 5.77482, 6.01156, 6.4843, 6.94576 ];
wagner4MIDINotes = [ 55, 62, 62, 62, 62, 60, 62, 60, 58, 60, 58, 57, 60, 58, 60, 58, 57, 58, 57, 55, 58, 57, 50, 62 ];
wagner4MIDIVel = [ 56, 68, 67, 71, 64, 56, 66, 59, 57, 57, 58, 60, 58, 65, 59, 59, 59, 69, 46, 68, 62, 66, 74, 83 ];
wagner4MIDIEnd = 0.96776;
		Routine({1.do({
			(0.34090909090909*(176/tempo)).yield;
			this.wagnerAlgo(wagner4,13,panArr[9],wagner4MIDITime*(176/tempo), wagner4MIDINotes,wagner4MIDIVel,wagner4MIDIEnd,7,0,0, volArr[9]); //starts wagner, 7 mult,startPos 0,midiOffset 12, and parsifal_transformation buffer. bass_phrase1
			});}).play;
		}
		{pedalOnGlob == 48} {
		stepGlob = 118;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		}
		{pedalOnGlob == 49} {
		stepGlob = 120;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(1*(60/176))), (tempo/176), (amplitude));
		}
		{pedalOnGlob == 50} {
		stepGlob = 122;
		this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(7*(60/176))), (tempo/176), (amplitude));
		stepGlob = stepGlob + 1;
		this.funcPlay(stepGlob, (differ[stepGlob-71]+(11*(60/176))), (tempo/176), (amplitude));
		}
		{pedalOnGlob == 54} {
		wagner2MIDITime = [ 0, 0.435, 0.93559, 1.40114, 1.86684, 2.34146, 2.56846, 2.78823, 3.01855, 3.24427, 3.46793, 3.71102, 3.92061, 4.17186, 4.38545, 4.63381, 4.85211, 5.1058, 5.3283, 5.55564, 5.78726, 6.03569, 7.20704, 7.46572, 7.69142, 7.93706, 8.16333, 8.38984, 8.61414, 9.86811 ];
wagner2MIDINotes = [ 65, 70, 70, 70, 70, 68, 70, 68, 67, 68, 67, 65, 68, 67, 68, 67, 65, 67, 65, 63, 67, 65, 64, 65, 67, 64, 67, 69, 70, 69 ];
wagner2MIDIVel = [ 71, 79, 81, 82, 75, 77, 70, 73, 76, 78, 74, 70, 70, 78, 69, 77, 70, 75, 65, 63, 71, 81, 66, 65, 74, 70, 71, 78, 87, 87 ];
wagner2MIDIEnd = 0.93403;

	wagner3MIDITime = [ 0, 1.94561, 3.79028, 5.68573, 7.51598, 8.01569, 8.47507, 10.32415, 12.18651, 13.15206, 13.61471, 14.08188, 14.80015, 15.00889 ];
	wagner3MIDINotes = [ 55, 57, 55, 53, 60, 58, 57, 55, 54, 55, 50, 55, 53, 51 ];
	wagner3MIDIVel = [ 79, 79, 72, 79, 75, 75, 70, 74, 67, 73, 69, 73, 68, 73 ];
	wagner3MIDIEnd = 1.85975;
	
		Routine({1.do({
		(0.85227272727273*(176/tempo)).yield;
		this.wagnerAlgo(wagner2,16,panArr[7],wagner2MIDITime*(176/tempo),wagner2MIDINotes,wagner2MIDIVel, wagner2MIDIEnd,2,0,12,volArr[7]); //starts wagner algorithm with 2 mult and SolIchLauchen! buffer. sop_phrase3
		(3.5795454545455*(176/tempo)).yield;
		metalOn = 2; //turn metal bangs off
		(4.3892045454545*(176/tempo)).yield;
		this.wagnerAlgo(wagner3,18,panArr[8],wagner3MIDITime*(176/tempo), wagner3MIDINotes,wagner3MIDIVel,wagner3MIDIEnd,2,0,12,volArr[8]); //starts wagner algorithm with 3 mult and parsifal_komkom buffer. alto_phrase3	
		});}).play;
		}
		{pedalOnGlob == 56} {
	wagner2MIDITime = [ 0, 0.41209, 0.88336, 1.33732, 1.79722, 2.26186, 2.4943, 2.72611, 2.94727, 3.19289, 3.41888, 3.66332, 3.881, 4.13489, 4.36788, 4.58225, 4.81257, 5.05364, 5.29011, 5.52206, 5.74677, 6.01292 ];
		wagner2MIDINotes = [ 62, 67, 67, 67, 67, 65, 67, 65, 63, 65, 63, 62, 65, 63, 65, 63, 62, 63, 62, 60, 63, 62 ];
		wagner2MIDIVel = [ 71, 74, 82, 77, 77, 63, 58, 50, 49, 54, 55, 66, 61, 68, 66, 61, 64, 63, 60, 55, 63, 66 ];
wagner2MIDIEnd = 1.01561;
		Routine({1.do({
		(2.2159090909091*(176/tempo)).yield;
		this.wagnerAlgo(wagner2,22,panArr[7],wagner2MIDITime*(176/tempo),wagner2MIDINotes,wagner2MIDIVel, wagner2MIDIEnd,1.5,0,12,volArr[7]); //starts wagner algorithm with 1.5 mult and auf_as_as_tan! buffer. sop_phrase4
		});}).play;
		}
		{pedalOnGlob == 60} {
		buxtahude.set(\gate, 0);
		}
		{pedalOnGlob == 61} {
		parsifal.set(\gate, 0);
		}
		;
		
		case
		{pedalOnGlob < 16} {metalOn = 0;}
		{(pedalOnGlob >= 16).and(pedalOnGlob <= 36)} {metalOn = 2; metalOn.postln}
		{(pedalOnGlob >= 37).and(pedalOnGlob <= 55)} {metalOn = 0;}
		{(pedalOnGlob >= 56).and(pedalOnGlob <= 100)} {metalOn = 2; metalOn.postln}
		;
		
	}
	
	pedalOffFunc {
		//pedal down
	if(pedalOffGlob <= 36 , {
		pedalOff.value;
		});
		
		//track tempo - unlock tempo (until pedal 50)
		if(pedalOffGlob < 51, {		
		piano.set(\lock, 0);
		tempoLock = 0;
		gap = 0;
		});
		
		if(pedalOffGlob < 58, {metalOn = 1;},{metalOn = 2});
		
		case
		{pedalOffGlob == 1;} {
			
			countGlob=1;
			stepGlob=1;
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), (amplitude));
			notes = [ 8, 9, 10, 22, 34, 41, 42, 49, 50, 54, 56, 58, 63, 69, 71, 73, 76, 77, 85, 92, 94];
			stepGlob = stepGlob + 1;
		}
		{pedalOffGlob == 2;} {
			stepGlob = 23;
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), (amplitude));
			notes = [];
		}
		{pedalOffGlob == 3;} {
			stepGlob = 24;
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), (amplitude));
			notes = [];
		}
		{pedalOffGlob == 4;} {
			stepGlob = 25;
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), (amplitude));
			notes = [];
		}
		{pedalOffGlob == 5;} {
			countGlob=1;
			stepGlob = 26;
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), (amplitude));
			notes = [ 7, 10, 19, 21, 22, 25, 27, 29, 39, 43, 45, 52, 57 ];
			stepGlob = stepGlob + 1;
		}
		{pedalOffGlob == 6;} {
			stepGlob = 40;
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), (amplitude));
			notes = [];
		}
		{(pedalOffGlob >= 6).and(pedalOffGlob <= 15)} {
			stepGlob = stepGlob + 1;
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), (amplitude));
		}
		//new section
		{(pedalOffGlob >= 17).and(pedalOffGlob <= 36)} {
		
		grito4.set(\gates, 0);	
		}
		//new section
		{pedalOffGlob == 37;} {
			countGlob=1;
			stepGlob=71;
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
			notes = [2, 3, 11, 12, 16, 18, 23, 29, 30 ];
			stepGlob = stepGlob + 1;
		}
		{pedalOffGlob == 38;} {
			countGlob=1;
			stepGlob=82;
			notes = [2, 3, 8];
		}
		{pedalOffGlob == 40;} {
			countGlob=1;
			stepGlob=91;
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
			notes = [2];
			stepGlob = stepGlob + 1;
		}
		{pedalOffGlob == 41;} {
			countGlob=1;
			stepGlob=94;
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
			notes = [6, 8, 9 ];
			stepGlob = stepGlob + 1;
		}
		{pedalOffGlob == 42;} {
			countGlob=1;
			stepGlob=100;
			notes = [2, 4];
		}
		{pedalOffGlob == 43;} {
			stepGlob=103;
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
			notes = [];
		}
		{pedalOffGlob == 44;} {
			stepGlob=106;
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
			notes = [];
		}
		{pedalOffGlob == 46;} {
			countGlob=1;
			stepGlob=114;
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
			notes = [3];
			stepGlob = stepGlob + 1;
		}
		{pedalOffGlob == 48;} {
			countGlob=1;
			stepGlob=119;
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), (amplitude));
			notes = [];
		}
		{pedalOffGlob == 49;} {
			countGlob=1;
		}
		{pedalOffGlob == 52} {
		wagner2MIDITime = [ 0, 0.43788, 0.92651, 1.4018, 1.86847, 2.32857, 2.53807, 2.77511, 3.02108, 3.26988, 3.49546, 3.7272, 3.95072, 4.19399, 4.42221, 4.65991, 4.88228, 5.12046, 5.33463, 5.55283, 5.78762, 6.04365 ];
		wagner2MIDINotes = [ 60, 65, 65, 65, 65, 63, 65, 63, 62, 63, 62, 60, 63, 62, 63, 62, 60, 62, 60, 58, 62, 60 ];
		wagner2MIDIVel = [ 65, 75, 70, 67, 64, 67, 63, 64, 66, 74, 67, 67, 64, 70, 70, 73, 69, 71, 68, 64, 74, 73 ];
wagner2MIDIEnd = 0.91555;
		Routine({1.do({
		(0.68181818181818*(176/tempo)).yield;
		this.wagnerAlgo(wagner2,15,panArr[7],wagner2MIDITime*(176/tempo),wagner2MIDINotes,wagner2MIDIVel, wagner2MIDIEnd,3,0,12,volArr[7]); //starts wagner algorithm with 3 mult and HeHo! buffer. sop_phrase2
		});}).play;
		}
		{pedalOffGlob == 53} {
	wagner3MIDITime = [ 0, 0.22216, 0.41707, 0.68421, 1.60809, 2.07582, 2.56534 ];
	wagner3MIDINotes = [ 55, 57, 58, 57, 58, 53, 58 ];
	wagner3MIDIVel = [ 57, 59, 73, 74, 71, 65, 73 ];
	wagner3MIDIEnd = 0.97345;
	wagner1MIDITime = [ 0, 0.19504, 0.41741, 0.68072, 3.07783, 3.57588, 5.37327, 7.20836, 8.15494, 8.40785, 8.63849, 8.84088, 9.12368, 11.01333, 11.24076, 11.47899, 11.68768, 11.9344, 12.92046, 14.29654, 14.52098, 14.7693, 16.15705, 16.36318, 16.62956, 17.10326, 17.56722, 18.05898, 18.54971, 19.04267, 19.50867, 19.72804, 19.95247, 20.18202, 20.42707, 20.63169, 20.85759, 21.0932, 21.3297, 21.55346, 21.78639, 21.98898, 22.23242, 22.41869, 22.66688, 22.8777, 23.14348, 25.56309, 26.05803, 27.89297, 29.71817, 31.16498, 31.38196, 31.67431, 32.55808, 33.52447, 35.38815, 36.30197 ];
wagner1MIDINotes = [ 60, 62, 63, 62, 58, 63, 62, 60, 57, 60, 58, 57, 58, 64, 67, 65, 64, 60, 62, 60, 58, 60, 58, 57, 58, 58, 63, 63, 63, 63, 62, 63, 62, 60, 62, 60, 58, 62, 60, 62, 60, 58, 60, 58, 57, 60, 58, 55, 60, 58, 57, 55, 53, 55, 54, 55, 54, 55 ];
wagner1MIDIVel = [ 57, 66, 66, 72, 66, 80, 67, 76, 70, 69, 66, 70, 75, 77, 73, 76, 71, 73, 78, 65, 72, 75, 66, 73, 73, 73, 78, 74, 74, 72, 72, 70, 70, 68, 71, 58, 54, 60, 67, 68, 56, 55, 51, 65, 61, 64, 74, 70, 73, 71, 77, 68, 72, 76, 79, 81, 80, 78 ];
wagner1MIDIEnd = 0.99729;
	wagner4MIDITime = [ 0, 0.46816, 0.90944, 1.38647, 1.84337, 2.31654, 4.20449, 6.01258, 6.97907, 7.87769, 10.69412 ];
	wagner4MIDINotes = [ 50, 51, 48, 53, 41, 46, 55, 53, 58, 51, 50 ];
	wagner4MIDIVel = [ 59, 71, 64, 70, 67, 70, 74, 66, 75, 82, 68 ];
	wagner4MIDIEnd = 0.81921;
	Routine({1.do({
	(0.17045454545455*(176/tempo)).yield;
	this.wagnerAlgo(wagner4,20,panArr[9],wagner4MIDITime*(176/tempo), wagner4MIDINotes,wagner4MIDIVel,wagner4MIDIEnd,3,0,12, volArr[9]); //starts wagner, 3 mult,startPos 0,midiOffset 12, and parsifal_van_bade_kehrt. bass_phrase3
	(0.703125*(176/tempo)).yield;
	this.wagnerAlgo(wagner3,17,panArr[8],wagner3MIDITime*(176/tempo), wagner3MIDINotes,wagner3MIDIVel,wagner3MIDIEnd,3,0,0,volArr[8]); //starts wagner algorithm with 3 mult and trsitan_preludeII buffer. alto_phrase2
	(0.93749999999995*(176/tempo)).yield;
	this.wagnerAlgo(wagner1,19,panArr[6],wagner1MIDITime*(176/tempo), wagner1MIDINotes,wagner1MIDIVel,wagner1MIDIEnd,3,0,12,volArr[6]); //starts wagner algorithm with 3 mult and tristan_prelude buffer. tenor_phrase4
	});}).play;}
	{pedalOffGlob == 54;} {
		wagner4MIDITime = [ 0, 0.48919, 0.9333, 1.40955, 1.85785, 2.31444, 2.54655, 2.77088, 3.02001, 3.2772, 3.50163, 3.72964, 3.97383, 4.20878, 4.43551, 4.66431, 4.91177, 5.1623, 5.40268, 5.61955, 5.84173, 6.12344, 7.0537, 7.93095, 9.81296, 10.73382, 11.70746, 13.6092, 15.46883, 16.3962, 17.30361, 18.27207, 19.21313, 20.11914, 21.0387 ];
wagner4MIDINotes = [ 48, 53, 53, 53, 53, 51, 53, 51, 50, 51, 50, 48, 51, 50, 51, 50, 48, 50, 48, 46, 50, 48, 53, 46, 45, 50, 43, 51, 50, 55, 48, 53, 46, 51, 50 ];
wagner4MIDIVel = [ 54, 68, 67, 69, 67, 65, 56, 60, 53, 65, 61, 59, 66, 71, 66, 61, 57, 57, 52, 64, 59, 71, 74, 70, 70, 75, 71, 72, 73, 73, 76, 73, 66, 73, 74 ];
wagner4MIDIEnd = 3.6684;

		Routine({1.do({
			(0.34090909090909*(176/tempo)).yield;
this.wagnerAlgo(wagner4,21,panArr[9],wagner4MIDITime*(176/tempo), wagner4MIDINotes,wagner4MIDIVel,wagner4MIDIEnd,2,0,0, volArr[9]); //starts wagner, 2 mult,startPos 0,midiOffset 0, and parsifal_preludeII. bass_phrase4
			});}).play;
		}
		{pedalOffGlob == 56;} {
		wagner2MIDITime = [ 0, 0.47436, 0.9634, 1.44061, 1.90025, 2.3823, 2.59852, 2.84301, 3.04533, 3.30628, 3.5162, 3.76482, 3.96949, 4.21576, 4.44904, 4.66571, 4.8927, 5.14327, 5.37231, 5.605, 5.81542, 6.0906, 7.05312, 8.45731, 8.68843, 8.92861, 10.31457, 10.52438, 10.82723, 13.17334, 13.63016, 14.58983, 15.96905, 16.19159, 16.44525, 17.82231, 18.04448, 18.29817, 20.13637, 21.08298, 22.95217, 24.17524, 24.40116, 24.63341, 24.89608 ];
		wagner2MIDINotes = [ 67, 74, 74, 74, 74, 72, 74, 72, 70, 72, 70, 69, 72, 70, 72, 70, 69, 70, 69, 67, 70, 69, 70, 69, 67, 69, 67, 66, 67, 66, 74, 75, 74, 72, 74, 72, 70, 72, 70, 69, 74, 72, 70, 74, 72 ];
		wagner2MIDIVel = [ 69, 76, 70, 79, 77, 68, 74, 56, 63, 70, 67, 67, 64, 71, 68, 70, 66, 73, 68, 68, 69, 74, 76, 63, 69, 71, 62, 58, 73, 69, 78, 78, 70, 72, 73, 71, 74, 77, 77, 77, 77, 74, 71, 77, 79 ];
		
		wagner3MIDITime = [ 0, 1.88017, 2.8269, 3.77808, 5.64438, 7.11686, 7.31948, 7.56779, 8.4465, 9.63767, 9.88359, 10.10237, 10.37013, 11.51949, 11.7726, 12.00436, 12.23856, 12.47721, 12.69961, 12.88553, 13.17163, 13.62918, 14.56985, 15.02778, 15.96277, 17.40757, 17.61699, 17.881, 19.69461, 20.61026, 22.5021, 24.42241, 25.98756, 26.21682 ];
wagner3MIDINotes = [ 60, 58, 57, 62, 67, 65, 64, 65, 63, 62, 60, 63, 62, 60, 58, 62, 60, 62, 60, 58, 60, 62, 67, 70, 69, 70, 69, 67, 66, 67, 66, 67, 64, 66 ];
wagner3MIDIVel = [ 70, 73, 77, 77, 78, 72, 73, 78, 78, 75, 69, 71, 80, 67, 52, 63, 66, 62, 61, 63, 68, 78, 74, 82, 81, 68, 71, 75, 76, 78, 75, 82, 75, 87 ];

		wagner4MIDITime = [ 0, 0.46794, 0.94091, 1.42954, 1.88165, 2.34396, 2.56138, 2.80972, 3.00892, 3.27165, 3.46852, 3.70592, 3.93331, 4.19308, 4.4358, 4.68418, 4.89182, 5.12499, 5.35651, 5.59704, 5.82029, 6.07181 ];
		wagner4MIDINotes = [ 50, 55, 55, 55, 55, 53, 55, 53, 51, 53, 51, 50, 53, 51, 53, 51, 50, 51, 50, 48, 51, 50 ];
		wagner4MIDIVel = [ 55, 61, 59, 64, 59, 66, 54, 58, 65, 60, 55, 48, 52, 64, 63, 69, 63, 69, 63, 67, 67, 70 ];
		wagner4MIDIEnd = 7.68043;
		wagner3MIDIEnd = 1.29831;
		
		Routine({1.do({
		(2.0880681818182*(176/tempo)).yield;
		this.wagnerAlgo(wagner3,24,panArr[8],wagner3MIDITime*(176/tempo), wagner3MIDINotes,wagner3MIDIVel,wagner3MIDIEnd,1,0,12,volArr[8]); //starts wagner algorithm with 4 mult and verrater_ha buffer. alto_phrase4
		(0.82094*(176/tempo)).yield;
		this.wagnerAlgo(wagner2,23,panArr[7],wagner2MIDITime*(176/tempo),wagner2MIDINotes,wagner2MIDIVel, wagner2MIDIEnd,1,0,12,volArr[7]); //starts wagner algorithm with 1 mult and koenig_merke_heil! buffer. sop_phrase5
		(12.94195*(176/tempo)).yield;
		this.wagnerAlgo(wagner4,26,panArr[9],wagner4MIDITime*(176/tempo), wagner4MIDINotes,wagner4MIDIVel,wagner4MIDIEnd,1,0,12, volArr[9]); //starts wagner, 1 mult,startPos 0,midiOffset 12, and parsifal_weh_hoho buffer. bass_phrase5
		wagner1MIDITime = [ 0, 0.43668, 0.92356, 1.39472, 1.86051, 2.33669, 2.54125, 2.79771, 3.00566, 3.26049, 3.45647, 3.69883, 3.93714, 4.17674, 4.40239, 4.62087, 4.84831, 5.08735, 5.31378, 5.55273, 5.75323, 6.0369, 7.05622 ];
wagner1MIDINotes = [ 55, 62, 62, 62, 62, 60, 62, 60, 58, 60, 58, 57, 60, 58, 60, 58, 57, 58, 57, 55, 58, 57, 57 ];
wagner1MIDIVel = [ 57, 59, 67, 70, 66, 51, 58, 46, 57, 57, 48, 57, 49, 54, 52, 49, 50, 58, 56, 54, 64, 65, 59 ];
wagner1MIDIEnd = 0.73818;
		(5.95784*(176/tempo)).yield;
		this.wagnerAlgo(wagner1,27,panArr[6],wagner1MIDITime*(176/tempo), wagner1MIDINotes,wagner1MIDIVel,wagner1MIDIEnd,1,0,12,volArr[6]); //starts wagner, 1 mult,startPos 0,midiOffset 12, and parsifal_met_diesem buffer. tenor_phrase6
		(5.2*(176/tempo)).yield;
		wagner3.set(\gate, 0, \dec, rrand(2.0,2.5));
		wagner4.set(\gate, 0, \dec, rrand(2.0,2.5));
		(1.0*(176/tempo)).yield;
		wagner1.set(\gate, 0, \dec, rrand(1.0,1.4));
		wagner2.set(\gate, 0, \dec, rrand(1.0,1.2));
			});}).play;
		}
		{pedalOffGlob == 57;} {
		wagner1MIDITime = [ 0, 0.47068, 0.95388, 1.39275, 1.85402, 2.30156, 2.53577, 2.79098, 2.99884, 3.25361, 3.46734, 3.7179, 3.94148, 4.20726, 4.41299, 4.65902, 4.88858, 5.12812, 5.35212, 5.58883, 5.82329, 6.07089, 6.29935, 6.55105, 6.75321, 7.0278, 7.97316 ];
wagner1MIDINotes = [ 55, 62, 62, 62, 62, 60, 62, 60, 58, 60, 58, 57, 60, 58, 60, 58, 57, 58, 57, 55, 58, 57, 58, 57, 55, 57, 55 ];
wagner1MIDIVel = [ 73, 79, 80, 82, 82, 73, 73, 61, 65, 67, 65, 68, 58, 74, 53, 67, 65, 72, 67, 64, 67, 68, 66, 66, 67, 83, 79 ];
wagner1MIDIEnd = 0.32705;
		Routine({1.do({
			(2.7272727272727*(176/tempo)).yield;
			this.wagnerAlgo(wagner1,25,panArr[6],wagner1MIDITime*(176/tempo), wagner1MIDINotes,wagner1MIDIVel,wagner1MIDIEnd,2,0,12,volArr[6]); //starts wagner, 2 mult,startPos 0,midiOffset 12, and parsifal_vergeh buffer. tenor_phrase5
			});}).play;
		}
		{pedalOffGlob == 59;} {
		//turn wagner off for solo buxtahude
		//solo buxtahude
		buxtahude.put(0, \pvplayBuxta2, 0, [\out, 0, \recBuf, 28, \amp, volArr[10], \rate, (tempo/176), \gate, 1, \pan, 1]);
		buxtahude.put(1, \pvplayBuxta2, 0, [\out, 0, \recBuf, 29, \amp, volArr[10], \rate, (tempo/176), \gate, 1, \pan, -1]);
		}
		{pedalOffGlob == 60;} {
		//solo wagner
		//buxtahude.set(\gate, 1);
		parsifal.put(0, \pvplayBuxta2, 0, [\out, 0, \recBuf, 30, \amp, volArr[11], \rate, (tempo/176), \gate, 1, \pan, 1]);
		parsifal.put(1, \pvplayBuxta2, 0, [\out, 0, \recBuf, 31, \amp, volArr[11], \rate, (tempo/176), \gate, 1, \pan, -1]);
		}
		;
		
		//send tempo to computer2
		//case
//		{pedalOffGlob >= 37} {midiOut.songPtr( tempo )};
		

	}
	
	oscRespond {var oldTime=0, averege, bep=0, difference, ampSilence, ampOnset=0, arrFric1, arrFric2, oldNum = 0, oldNum2 = 0, cambio;
	amplitude = 0.1;
	countGlob=1;
	stepGlob=1;
	notes = [];
	tempoLock = 1;
	metalOn = 0;
	gap = 0;
	ampSilence = (1..6);
	osc = OSCresponder(s.addr,'/tr',{ arg time,responder,msg;
	case
	{msg[2] == 0} {
		if(msg[3] == 1, {
			if((time-oldTime) > (60/tempo/ 3*2), {
			
			if(metalOn == 1, {
			metal.spawn([\amp, (ampOnset*volArr[0]), \recBuf, bufferArr[[0,1,2].choose].bufnum, \rate, rrand(0.8,1.2), \pan, panArr[0]]);
			ampSilence.add(ampOnset*volArr[0]);
			ampSilence.removeAt(0);
			});
			
			if(notes.includes(countGlob), { 
			case
			{pedalOffGlob < 36} {
			this.funcPlay(stepGlob, d[stepGlob-1], (tempo/176), amplitude);
			}
			{pedalOffGlob >= 36} {
			this.funcPlay(stepGlob, differ[stepGlob-71], (tempo/176), amplitude);
			};	//samples triggered by offset
			stepGlob = stepGlob + 1; });
			
			//triggering wagner samples:
			//notice pedal number - 1 => because of other function
			case
			{(pedalOffGlob == 37).and(countGlob == 6)} {
			wagner1MIDITime = [ 0, 0.47651, 0.96786, 1.45693, 1.91842, 2.36604, 2.60067, 2.84565, 3.06196, 3.3013, 3.53399, 3.77135, 3.97449, 4.22417, 4.43986, 4.67524, 4.92263, 5.17331, 5.41597, 5.63673, 5.85276, 6.1186, 6.58039, 7.05998, 9.00473, 9.90555, 10.78184, 11.7118, 12.63552, 13.60302, 14.03527, 14.51187, 15.50301, 16.47957 ];
wagner1MIDINotes = [ 55, 62, 62, 62, 62, 60, 62, 60, 58, 60, 58, 57, 60, 58, 60, 58, 57, 58, 57, 55, 58, 57, 50, 62, 58, 60, 57, 58, 55, 57, 55, 57, 55, 55 ];
wagner1MIDIVel = [ 71, 78, 71, 74, 73, 67, 71, 65, 65, 67, 65, 68, 66, 68, 53, 65, 66, 69, 63, 63, 65, 70, 74, 84, 71, 71, 75, 79, 74, 77, 68, 78, 73, 80 ];
wagner1MIDIEnd = 0.6562;
			this.wagnerAlgo(wagner1,9,panArr[6],wagner1MIDITime*(176/tempo), wagner1MIDINotes,wagner1MIDIVel,wagner1MIDIEnd,9,0,0,volArr[6]); //starts wagner algorithm with 9 mult and tristan_prelude buffer. tenor_phrase1
			}
			{(pedalOffGlob == 41).and(countGlob == 5)} {
			
			wagner2MIDITime = [ 0, 0.4926, 0.96348, 1.40365, 1.86662, 2.33738, 2.58841, 2.80683, 3.00521, 3.25671, 3.47585, 3.7493, 3.9665, 4.21587, 4.43472, 4.66107, 4.89566, 5.16304, 5.37344, 5.61783, 5.83083, 6.07941, 6.55793, 7.02662, 9.38474, 9.82853, 10.81561, 11.75137, 13.10319, 13.59658, 14.96066, 15.41809, 15.89906, 16.39481, 18.26813, 19.21776, 21.12464, 22.97246, 24.15054, 24.36591, 24.58344, 24.8606, 25.51098, 25.81194, 27.68392, 29.51835, 30.46612, 31.46115, 32.34348, 33.30384, 34.21755, 36.50215, 36.72444, 37.00558 ];
wagner2MIDINotes = [ 74, 79, 79, 79, 79, 77, 79, 77, 75, 77, 75, 74, 77, 75, 77, 75, 74, 75, 74, 72, 75, 74, 67, 79, 78, 79, 82, 81, 82, 79, 81, 78, 79, 79, 77, 75, 74, 72, 69, 70, 72, 70, 72, 74, 72, 74, 67, 72, 66, 67, 67, 66, 64, 66 ];
wagner2MIDIVel = [ 68, 73, 71, 77, 69, 63, 68, 61, 58, 64, 65, 62, 58, 67, 61, 63, 63, 70, 67, 59, 74, 74, 68, 77, 73, 81, 77, 76, 76, 73, 75, 78, 74, 78, 74, 81, 75, 77, 63, 71, 73, 78, 74, 78, 79, 78, 73, 77, 77, 81, 81, 74, 71, 78 ];
wagner2MIDIEnd = 0.6562;
		this.wagnerAlgo(wagner2,10,panArr[7],wagner2MIDITime*(176/tempo),wagner2MIDINotes,wagner2MIDIVel, wagner2MIDIEnd,4,0,12,volArr[7]); //starts wagner algorithm with 4 mult and Shepards Pipe! buffer. sop_phrase1
			}
			{(pedalOffGlob == 47).and(countGlob == 3)} {
			wagner4MIDITime = [ 0, 0.20411, 0.43175, 0.6879, 1.1572, 1.63268, 4.41594, 5.30933, 7.47155, 7.68829, 7.99209, 8.12705, 8.26395, 8.32009, 8.44856, 8.54086, 8.66406, 8.75268, 8.88526, 9.02343, 10.05006, 11.01847, 13.80769, 14.76692, 16.14224, 16.59616, 18.01684, 18.48065, 18.73482, 18.9638, 19.1876, 19.44817, 19.66549, 19.88885, 20.11209, 20.36218, 20.59951, 20.82284, 21.05112, 21.28182, 21.50758, 21.74777, 21.95778, 22.20584, 22.65811, 23.13487, 24.11735, 25.49436, 25.93502 ];
wagner4MIDINotes = [ 54, 55, 57, 58, 46, 51, 50, 48, 48, 46, 45, 43, 45, 46, 48, 50, 52, 54, 55, 51, 52, 50, 50, 52, 54, 55, 51, 53, 55, 53, 51, 53, 51, 50, 53, 51, 53, 51, 50, 51, 50, 48, 51, 50, 46, 58, 55, 57, 58 ];
wagner4MIDIVel = [ 61, 75, 76, 84, 68, 76, 73, 79, 65, 72, 58, 73, 60, 74, 70, 71, 66, 69, 78, 84, 83, 77, 77, 80, 77, 82, 77, 72, 66, 71, 73, 76, 69, 70, 67, 74, 68, 66, 73, 74, 67, 78, 78, 77, 78, 81, 83, 82, 85 ];
wagner4MIDIEnd = 0.96647;
			this.wagnerAlgo(wagner4,14,panArr[9],wagner4MIDITime*(176/tempo), wagner4MIDINotes+12,wagner4MIDIVel,wagner4MIDIEnd,4,0,16,volArr[9]); //starts wagner algorithm with 4 mult and parsifal_nun_achte_wohl buffer. bass_phrase2
			}
			{(pedalOffGlob == 47).and(countGlob == 7)} {
			wagner3MIDITime = [ 0, 0.49981, 0.98657, 1.45584, 1.93556, 2.39581, 2.62623, 2.84031, 3.06382, 3.30381, 3.51321, 3.74948, 3.97229, 4.22486, 4.44004, 4.67555, 4.90719, 5.17094, 5.38583, 5.6202, 5.82304, 6.11408, 8.98215, 9.85037 ];
wagner3MIDINotes = [ 62, 67, 67, 67, 67, 65, 67, 65, 63, 65, 63, 62, 65, 63, 65, 63, 62, 63, 62, 60, 63, 62, 60, 62 ];
wagner3MIDIVel = [ 69, 81, 76, 75, 75, 68, 72, 59, 63, 70, 66, 64, 58, 67, 64, 64, 64, 71, 68, 59, 70, 77, 69, 73 ];
wagner3MIDIEnd = 1.77975;
			this.wagnerAlgo(wagner3,8,panArr[8],wagner3MIDITime*(176/tempo), wagner3MIDINotes,wagner3MIDIVel,wagner3MIDIEnd,4,0,12,volArr[8]); //starts wagner algorithm with 4 mult and parsifal_komkom buffer. alto_phrase1
			}
			{(pedalOffGlob == 48).and(countGlob == 1)} {
			wagner1MIDITime = [ 0, 0.4715, 0.96028, 1.44311, 1.93176, 3.3025, 4.22619, 5.17069, 6.56596, 7.47476, 7.93078, 8.40567, 8.64498, 8.92295, 9.37108, 9.83762, 10.30725, 10.80573, 11.26355, 11.72448, 11.95507, 12.2187, 12.4382, 12.68296, 12.91768, 13.14702, 13.37984, 13.63288, 13.88107, 14.10626, 14.30839, 14.53982, 14.74714, 15.01979, 15.23764, 15.50634, 16.84679, 17.35275, 18.75453, 19.68404, 20.6378, 21.12992, 22.53289, 22.97131 ];
wagner1MIDINotes = [ 55, 57, 57, 57, 58, 57, 57, 58, 55, 57, 58, 57, 55, 57, 57, 62, 62, 62, 62, 60, 62, 60, 58, 60, 58, 57, 60, 58, 60, 58, 57, 58, 57, 55, 58, 57, 58, 55, 57, 62, 62, 58, 60, 58 ];
wagner1MIDIVel = [ 58, 66, 63, 68, 70, 74, 70, 73, 75, 67, 68, 70, 64, 78, 67, 75, 70, 74, 75, 72, 65, 58, 61, 66, 63, 65, 66, 71, 59, 67, 71, 74, 68, 69, 70, 73, 68, 76, 69, 75, 77, 70, 70, 71 ];
wagner1MIDIEnd = 1.40038;
			this.wagnerAlgo(wagner1,12,panArr[6],wagner1MIDITime*(176/tempo), wagner1MIDINotes,wagner1MIDIVel,wagner1MIDIEnd,5,0,12,volArr[6]); //starts wagner algorithm with 9 mult and tristan_prelude buffer. tenor_phrase3
			};
			
			
			countGlob = countGlob + 1;
			
			oldTime = time;
			});	
		}); 
	}
	{msg[2] == 1} {amplitude = msg[3]; ampOnset = msg[3]}
	//{msg[2] == 2} {ampOnset = msg[3]}
	{msg[2] == 3} {
		if(msg[3] != 0 , {
			bep = bep + 1; 
			if((bep == 1).and(gap == 1), {
			difference = ampSilence - ampSilence.mean;
			
			if(metalOn == 0, {
			metal.spawn([\amp, (ampSilence.mean + rrand(difference.minItem, difference.maxItem)), \recBuf, bufferArr[[0,1,2].choose].bufnum, \rate, rrand(0.8,1.2), \pan, panArr[0]]);
			
			});
			
			});
		}, {
			bep= 0
		});
	}
	{msg[2] == 4} {
				if(oldNum != msg[3], {
				
				sensorVal1 = msg[3];
				sensor1.value(sensorVal1);
				sensor2.value(sensorVal1);
				
				//"updown: ".post; 
				
				 
				oldNum = msg[3]
				});
				}
	{msg[2] == 5} {
				if(oldNum2 != msg[3], {
				
				if(oldNum2 != msg[3], {
				//"leftright: ".post; 
				if([0,1,2,3].includes(msg[3].linlin(0,127,127,0)), {cambio = [0,1].choose; 'cambio cambio'.postln}); 
				if(cambio == 0, {
				sensorVal2 = msg[3].linlin(0,127,0, 63).round(1);
				bend1.value(sensorVal2);
				bend2.value(sensorVal2);
				}, {
				sensorVal2 = msg[3].linlin(0,127,127,64).round(1);
				bend1.value(sensorVal2);
				bend2.value(sensorVal2);
				});
				oldNum2 = msg[3]});
				});
				}
				{msg[2] == 6} {if(tempoStart == 1, {tempo = (msg[3]*60).round(1)});}	
				;
	}).add;
	
	//oscMax = OSCresponder(nil, '/max', {arg time, responder, msg, addr;
//var number1, number2;
//
//	if(msg[1] == 2, {
//		case
//			{msg[2] < leftVal} {number1 = leftVal}
//			{msg[2] > rightVal} {number1 = rightVal}
//			{(msg[2] >= leftVal).and(msg[2] <= rightVal)} {number1 = msg[2]};
//		sensorLag2.set(\num, number1);
//	});
//		
//	if(msg[1] == 3, {
//		case
//			{msg[2] < lowVal} {number2 = lowVal}
//			{msg[2] > highVal} {number2 = highVal}
//			{(msg[2] >= lowVal).and(msg[2] <= highVal)} {number2 = msg[2]};
//		sensorLag1.set(\num, number2);
//	});
//}).add;

//direct from MIDIBlue
MIDIIn.control = { arg src, chan, num, val; 
var number1, number2;

	if(chan == 2, {
		case
			{val < leftVal} {number1 = leftVal}
			{val > rightVal} {number1 = rightVal}
			{(val >= leftVal).and(val <= rightVal)} {number1 = val};
		sensorLag2.set(\num, number1);
	});
		
	if(chan == 1, {
		case
			{val < lowVal} {number2 = lowVal}
			{val > highVal} {number2 = highVal}
			{(val >= lowVal).and(val <= highVal)} {number2 = val};
		sensorLag1.set(\num, number2);
	});
};

	}
	
	prepare {arg pianoOut = 0, metalOut=0, grito1Out = 2, grito2Out = 3, grito3Out=4, grito4Out=5, motorOut=5, wagner1Out=4, wagner2Out=4, wagner3Out=4, wagner4Out=4,buxtaOut=4,parsifalOut=4;
	Routine({1.do({
	'wait'.postln;
	this.startMIDI(pianoOut, metalOut, grito1Out, grito2Out, grito3Out, grito4Out, motorOut, wagner1Out, wagner2Out, wagner3Out, wagner4Out, buxtaOut, parsifalOut);
	0.01.yield;
	this.oscRespond;
	});
	'ready'.postln;
	//message to computer2: start audio for headphones
	//midiOut.polyTouch(0,1,0); 
	}).play
	}
	 
	remove {
	osc.remove;
	oscMax.remove;
	piano.clear;
	metal.clear; 
	grito1.clear;
	grito2.clear;
	grito3.clear;
	grito4.clear;
	motor.clear;
	wagner1.clear;
	wagner2.clear;
	wagner3.clear;
	wagner4.clear;
	buxtahude.clear;
	parsifal.clear;
	sensor.clear;
	//message to computer2: remove score
	//midiOut.polyTouch(0,3,0);
	tempoStart=0;
	stepTempo=0;
	
	}
	
	readyToStart {arg firstTempo=156, onsetThresh=0.13, page=1, pedalDown=1, pedalUp=1;
	Routine({1.do({
	'wait'.postln;
	piano.put(0, \detect, 0, [\out, 0, \in, audioIn, \thresh1, onsetThresh, \initTempo, firstTempo/60, \micOn, 0, \impOn, 0, \initImpOn, 1]);
	metal.put(0, \bang, 0, [\out, 0, \amp, 0, \recBuf, bufferArr[[0,1,2].choose].bufnum, \rate, rrand(0.8,1.2), \adjVol, 1.0, \pan, 0]);
	grito1.put(0, \pvplayVoc, 0, [\out, 0, \recBuf, randBuffArr[0].bufnum, \rate, 1, \amp, 0, \thresh, 0.5, \ratioDown, 0.7, \ratioUp, 0.9, \adjVol, 1.0, \pan, 0]);
	grito2.put(0, \pvplayGrito, 0, [\out, 0, \recBuf, randBuffArr[1].bufnum, \rate, 1, \amp, 0, \thresh, 0.5, \ratioDown, 0.7, \ratioUp, 0.9, \adjVol, 1.0, \pan, 0, \recBuf2, bufferArr[4].bufnum, \jefeBuf, bufferArr[3].bufnum, \startPos, rrand(0, 820865), \osciOff, rrand(1.25, 0.75), \startOff, rrand(0,403)]); 
	grito3.put(0, \pvplayBet, 0, [\out, 0, \recBuf, randBuffArr[2].bufnum, \rate, 1, \amp, 0, \thresh, 0.5, \ratioDown, 0.6, \ratioUp, 0.9, \adjVol, 1.0, \pan, 0]);
	grito4.put(0, \pvplayMono, 0, [\out, 0, \recBuf, randBuffArr[3].bufnum, \rate, 1, \amp, 0, \thresh, 0.5, \ratioDown, 0.7, \ratioUp, 0.9, \adjVol, 0.5, \pan, -1]);
	
	//sensor.put(0, \lagging, 0, [\num, lowVal, \low, lowVal, \high, highVal, \id, 4, \lag, 0.5]);
//	sensor.put(1, \lagging, 0,  [\num, rightVal, \low, leftVal, \high, rightVal, \id, 5, \lag, 0.8]);

//	sensorLag1 = sensor.objects[0];
//	sensorLag2 = sensor.objects[1];
	
	wagner1.put(0, \playWagner, 0, [\rate,1,\amp,0,\buffer,7]);
	wagner1.put(1, \playWagner, 0, [\rate,1,\amp,0,\buffer,7]);
	wagner2.put(0, \playWagner, 0, [\rate,1,\amp,0,\buffer,8]);
	wagner2.put(1, \playWagner, 0, [\rate,1,\amp,0,\buffer,8]);
	wagner3.put(0, \playWagner, 0, [\rate,1,\amp,0,\buffer,9]);
	wagner3.put(1, \playWagner, 0, [\rate,1,\amp,0,\buffer,9]);
	wagner4.put(0, \playWagner, 0, [\rate,1,\amp,0,\buffer,10]);
	wagner4.put(1, \playWagner, 0, [\rate,1,\amp,0,\buffer,10]);
	0.2.yield;
	wagner1.objects[0].set(\gate, 0);
	wagner1.objects[1].set(\gate, 0);
	wagner2.objects[0].set(\gate, 0);
	wagner2.objects[1].set(\gate, 0);
	wagner3.objects[0].set(\gate, 0);
	wagner3.objects[1].set(\gate, 0);
	wagner4.objects[0].set(\gate, 0);
	wagner4.objects[1].set(\gate, 0);
	//message to computer2: display score at page 
	//midiOut.polyTouch(0,2,page);
	});
	'ready'.postln;
	}).play;
	
	//case
//{page == \pedal} {this.changePedalNum(pedalDown,pedalUp)}
//{page == 1} {this.changePedalNum(1,1)}
//{[2,3,4].includes(page)} {this.changePedalNum(2,1)}
//{page == 5} {this.changePedalNum(3,3)}
//{page == 6} {this.changePedalNum(5,5)}
//{page == 7} {this.changePedalNum(6,5)}
//{page == 8} {this.changePedalNum(6,6)}
//{page == 9} {this.changePedalNum(8,8)}
//{page == 10} {this.changePedalNum(10,10)}
//{page == 11} {this.changePedalNum(13,13)}
//{page == 12} {this.changePedalNum(17,17)}
//{page == 13} {this.changePedalNum(22,23)}
//{page == 14} {this.changePedalNum(28,29)}
//{page == 15} {this.changePedalNum(34,35)}
//{page == 16} {this.changePedalNum(37,38)}
//{page == 17} {this.changePedalNum(38,38)}
//{page == 18} {this.changePedalNum(38,39)}
//{page == 19} {this.changePedalNum(39,40)}
//{page == 20} {this.changePedalNum(40,41)}
//{page == 21} {this.changePedalNum(41,42)}
//{page == 22} {this.changePedalNum(42,43)}
//{page == 23} {this.changePedalNum(43,44)}
//{page == 24} {this.changePedalNum(44,45)}
//{page == 25} {this.changePedalNum(45,46)}
//{page == 26} {this.changePedalNum(46,47)}
//{page == 27} {this.changePedalNum(47,48)}
//{page == 28} {this.changePedalNum(48,49)}
//{page == 29} {this.changePedalNum(49,50)}
//{page == 30} {this.changePedalNum(50,51)}
//{page == 31} {this.changePedalNum(51,52)};

	
}

	wagnerAlgo {arg wagnerSynth, wagnerBuffer, wagnerPan, wagnerTimes, wagnerNotes, wagnerVelocity, wagnerEnd, wagnerMult, wagnerOffset=0, wagnerMidiOff=0, ampWagner=1;
	var step = 0,rate,index,wagnerArray,wagnerDiff,wagnerRateTime,wagnerRate,wagnerRoutine,wagnerPatt;
	wagnerDiff = wagnerTimes.differentiate;
	wagnerPatt = Pseq([0,1],inf).asStream;
	wagnerRate = [];
	wagnerNotes.do({|item|wagnerRate = wagnerRate.add( (item.midicps/(wagnerNotes[0].midicps+12)))});
	wagnerRateTime = (wagnerDiff.copyRange(1,wagnerDiff.size-1)/wagnerRate).integrate;
	
	wagnerRoutine = Routine({
	1.do({
	index = wagnerPatt.next;
	rate = (wagnerNotes[step].midicps/((55+wagnerMidiOff).midicps));
	wagnerSynth.put(index,\playWagner, 0, [\rate, rate, \amp, (wagnerVelocity[step]/wagnerVelocity.maxItem)*ampWagner, \start, (0+wagnerOffset)*44100, \atk, rrand(0.05,0.3), \dec, rrand(0.2,0.5), \buffer, wagnerBuffer, \pan, wagnerPan]);
	step = step + 1;
	wagnerArray = Array.fill((wagnerTimes.size/wagnerMult).round(1), {rrand(0, wagnerTimes.size)});
	wagnerArray.sort.postln;
	(wagnerTimes.size-1).do({
	if(wagnerArray.includes(step), {
	rate = (wagnerNotes[step].midicps/((55+wagnerMidiOff).midicps));
	wagnerSynth.objects[index].set(\gate, 0);
	index = wagnerPatt.next;
	wagnerSynth.put(index,\playWagner, 0, [\rate, rate, \amp, (wagnerVelocity[step]/wagnerVelocity.maxItem)*ampWagner, \start, (wagnerRateTime[step]+wagnerOffset)*44100, \atk, rrand(0.05,0.3), \dec, rrand(0.2,0.5), \buffer, wagnerBuffer, \pan, wagnerPan]);
	});
	wagnerDiff[step].yield;
	
	//step.postln;
	step = step + 1;
	});
	wagnerEnd.yield;
	wagnerSynth.objects[index].set(\gate, 0, \dec, rrand(0.4,0.8));
	});
	}).play;
	}
	
	changePedalNum {arg pedalDown = 1, pedalUp = 1;
	pedalOffGlob = pedalDown-1;
	pedalOnGlob = pedalUp-1;
	//message to computer 2: send pedalOn number
	//midiOut.noteOff(0,1,pedalOnGlob);
	}
	
	changeLocation {arg page=1, pedalDown = 1, pedalUp = 1;
	this.funcPage(page);
	this.changePedalNum(pedalDown, pedalUp);
	}	

	//funcPage {arg page;
	//message to computer 2: send page number
	//midiOut.noteOff(0,0,page);
	//}
	
	mixer {var controlSpec, mixWindow;
	mixWindow = GUI.window.new("Mixer - OnViolence", Rect(800, 700, 440, 280));
	mixWindow.front;
	mixWindow.view.decorator = FlowLayout(mixWindow.view.bounds);
	controlSpec = ControlSpec(0.0, 2.0);
	//piano slider
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"pianoVol", // label
					controlSpec, // control spec
					{|ez| piano.set(\globamp, ez.value) },// action
					1.0 // initVal
					);
	//metal slider				
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"metalVol", // label
					controlSpec, // control spec
					{|ez| metal.set(\globamp, ez.value) },// action
					1.0 // initVal
					);
	//scream slider					
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"screamVol", // label
					controlSpec, // control spec
					{|ez| 	grito1.set(\globamp, ez.value);
							grito2.set(\globamp, ez.value);
							grito3.set(\globamp, ez.value);
					
					 },// action
					1.0 // initVal
					);
	//guitar slider					
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"guitarVol", // label
					controlSpec, // control spec
					{|ez| grito4.set(\globamp, ez.value) },// action
					1.0 // initVal
					);
	//motor slider					
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"volMotor", // label
					controlSpec, // control spec
					{|ez| motor.set(\globamp, ez.value) },// action
					1.0 // initVal
					);
	//wagner slider					
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"volWagner", // label
					controlSpec, // control spec
					{|ez|	wagner1.set(\globamp, ez.value);
							wagner2.set(\globamp, ez.value);
							wagner3.set(\globamp, ez.value);
							wagner4.set(\globamp, ez.value);
					},// action
					1.0 // initVal
					);
	//buxtahude slider				
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"buxtahude", // label
					controlSpec, // control spec
					{|ez| buxtahude.set(\globamp, ez.value) },// action
					1.0 // initVal
					);
	//parsifal slider				
	EZSlider(mixWindow, // window
					400 @ 24, // dimensions
					"parsifal", // label
					controlSpec, // control spec
					{|ez| parsifal.set(\globamp, ez.value) },// action
					1.0 // initVal
					);
										
	}

	mixer2 {var controller, specs;
		controller = ControllerGUI(volArr.size, string: "Mixer");
		specs = [-inf, 6, \db, 0, -inf, " dB"].asSpec;
		
		
		volArr.do{|item, index|
		controller.setSlider(index+1, specs.unmap(item.ampdb).linlin(0,1,0,127));
		};
		
		panArr.do{|item, index|
		controller.setKnob(index+1, item.linlin(-1,1,0,127));
		};
		
		volArr.do{|item,index|
		controller.slider(index+1, {arg val; 
		volArr[index] = 
		specs.map(val.linlin(0,127,0,1)).dbamp.round(0.01).postln });
		};
		
		panArr.do{|item,index|
		controller.knob(index+1, {arg val; 
		panArr[index] = 
		val.linlin(0,127,-1,1).round(0.02).postln });
		};

		}
	
	
	*initClass {

	SynthDef.writeOnce(\pvplayMono, {arg out=0, recBuf=1, rate=1.0, amp=1.0, adjVol=1, thresh= 0.1, ratioUp = 1/3, ratioDown=1, pan=0, dec=0.9, gates=1, globamp=1.0;
	var in, chain, signal, signal2, signal3, bufnum;
	bufnum = LocalBuf.new(2048, 1);
	chain = PV_PlayBuf(bufnum, recBuf, rate);
	signal = IFFT(chain);
	signal2 = Compander.ar(signal, signal,
		thresh: thresh,
		slopeBelow: ratioDown,
		slopeAbove: 1.0,
		clampTime: 0.01,
		relaxTime: 0.01
	);
	signal3 = Limiter.ar(signal2*adjVol, ratioUp, 0.01)*EnvGen.kr(Env.asr(0.01, 1.0, dec), gates);
	DetectSilence.ar(signal3, doneAction: 2);
	Out.ar(out, Pan2.ar(signal3, pan, amp*globamp));
	});
	
	SynthDef.writeOnce(\pvplayVoc, {arg out=0, recBuf=1, rate=1.0, amp=1.0, adjVol=1, pan=0, globamp=1.0;
	var in, chain, signal, signal2, signal3, kernel, kernel2, freq, hasFreq, bufnum;
	bufnum = LocalBuf.new(2048, 1);
	chain = PV_PlayBuf(bufnum, recBuf, rate);
	signal = IFFT(chain);
	#freq, hasFreq = Pitch.kr(signal);
	kernel= WeaklyNonlinear.ar(signal,0,1,1,freq*2,0,0,-0.001,3,0,0);
	kernel2= WeaklyNonlinear2.ar(signal,0,1,1,freq*2,0,0,-0.001,3,0,0);
	signal2 = Convolution.ar(kernel+signal,kernel2, 2048, 0.0005);
	signal3 = Limiter.ar(signal2*adjVol) * EnvGen.kr(Env.asr(0.01, 1.0, 0.01), Amplitude.kr(signal));
	DetectSilence.ar(signal3, doneAction: 2);
	Out.ar(out, Pan2.ar(signal3, pan, amp*globamp));
	});
	
	SynthDef.writeOnce(\pvplayBet, {arg out=0, recBuf=1, rate=1.0, amp=1.0, adjVol=1, pan=0, globamp=1.0;
	var in, chain, signal, signal2, signal3, kernel, kernel2, freq, hasFreq, bufnum;
	bufnum = LocalBuf.new(2048, 1);
	chain = PV_PlayBuf(bufnum, recBuf, rate);
	signal = IFFT(chain);
	#freq, hasFreq = Pitch.kr(signal);
	kernel= WeaklyNonlinear.ar(signal,0,1,1,freq*2,0,0,-0.001,3,0,0);
	kernel2= LPCError.ar(signal, signal.linlin(-1,1,64,25));
	signal2 = Convolution.ar(kernel*3.5,kernel2*1.5, 2048, 0.0005);
	signal3 = Limiter.ar(signal2*adjVol) * EnvGen.kr(Env.asr(0.01, 1.0, 0.01), Amplitude.kr(signal));
	DetectSilence.ar(signal3, doneAction: 2);
	Out.ar(out, Pan2.ar(signal3, pan, amp*globamp));
	});
	
	SynthDef.writeOnce(\pvplayGrito, {arg out=0, amp=1.0, adjVol=0.9, thresh= 0.5, ratioDown=1, recBuf=4, rate=1, jefeBuf = 1, recBuf2 = 3, startPos = 0, osciOff=1, startOff=0, globamp=1.0, pan=0;
	 var input, freq, hasFreq, signal, signal2, input2, chain, output, osci, bufnum, bufnum2;
	 var gritos, elJefe, osci2, osci3;
	bufnum = LocalBuf.new(2048, 1);
	chain = PV_PlayBuf(bufnum, recBuf, rate);
	input = IFFT(chain);	
	#freq, hasFreq = Pitch.kr(input*8, 783.9908719635);
	osci = (freq/"g5".notecps);
	osci2 = osci.linlin(0,2,2,0) * osciOff; 
	bufnum2 = LocalBuf.new(2048, 1);
	chain =  PV_PlayBuf(bufnum2, recBuf2, osci2.min(1.5).max(0.75), startOff, 1);
	gritos = IFFT(chain, 1);
	elJefe = PlayBuf.ar(1, jefeBuf, startPos:  startPos, loop:1);
	input2 = PitchShift.ar((gritos + (elJefe/2)), 0.4, osci.min(1.5).max(0.75));
	output = HPF.ar(input2, 188);
	signal = Compander.ar(output, output,
		thresh: thresh,
		slopeBelow: 0.7,
		slopeAbove: 1.0,
		clampTime: 0.01,
		relaxTime: 0.01
	);
	signal2 = Limiter.ar(signal*adjVol) * EnvGen.kr(Env.asr(0.01, 1.0, (0.027*(osci*16))), Amplitude.kr(input))/2;
	DetectSilence.ar(signal2, doneAction: 2);
	Out.ar(out, Pan2.ar((signal2*amp).softclip(1), pan, globamp));
	});

	SynthDef.writeOnce(\detect,{arg out = 0, in = 0, thresh1=1, rate=200, lock=0, freq=0.1, micOn=0, impOn=0, initTempo=2.0, initImpOn=1.0;
	var signal, signal2, chain1, onsets1, trig, amplitude, buffer;
	var trackb,trackh,trackq,tempo;
	trig = Impulse.kr(rate);
	signal = SoundIn.ar(in);
	#trackb,trackh,trackq,tempo=AutoTrack.kr(signal, lock);
	signal2 = (signal*micOn)+(Impulse.ar(tempo)*impOn)+(Impulse.ar(initTempo)*initImpOn);
	buffer = LocalBuf.new(512, 1);
	chain1 = FFT(buffer, signal2);
	onsets1 = Onsets.kr(chain1, thresh1, \rcomplex);
	amplitude = Amplitude.kr(signal);
	SendTrig.kr(trig, 0, onsets1);
	SendTrig.kr(trig, 1, amplitude);
	SendTrig.kr(onsets1, 2, amplitude);
	SendTrig.kr(Impulse.kr(352),3,Decay.kr(trackb,0.05));
	SendTrig.kr(Impulse.kr(3),6,tempo);
	});
	
	SynthDef.writeOnce(\bang, {arg out=0, amp=0.5, recBuf=1, pan=0.0, rate=1, adjVol=1.0, lag=0.5, globamp=1.0;
	var signal, signal2, chain, bufnum;
	bufnum = LocalBuf.new(2048, 1);
	chain =  PV_PlayBuf(bufnum, recBuf, rate);
	signal = IFFT(chain, 1);
	DetectSilence.ar(signal, doneAction: 2);
	Out.ar(out, Pan2.ar(signal, pan, amp*globamp*adjVol));
	});

	SynthDef.writeOnce(\playWagner, {arg rate=1, gate=1, amp = 1.0, start=0, atk=0.1, dec=0.2, trig=1, pan=0, buffer=0, out=0, globamp=1.0, globpan=0;
	var signal, env;
	signal = PlayBuf.ar(1,buffer, rate, trig, startPos: start, loop:1); 
	env = EnvGen.kr(Env.asr(atk, 1.0, dec), gate, doneAction:2);
	Out.ar(out, Pan2.ar((signal*env), pan, amp*globamp));
	});

	SynthDef.writeOnce(\pvplayBuxta2, { arg out=0, recBuf=1,amp=1,gate=1,rate=1,dec=2.0, pan=0.0, globamp=1.0, globpan=0;
	var in, chain, signal, env, bufnum;
	bufnum = LocalBuf.new(2048, 1);
	chain = PV_PlayBuf(bufnum, recBuf, rate, 0, 0);
	signal = IFFT(chain, 1);
	env = EnvGen.kr(Env.asr(0.005, 1.0, dec), gate, doneAction:2);
	Out.ar(out, Pan2.ar(signal, pan+(globpan*2).min(1).max(-1), (amp*0.5)*globamp) * env);	
});
	
	}

}

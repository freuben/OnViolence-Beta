OnViolenceScore {var <>perfMode, headOut, adjScore, s, basicPath, <>score, globalMIDI, globalTimes, start, end, bufferTimes, selectPitch, buffType, algoTimes, <>stepPedal, <>chooseSamples, countPedalUp, countPedalDown,<>tempo=176, <>partials, <percglobalTimesGlob, partialTrig=0, document, <>bufferArr, <percArray, pedalUpOld, pedalDownOld, ccResponder, <>node, <>rightWin=1, clock, motorSound;

	*new {arg perfMode=\concert, headOut=0,adjScore= -11.7;
	^super.new.initOnViolenceScore(perfMode,headOut,adjScore);
	}
	
	initOnViolenceScore {arg modePerf=\concert,outHead=0,scoreAdj= -11.7;
		//arguments to variables
		perfMode = modePerf;
		headOut = outHead;
		adjScore = scoreAdj;
		
		s = Server.default;
		clock = AppClock;
		basicPath = Document.current.path.dirname;
		bufferArr = [Buffer.read(s, basicPath ++ "/AlgoScore/samples/hihat.wav"), 
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/woodblock.wav"), 
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/ride.wav"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/snarerim.aif"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/conga.aif"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/kick.wav"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/snare1.wav"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/cowbell.aif"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/low_tom.wav"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/splash.wav"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/crash.aif"),
		Buffer.read(s, basicPath ++ "/AlgoScore/samples/ride_open.wav")];
		
		{node = NodeProxy.audio(s, 2);
		node.play(headOut);
		0.1.yield;
		node.put(0, \synthPiano, extraArgs:[\note, rrand(60,80), \dur, 0.01,\amp,0]);
		node.put(1, \headmonitor, extraArgs:[\bufnum, bufferArr[0].bufnum, \vol,0]);
		0.5.yield;
		{this.displayScore(1);}.defer;
		}.fork;
		
		if(perfMode == \concert, {
		chooseSamples = [];
		//recive data through midi from computer 2
		//MIDIIn.noteOn = { arg src, chan, num, vel;  chooseSamples = chooseSamples.add(vel.postln); };
//		MIDIIn.noteOff = { arg src, chan, num, vel; 	
//			case
//			{num == 0} {{this.funcPage(vel);}
//			{num == 1} {countPedalUp = vel;}
//			{num == 2} {countPedalDown = vel;};
//		};
//			
//		MIDIIn.polytouch = { arg src, chan, num, vel; 	
//			case
//			{num == 1} {this.readyToStart;}
//			{num == 2} {{this.displayScore(vel,adjScore);}.defer}
//			{num == 3} {this.remove};
//		};
//		
//		MIDIIn.sysrt = { arg src, chan, val;  tempo = val; };
		}, {
		chooseSamples = Array.fill(26, {rrand(1,10)}); 
		chooseSamples = chooseSamples-1;
		
		motorSound = OnViolenceMotor(0,1);
		});
		
		partials = NodePT(512, 5);  //a partial tracker
		
		percArray = Array.fill(7, {rrand(0,9)}); //random numbers for rhythmic (clusters) sections
		
		//data for rythmic sections
		percglobalTimesGlob = (basicPath ++ "/data/AlgoScore/percTimes.rtf").loadPath;

	}
	
	readyToStart {	
	
		if(chooseSamples == [], {'ERROR: Array version not reciverd from other computer'.postln}, {chooseSamples = chooseSamples-1;});
		//MIDIIn.noteOn = nil;
		
				
	}
	
	pageFunc {arg page=1;
	
		case
		{page == 1} {countPedalUp = 0; countPedalDown = 0}
		{page == 2} {countPedalUp = 1; countPedalDown = 1}
		{page == 3} {countPedalUp = 2; countPedalDown = 1}
		{page == 4} {countPedalUp = 3; countPedalDown = 1}
		{page == 5} {countPedalUp = 5; countPedalDown = 2}
		{page == 6} {countPedalUp = 7; countPedalDown = 4}
		{page == 7} {countPedalUp = 8; countPedalDown = 5}
		{page == 8} {countPedalUp = 9; countPedalDown = 5}
		{page == 9} {countPedalUp = 11; countPedalDown = 7}
		{page == 10} {countPedalUp = 13; countPedalDown = 9}
		{page == 11} {countPedalUp = 16; countPedalDown = 12}
		{page == 12} {countPedalUp = 20; countPedalDown = 16}
		{page == 13} {countPedalUp = 26; countPedalDown = 21}
		{page == 14} {countPedalUp = 32; countPedalDown = 27}
		{page == 15} {countPedalUp = 38; countPedalDown = 33}
		{page == 16} {countPedalUp = 41; countPedalDown = 36}
		{page == 17} {countPedalUp = 42; countPedalDown = 37}
		
		{(18..30).includes(page)} {countPedalUp = page+25; countPedalDown = page+19};
	}
	
	displayScore {arg page=1;
		partials.startsynth(1); //use AudioIn to track loadest partials
		if(chooseSamples != [], {score = AlgorithmicScore.screenBounds;});
		this.funcPage(page);
		score.w.view.keyDownAction = {arg view, char, modifiers; 
		case
		{char == $p} {}
		{char == $q} {this.pedalFwd}
		{char == $a} {this.pedalBck}
		
		{char == $w} {this.pedalHigh}
		{char == $s} {this.pedalLow}
		 };
	 	
	 	score.click4(winAdd: 0, leftWin: 20, scaleSize:0.4, name:"pedal");
		this.rightWin = 1.2;
		{0.2.yield;
		{score.w5.alwaysOnTop;}.defer;
		0.2.yield;
		this.startPedals;
		2.6.yield;
		this.getPartials;
		}.fork;
	
	}
	
	displayPage {arg page=1;
	this.funcPage(page);
	}
	
	getPartials {
		partials.getarrays;
	}
	
	partialNotes {arg time=1.0, page=1, low=60, high=84, expression="P", perc=1;
		var notes;
		partialTrig = perc;
		notes = partials.frequencies.cpsmidi.round(1);
		notes.do({|item, index|
		case
		{item > high} {notes[index] = high-(12-item.midinoteclass)}
		{item < low} {notes[index] = low+(item.midinoteclass)};
		});
		{
		score.score([\piano], 1, 1.3);
		score.notes(notes, (0..(notes.size)));
		score.timer(time*0.833333, rightWin:rightWin);
		if(perc == 1, {
		score.text("Improvise with pitch material", "Helvetica", 50, 0.5, 0.7);
		});
		score.expression(expression);
		time.yield;
		this.funcPage(page);
		score.textClose;
		score.expressionClose;
		partialTrig = 0;
		}.fork(clock);
	}
	
	textOnly {arg string="SILENCE", time=1.0,  page=32, perc=1;
		{
		if(score.picture.notNil, {score.picture.free});
		score.w.refresh;
		score.text(string, "Helvetica", 90, 1, 1);
		score.timer(time*0.833333, rightWin:rightWin);
		time.yield;
		if(perc == 1, {
		this.funcPage(page);
		});
		score.textClose;
		}.fork(clock);
	}
	
	timerOnly {arg time=1.0;
		{
		score.timer(time*0.833333, rightWin:rightWin);
		time.yield;
		score.tag("Pedal");
		}.fork(clock);
		
	}
	
	partialNum {arg number=10;
		partials.numpartials(number);
	}
	
	funcSample {arg sample;
		var pitchData;
		pitchData = (basicPath ++ "/AlgoScore/data/pitchData.rtf").loadPath[sample-1];
		
		globalMIDI = pitchData[0];
		globalTimes = pitchData[1]; 
		start = pitchData[2];
		end = pitchData[3];

	}

	funcChooseSample {arg sampleIndex = 0;
		var globalTimes2, globalTimes3, globalTimes4, indexesOf;
		
		bufferTimes = [ 0.025, 0.039185800697256, 0.061421079051404, 0.096273366492749, 0.15090195807355, 0.21139096075449, 0.33134096229308, 0.51935443645014, 0.81405277751885, 1.2759723958761, 2 ];
		
		bufferTimes = bufferTimes.roundUp(0.3);
		
		globalTimes2 = globalTimes[sampleIndex].flat.evenIndex;
		globalTimes3 = globalTimes[sampleIndex].flat.evenIndex.sort;
		
		indexesOf = globalTimes2.indexOfAll(globalTimes3);
		
		globalTimes4 = globalTimes[sampleIndex].itemsAt(indexesOf);
		
		selectPitch = globalMIDI[sampleIndex].itemsAt(indexesOf);
		
		buffType = [];
		globalTimes4.do({|item| buffType = buffType.add(item[1]); });
		
		algoTimes = [];
		globalTimes4.do({|item| algoTimes = algoTimes.add(item[0]); });
		algoTimes = algoTimes.differentiate;
	}

	funcPage {arg page=0; 
		{score.image((basicPath ++ "/AlgoScore/score/onviolencescore" ++ page.asString ++ ".jpg"), 1.1, -10);}.defer;
		stepPedal = page;
		this.pageFunc(page);
	}

	//use percussion sounds as triggers
	funcAlgoScore {arg sample=1,page=1, tempo=176, repeat=1,pagePartial=0;
		var step=0, color, tempo2, sampleEnd;
		{ 
		partialTrig = 1;
		sampleEnd = sample;
		repeat.do({
		tempo2 = 176/tempo;
		this.funcSample(sampleEnd);
		this.funcChooseSample(chooseSamples[sampleEnd-1]); //rrand(0,9)
		color = Array.fill(selectPitch.size, {\black});
		{score.score([\piano], 1, 1.3);
		score.expression("f");
		score.notes(selectPitch, (0,1..selectPitch.size), color);
		score.timer((start+end)*tempo2, 0.99, rightWin:rightWin);}.defer;
		(start*tempo2).yield;
		selectPitch.size.do({
		(algoTimes[step]*tempo2).yield;
		color = Array.fill(selectPitch.size, {\black});
		color[(0..selectPitch.size)[step]] = \red;
		{score.notes(selectPitch, (0,1..selectPitch.size), color);}.defer;
		node.spawn([\bufnum, bufferArr[buffType[step]].bufnum], 1);
		step = step + 1;
		});
		((bufferTimes[buffType[step-1]])*tempo2).yield;
		color = Array.fill(selectPitch.size, {\black});
		{score.notes(selectPitch, (0,1..selectPitch.size), color);}.defer;
		(end-algoTimes.sum-(bufferTimes[buffType[step-1]])*tempo2).yield;
		sampleEnd = sampleEnd + 1;
		step = 0;
		{score.expressionClose;}.defer;
		});
		if(pagePartial == 0, {this.funcPage(page); partialTrig = 0;}, {this.partialNotes(5.47*tempo2,page,"c 4".notemidi,"c 6".notemidi, "f")});
		}.fork;
	}

		//send midi instead of using percussion sounds
	funcAlgoScoreMIDI {arg sample=1,page=1, tempo=176, repeat=1,pagePartial=0;
		var step=0, color, tempo2, sampleEnd, bufferTimesMIDI,pattMIDI;
		bufferTimesMIDI = [ 0.025, 0.039185800697256, 0.061421079051404, 0.096273366492749, 0.15090195807355, 0.21139096075449, 0.33134096229308, 0.51935443645014, 0.81405277751885, 1.2759723958761, 2 ];
		pattMIDI = Pseq((0,1..16), inf).asStream;
		{ 
		partialTrig = 1;
		sampleEnd = sample;
		repeat.do({
		tempo2 = 176/tempo;
		this.funcSample(sampleEnd);
		this.funcChooseSample(chooseSamples[sampleEnd-1]); //rrand(0,9)
		color = Array.fill(selectPitch.size, {\black});
		{score.score([\piano], 1, 1.3);
		score.expression("f");
		score.notes(selectPitch, (0,1..selectPitch.size), color);
		score.timer((start+end)*tempo2, 0.99, rightWin:rightWin);}.defer;
		(start*tempo2).yield;
		selectPitch.size.do({
		(algoTimes[step]*tempo2).yield;
		color = Array.fill(selectPitch.size, {\black});
		color[(0..selectPitch.size)[step]] = \red;
		{score.notes(selectPitch, (0,1..selectPitch.size), color);}.defer;
		node.spawn([\note, selectPitch[step], \dur, 0.5+(bufferTimesMIDI[buffType[step]])], 0);
		step = step + 1;
		});
		((bufferTimes[buffType[step-1]])*tempo2).yield;
		color = Array.fill(selectPitch.size, {\black});
		{score.notes(selectPitch, (0,1..selectPitch.size), color);}.defer;
		(end-algoTimes.sum-(bufferTimes[buffType[step-1]])*tempo2).yield;
		sampleEnd = sampleEnd + 1;
		step = 0;
		{score.expressionClose;}.defer;
		});
		if(pagePartial == 0, {this.funcPage(page); partialTrig = 0;}, {this.partialNotes(5.47*tempo2,page,"c 4".notemidi,"c 6".notemidi, "f")});
		}.fork;
	}

	funcPerc {arg percWhich=0, percsample = 4, perctempo = 176, adjEnd = 1, withPedal = 1;
		var step=0,percglobalTimes,percbufferTimes, percglobalTimes2, percglobalTimes3, percindexesOf,percglobalTimes4,percbuffType, percalgoTimes,percstart,percend;
		
		percglobalTimes = percglobalTimesGlob[percWhich];
		
		percstart = [0.24318181818182,0.053636363636372,0.27500000000001,0.28136363636364,0.33454545454546, 0.12500000000001,0.026363636363641, 0, 0];
		percend = [2.825,2.3327272727273,2.7931818181818,4.1504545454545,2.0518181818182,2.2613636363636, 10.541818181818, 3.06289, 7.7944];
		
		percbufferTimes = [ 0.025, 0.039185800697256, 0.061421079051404, 0.096273366492749, 0.15090195807355, 0.21139096075449, 0.33134096229308, 0.51935443645014, 0.81405277751885, 1.2759723958761, 2 ];
		
		percglobalTimes2 = percglobalTimes[percsample].flat.evenIndex;
		percglobalTimes3 = percglobalTimes[percsample].flat.evenIndex.sort;
		
		percindexesOf = percglobalTimes2.indexOfAll(percglobalTimes3);
		
		percglobalTimes4 = percglobalTimes[percsample].itemsAt(percindexesOf);
		
		percbuffType = [];
		percglobalTimes4.do({|item| percbuffType = percbuffType.add(item[1]); });
		
		percalgoTimes = [];
		percglobalTimes4.do({|item| percalgoTimes = percalgoTimes.add(item[0]); });
		percalgoTimes = percalgoTimes.differentiate;
		
		{score.timer((percalgoTimes.sum*adjEnd)*(176/perctempo), rightWin:rightWin);
		score.click1(name:"Trigger");
		score.click1CloseTime(percstart[percWhich]+percend[percWhich]*(176/perctempo));}.defer;
		
		{
		percstart[percWhich].yield;
		percalgoTimes.size.do({
		{score.click1Note(percbufferTimes[percbuffType[step]]);}.defer;
		node.spawn([\bufnum, bufferArr[percbuffType[step]].bufnum], 1);
		(percalgoTimes[step]*(176/perctempo)).yield;
		step = step + 1;
		});
		(percend[percWhich]-(percalgoTimes.sum+percstart[percWhich])*(176/perctempo)).yield;
		if(withPedal == 1, {{score.tag("Pedal");}.defer;});
		}.fork;
	}

	funcPercMIDI {arg percWhich=0, percsample = 4, perctempo = 176, adjEnd = 1, withPedal = 1;
		var step=0,percglobalTimes,percbufferTimes, percglobalTimes2, percglobalTimes3, percindexesOf,percglobalTimes4,percbuffType, percalgoTimes,percstart,percend;
		
		percglobalTimes = percglobalTimesGlob[percWhich];
		
		percstart = [0.24318181818182,0.053636363636372,0.27500000000001,0.28136363636364,0.33454545454546, 0.12500000000001,0.026363636363641, 0, 0];
		percend = [2.825,2.3327272727273,2.7931818181818,4.1504545454545,2.0518181818182,2.2613636363636, 10.541818181818, 3.06289, 7.7944];
		
		percbufferTimes = [ 0.025, 0.039185800697256, 0.061421079051404, 0.096273366492749, 0.15090195807355, 0.21139096075449, 0.33134096229308, 0.51935443645014, 0.81405277751885, 1.2759723958761, 2 ];
		
		percglobalTimes2 = percglobalTimes[percsample].flat.evenIndex;
		percglobalTimes3 = percglobalTimes[percsample].flat.evenIndex.sort;
		
		percindexesOf = percglobalTimes2.indexOfAll(percglobalTimes3);
		
		percglobalTimes4 = percglobalTimes[percsample].itemsAt(percindexesOf);
		
		percbuffType = [];
		percglobalTimes4.do({|item| percbuffType = percbuffType.add(item[1]); });
		
		percalgoTimes = [];
		percglobalTimes4.do({|item| percalgoTimes = percalgoTimes.add(item[0]); });
		percalgoTimes = percalgoTimes.differentiate;
		
		score.timer((percalgoTimes.sum*adjEnd)*(176/perctempo), rightWin:rightWin);
		score.click1(name:"Trigger");
		score.click1CloseTime(percstart[percWhich]+percend[percWhich]*(176/perctempo));
		
		{
		percstart[percWhich].yield;
		percalgoTimes.size.do({
		{score.click1Note(percbufferTimes[percbuffType[step]]);}.defer;
		node.spawn([\bufnum, bufferArr[percbuffType[step]].bufnum], 1);
		(percalgoTimes[step]*(176/perctempo)).yield;
		step = step + 1;
		});
		(percend[percWhich]-(percalgoTimes.sum+percstart[percWhich])*(176/perctempo)).yield;
		if(withPedal == 1, {{score.tag("Pedal");}.defer;});
		}.fork;
	}

	closeTag {	
		score.tagClose;
	}

	practiceAlgoScore {arg sample=1,page=1, tempo=176, repeat=1;
		var step=0, color, tempo2, sampleEnd;
		sampleEnd = sample;
		this.funcSample(sampleEnd);
		this.funcChooseSample(chooseSamples[sampleEnd-1]); //rrand(0,9)
		score.score([\piano], 1, 1.3);
		color = Array.fill(selectPitch.size, {\black});
		score.notes(selectPitch, (0,1..selectPitch.size), color);
	}
	
	pedalLow {
		if(partialTrig == 0, {
		countPedalDown = countPedalDown + 1;
		
		if(countPedalDown != pedalDownOld, {
		'pedalDown '.post; countPedalDown.postln;
		if((38..59).includes(countPedalDown), {countPedalUp = countPedalDown+5;});
		case
		{countPedalDown == 51} {this.funcPerc(0, percArray[0], tempo);}
		{countPedalDown == 52} {this.funcPerc(1, percArray[1], tempo);}
		{countPedalDown == 53} {this.funcPerc(2, percArray[2], tempo);}
		{countPedalDown == 54} {this.funcPerc(3, percArray[3], tempo);}
		{countPedalDown == 55} {this.funcPerc(4, percArray[4], tempo); this.partialNum(24);}
		{countPedalDown == 56} {this.funcPerc(5, percArray[5], tempo); this.partialNum(9);}
		{countPedalDown == 57} {this.funcPerc(6, percArray[6], tempo, 0.85); this.partialNum(19);}
		{countPedalDown == 58} {this.funcPerc(7, 0, tempo); this.partialNum(24);}
		{countPedalDown == 59} {this.timerOnly(15.0*(176/tempo))};
		});
		pedalDownOld = countPedalDown;	
		});
	}
	
	pedalHigh {
		countPedalUp = countPedalUp + 1;
		
		if(countPedalUp != pedalUpOld, {
		'pedalUp '.post; countPedalUp.postln;
		
		if((43..100).includes(countPedalUp), {(countPedalDown = countPedalUp-6);});
		case
		{countPedalUp == 1} {this.funcPage(2);}
		{countPedalUp == 2} {this.funcPage(3);}
		{countPedalUp == 3} {this.funcPage(4);}
		{countPedalUp == 5} {this.funcPage(5);}
		
		{countPedalUp == 7} {this.funcPage(6);}
		{countPedalUp == 8} {this.funcPage(7);}
		{countPedalUp == 9} {this.funcPage(8);}
		{countPedalUp == 11} {this.funcPage(9);}
		{countPedalUp == 13} {this.funcPage(10);}
		{countPedalUp == 16} {this.funcPage(11);}
		{countPedalUp == 20} {this.funcPage(12);}
		{countPedalUp == 26} {this.funcPage(13);}
		{countPedalUp == 32} {this.funcPage(14);}
		{countPedalUp == 38} {this.funcPage(15);}
		{countPedalUp == 41} {this.funcPage(16);}
		{countPedalUp == 42} {this.funcPage(17);}
		
		//algoScore
		{countPedalUp == 43} {this.funcAlgoScoreMIDI(1,18,tempo);}
		{countPedalUp == 44} {this.funcAlgoScoreMIDI(2,19,tempo);}
		{countPedalUp == 45} {this.funcAlgoScoreMIDI(3,20,tempo,2);}
		{countPedalUp == 46} {this.funcAlgoScoreMIDI(5,21,tempo);}
		{countPedalUp == 47} {this.funcAlgoScoreMIDI(6,22,tempo, 2);}
		{countPedalUp == 48} {this.funcAlgoScoreMIDI(8,23,tempo);}
		{countPedalUp == 49} {this.funcAlgoScoreMIDI(9,24,tempo);}
		{countPedalUp == 50} {this.funcAlgoScoreMIDI(10,25,tempo, 2);}
		{countPedalUp == 51} {this.funcAlgoScoreMIDI(12,26,tempo,3);}
		{countPedalUp == 52} {this.funcAlgoScoreMIDI(15,27,tempo,2);}
		{countPedalUp == 53} {this.funcAlgoScoreMIDI(17,28,tempo,1);}
		{countPedalUp == 54} {this.funcAlgoScoreMIDI(18,29,tempo,2);}
		{countPedalUp == 55} {this.funcAlgoScoreMIDI(20,30,tempo,2);}
		{countPedalUp == 56} {this.funcAlgoScoreMIDI(22,31,tempo,1);}
		{countPedalUp == 57} {score.tagClose; this.funcAlgoScoreMIDI(23,31,tempo,1);}
		{countPedalUp == 58} {score.tagClose; this.funcAlgoScoreMIDI(24,31,tempo,1);}
		{countPedalUp == 59} {score.tagClose; this.funcAlgoScoreMIDI(25,32,tempo,1,1); this.partialNum(18);}
		{countPedalUp == 60} {score.tagClose; this.funcAlgoScoreMIDI(26,32,tempo,1);}
		{countPedalUp == 61} {score.tagClose; this.partialNotes(7.17*(176/tempo),32,"c 3".notemidi,"c 6".notemidi, "F");}
		{countPedalUp == 62} {score.tagClose; this.partialNotes(3.07*(176/tempo),32,"c 2".notemidi,"c 7".notemidi, "F");}
		{countPedalUp == 63} {score.tagClose; this.partialNotes(6.42*(176/tempo),33,"c 2".notemidi,"c 7".notemidi, "P");}
		{countPedalUp == 64} {score.tagClose; this.partialNotes(7.5*(176/tempo),34,"c 2".notemidi,"c 7".notemidi, "p", 0); this.funcPerc(8, 0, tempo, 1, 0);}
		{countPedalUp == 65} {score.tagClose; this.funcPage(35)}
		{countPedalUp == 66} { this.textOnly("FREE IMPROV", 37.5*(176/tempo), 36, 0) };
		});
			pedalUpOld = countPedalUp;
	
			this.arrangePedal;
	}
	
	pedalFwd {
		stepPedal = stepPedal + 1;	this.funcPage(stepPedal);
	}
	
	pedalBck {
		stepPedal = stepPedal - 1;	this.funcPage(stepPedal);
	}
	
	arrangePedal {
		//reads pedal numbers and arranges them
		case
		{countPedalUp == 0}{countPedalDown = 0}
		{(1..4).includes(countPedalUp)}{countPedalDown = 1}
		{(5..8).includes(countPedalUp)}{countPedalDown = countPedalUp - 3}
		{(9..20).includes(countPedalUp)}{countPedalDown = countPedalUp - 4}
		{(21..42).includes(countPedalUp)}{countPedalDown = countPedalUp - 5}
		{(43..56).includes(countPedalUp)}{countPedalDown = countPedalUp - 6};
		
		//countPedalDown.postln;
		}
	
	startPedals {
		if(perfMode == \concert, {
		
		//ebody...
		//ccResponder = CCResponder({ |src,chan,num,value|
//		if(value == 0, {
//		case
//		{num == 1} {this.pedalLow}
//		{num == 2} {this.pedalHigh};
//		});
//		});
	
		}, {
		
		//ebody...
		ccResponder = CCResponder({ |src,chan,num,value|
		case
		{(num == 1).and(value == 0)} {this.pedalLow; this.score.click4Note(0.05);}
		{(num == 2).and(value == 0)} {this.pedalHigh; this.score.click4Note(0.05);};
		
		});
		});
		
	}
	
	removePedals {
	if(perfMode == \concert, {
	ccResponder.remove;
	}, {
	ccResponder.remove;
	});
	}

	//turns keyboard on instead of midi pedal
	keyboardPedal {
		document = Document.new("");
		document.keyDownAction_({arg doc, key, modifiers, keycode;
		
		case
		//instead of pedals use keyboard:
		//when 's' is pressed:
		{keycode == 115} {this.pedalFwd}
		//when 'w' is pressed:
		{keycode == 119} {this.pedalBck}
		//pedals for sensors, algorithmic score, pages:
		//when 'a' is pressed:
		{keycode == 97} {this.pedalLow}
		//when 'q' is pressed:
		{keycode == 113} {this.pedalHigh};
		
		});
			
	}

	//turns keyboard off - when used instead of pedal
	keyboardpedalDown {
		document.keyDownAction_(nil);
		document.close;
	}

	remove {	
		score.close;	
		SystemClock.clear;
	}
	
	//sensors:
	
	
	
	
	*initClass {
		SynthDef.writeOnce("headmonitor", {arg bufnum=0, out=0, vol = 1.0, gates = 1, dec=1.0, pan=0, dur=1.0;
		var signal, signal2, env; 
		env = EnvGen.kr(Env.linen(0.0001, dur, dec, 1.0, -4), gates, doneAction: 2);
		signal = PlayBuf.ar(1, bufnum, 1, doneAction: 2);
		signal2 = signal*env;
		Out.ar(out, Pan2.ar(signal2*vol, 0));
		});
		
		SynthDef.writeOnce("synthPiano", {arg note = 36, out=0, dur=1, amp=1; 
		var delayTime, detune, strike, hammerEnv, hammer, signal, env;
		env = EnvGen.kr(Env.linen(0.01, dur, 0.1), doneAction:2);
		hammerEnv = Decay2.ar(Impulse.ar(0.1), 0.008, 0.04); 
		signal = Mix.ar(Array.fill(3, { arg i;
		detune = #[-0.05, 0, 0.04].at(i);
		delayTime = 1 / (note + detune).midicps;
		hammer = LFNoise2.ar(3000, hammerEnv); 
		CombL.ar(hammer, delayTime, delayTime, dur) }));
		Out.ar(out, Pan2.ar(signal*env, (note - 36)/27 - 1, amp));
		});
	}
	
}
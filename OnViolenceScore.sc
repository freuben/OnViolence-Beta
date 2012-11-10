OnViolenceScore {var <>headOut, <>motorOut, <>motorVol, <>motorPan, s, basicPath, <>score, globalMIDI, globalTimes, start, end, bufferTimes, selectPitch, buffType, algoTimes, <>stepPedal, countPedalUp, countPedalDown,<>tempo=1, <>partials, <percglobalTimesGlob, partialTrig=0, document, <>bufferArr, <>node, <>rightWin, clock, <motorSound, movwin, <>src, <>imageScale, <>imageAdj, movieScale, movieWinScale, <>network, snapshot, <>pedalTime, pedalDownMistake, pedalUpMistake, netConnect, <>rrandArray, <>muteMovie=false;
	
	*new {arg headOut=0, motorOut=0, motorVol=3, panVal=0, lowVal=40, highVal=50, leftVal=49, rightVal=31, src, scoreType=\macBookPro15, connect=false, hostcomputer="tremmac56150", port=57120;
	^super.new.initOnViolenceScore(headOut, motorOut, motorVol, panVal, lowVal, highVal, leftVal, rightVal, src, scoreType, connect, hostcomputer, port);
	}
	
	initOnViolenceScore {arg outHead, outMotor, volMotor, panMotor, lowVal, highVal, leftVal, rightVal, srcID, scoreType, connect, hostcomputer, port;
		var networkError;
		networkError = false;
		//arguments to variables
		headOut = outHead;
		motorOut = outMotor;
		motorVol = volMotor;
		motorPan = panMotor;
		src = srcID;
		src ?? {src = -1927836118};
		s = Server.default;
		clock = AppClock;
		pedalDownMistake = false;
		pedalUpMistake = false;
		pedalTime=0.5;
		
		basicPath =  Document.standardizePath("~/Library/Application Support/SuperCollider/Extensions/FedeClasses/OnViolence/");
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
		
		partials = NodePT(512, 5);  //a partial tracker
		
		netConnect = connect;
		
		if(netConnect, {
			if(NetAddr.langPort != 57120, {("SC Language port in not 57120, close other open applications and restart SuperCollider").warn; networkError=true;}, {
		this.connect(hostcomputer, port);
			});
		});
		
		//data for rythmic sections
		percglobalTimesGlob = (basicPath ++ "/data/AlgoScore/percTimes.rtf").loadPath;
		
		if(networkError.not, {
		{node = NodeProxy.audio(s, 2);
		node.play(headOut);
		0.1.yield;
		node.put(0, \synthPiano, extraArgs:[\note, rrand(60,80), \dur, 0.01,\amp,0]);
		node.put(1, \headmonitor, extraArgs:[\bufnum, bufferArr[0].bufnum, \vol,0]);
		0.5.yield;
		this.displayScore(1, scoreType);
		0.1.yield;
		
		motorSound = OnViolenceMotor(motorOut, motorVol, motorPan, lowVal, highVal, leftVal, rightVal, rectArr: ([ -40, (((score.w.bounds.height/2) - (252*score.resize/2+60)))] ++ ([ 21, 252 ]*score.resize)) );
		
		}.fork(clock);
		});
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
		
		{(18..45).includes(page)} {countPedalUp = page+25; countPedalDown = page+19}
		{(46..47).includes(page)} {countPedalUp = page+25; countPedalDown = page+18}
		{(48..54).includes(page)} {countPedalUp = page+25; countPedalDown = page+17}
		
		{page == 55} {countPedalUp = 80; countPedalDown = 73}
		
		;
	}
	
	displayScore {arg page=1, type=\macBookPro15;
		var gowin, gotext, gofunc, clickPos;
		partials.startsynth(1); //use AudioIn to track loadest partials
		
		case
		{type == \macBookPro15} {
			score = AlgorithmicScore.screenBounds("On Violence");
			imageScale = 1.1;
			imageAdj = -10;
			clickPos = 20;
			movieScale = 1.6;
			movieWinScale = 1
			}
		{type == \macBookAir11} {
			score = AlgorithmicScore.screenBounds("On Violence", 1.2);
			imageScale = 0.9;
			imageAdj = -35;
			clickPos = 14;
			movieScale = 1.2;
			movieWinScale = 0.8;
			}
		{type == \macBookAir11Sim} {
			score = AlgorithmicScore.screenSet("On Violence", 0,0,1362,728,1.2);
			imageScale = 0.9;
			imageAdj = -35;
			clickPos = 13.16;
			movieScale = 1.2;
			movieWinScale = 0.8;
		};
		
		this.funcPage(page);
	 	
	 	gofunc = {arg view, char, modifiers; 
		case
		{char == $p} { 
			gowin = Window("", Rect((score.w.bounds.width/2),(score.w.bounds.height/2),120,150)).front;
			gowin.addFlowLayout( 10@10, 20@5 );
			StaticText(gowin, 100@20).string_("Go to Page:");
			gotext = TextField(gowin, 60@20);
			gotext.action = {arg field; "Page: ".post; field.value.postln; 
			this.displayPage(field.value.asInteger); 
			gowin.close;
			};
			gotext.focus;
			}
		{char == $f} {this.pedalFwd}
		{char == $b} {this.pedalBck}
		
		{char == $u} {this.pedalHigh; this.score.click4Note(0.05);}
		{char == $d} {this.pedalLow; this.score.click4Note(0.05);}
		
		{char == $m} {{this.hide}.defer}
		
		{char == $t} { 
			gowin = Window("", Rect((score.w.bounds.width/2),(score.w.bounds.height/2),120,150)).front;
			gowin.addFlowLayout( 10@10, 20@5 );
			StaticText(gowin, 100@20).string_("New Tempo:");
			gotext = TextField(gowin, 60@20);
			gotext.string = "1";
			gotext.action = {arg field; "Tempo: ".post; field.value.postln; 
			tempo = field.value.asFloat; 
			gowin.close;
			};
			gotext.focus;
			}
		
		};
		 
	 	{
		0.2.yield;
	 	score.click4(winAdd: 0, leftWin: clickPos, scaleSize:0.4, name:"pedal");
		rightWin = 1.2;
		0.2.yield;
		score.w.view.keyDownAction = gofunc;
		score.w5.alwaysOnTop = true;
		score.w5.view.keyDownAction = gofunc;	
		0.2.yield;
		this.startPedals;
		0.5.yield;
		this.getPartials;
		}.fork(clock);
	
	}
	
	connect {arg hostcomputer="tremmac56150", port;
		var ip, stringArr;
		
	port ?? {port = 57120};
	ip = hostcomputer.ipAddr(\ethernet);
	if(ip.notNil, {
	network = NetAddr(hostcomputer.ipAddr, port);
	OSCdef(\tempotrack, {|msg, time, addr, recvPort| tempo = msg[1].postln}, '/tempo', network);
	OSCdef(\randArr, {|msg, time, addr, recvPort| 
		"Random array recieved:".postln;
		stringArr = msg[1].postln;
		stringArr = stringArr.asString.findReplaceAll(" ");
		stringArr = stringArr.findReplace("[","").findReplace("]","");
		rrandArray = stringArr.split($,);
		}, '/randArr', network); 
	}, {
	Error("Connection Failed").throw;
	}); 
	}
	
	displayPage {arg page=1;
	this.funcPage(page);
	}
	
	getPartials {
		partials.getarrays;
	}
	
	partialNotes {arg time=1.0, page=1, low=60, high=84, expression="P", displayNext=true;
		var notes, pagetag;
		
		if(network.notNil, {
			case
			{page == 34}{pagetag = 'partials 1'}
			{page == 36}{pagetag = 'partials 2'}
			{page == 37}{pagetag = 'partials 3'}
			{page == 38}{pagetag = 'partials 4'}
			{page == 39}{pagetag = 'partials 5'}
			{page == 49}{pagetag = 'partials 6'}
			{page == 51}{pagetag = 'partials 7'};
			
		network.sendMsg('/page', pagetag);
		});

		partialTrig = 1;
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
		if(displayNext, {
		score.text("Improvise with pitch material", "Helvetica", 50, 0.5, 0.7);
		}, {
		score.text("Improvise with pitch and rhythm", "Helvetica", 50, 1, 0.9);
		});
		score.expression(expression);
		("partialNotes: : time : ").post;
		time.postln;
		time.yield;
		this.funcPage(page);
		score.textClose;
		score.expressionClose;
		partialTrig = 0;
		}.fork(clock);
	}
	
	textOnly {arg string="SILENCE", time=1.0,  page=32, displayNext=true, fontSize=90;
		var pagetag;
		
		partialTrig = 1;
		
		if(network.notNil, {
			case
			{page == 41}{pagetag = 'improv 1'}
			{page == 45}{pagetag = 'improv 2'}
			{page == 56}{pagetag = 'end'}
			;
			
		network.sendMsg('/page', pagetag);
		});
		
		{
		if(score.picture.notNil, {score.picture.free});
		score.w.refresh;
		score.text(string, "Helvetica", fontSize, 1, 1);
		score.timer(time*0.833333, rightWin:rightWin);
		time.yield;
		if(displayNext, {
		this.funcPage(page);
		});
		score.textClose;
		partialTrig = 0;
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
		pitchData = (basicPath ++ "/data/AlgoScore/pitchData.rtf").loadPath[sample-1];
		
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
		{score.image((basicPath ++ "/AlgoScore/score/onviolencescore" ++ page.asString ++ ".jpg"), imageScale, imageAdj);}.defer;
		stepPedal = page;
		this.pageFunc(page);
		if(network.notNil, {
		network.sendMsg('/page', page);
		});
		if(motorSound.notNil, {
		if(page < 16, {
		{if(motorSound.window.visible.not, {motorSound.window.visible = true;});}.defer; 
		}, {
		{if(motorSound.window.visible, {motorSound.window.visible = false;});}.defer;
		});	
		});
	}

	
	funcAlgoScore {arg sample=1,page=1, tempo1=1, repeat=1,pagePartial=0;
		var step=0, color, tempo2, sampleEnd, bufferTimesMIDI,randNum;

		bufferTimesMIDI = [ 0.025, 0.039185800697256, 0.061421079051404, 0.096273366492749, 0.15090195807355, 0.21139096075449, 0.33134096229308, 0.51935443645014, 0.81405277751885, 1.2759723958761, 2 ];
		
				
		{if(motorSound.window.visible, {motorSound.window.visible = false;});}.defer;

		{ 
		partialTrig = 1;
		sampleEnd = sample;
		repeat.do({
		tempo2 = 1/tempo1;
		this.funcSample(sampleEnd);
		
		//score algo choices
		if(netConnect, {
		if(rrandArray.notNil, {
		randNum = (rrandArray[sampleEnd-1].asInteger);
		}, { randNum = rrand(0,9);});
		}, {
		randNum = rrand(0,9);
		});
		
		//send network message
		if(network.notNil, {
		network.sendMsg('/page', ('algoScore ' ++ sample ++ ' rand ' ++ randNum));
		});
		
		this.funcChooseSample(randNum); 
		color = Array.fill(selectPitch.size, {\black});
		{score.score([\piano], 1, 1.3);
		score.expression("f");
		score.notes(selectPitch, (0,1..selectPitch.size), color);
		("funcAlgoScore: " ++ sample ++ " : time : ").post;
		score.timer((start+end).postln*tempo2, 0.99, rightWin:rightWin);}.defer;
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
		case
		{pagePartial == 0} {this.funcPage(page); partialTrig = 0;}
		{pagePartial == 1} {this.partialNotes(5.47*tempo2,page,"c 4".notemidi,"c 6".notemidi, "f")}
		{pagePartial == 2} {this.partialNotes(7.69*tempo2,51,"c 2".notemidi,"c 7".notemidi, "P")} //11.83
		}.fork;
	}

	
	funcPerc {arg percWhich=0, percsample = 4, perctempo = 1, adjEnd = 1, withPedal = 1;
		var step=0,percglobalTimes,percbufferTimes, percglobalTimes2, percglobalTimes3, percindexesOf,percglobalTimes4,percbuffType, percalgoTimes,percstart,percend;
		
		if(network.notNil, {
		network.sendMsg('/page', ('perc ' ++ percWhich ++ ' rand ' ++ percsample));
		});
		
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

		{score.timer((percalgoTimes.sum*adjEnd)*(1/perctempo), rightWin:rightWin);
		score.click1(name:"Trigger");
		("funcPerc: " ++ percWhich ++ " : time : ").post;
		score.click1CloseTime((percstart[percWhich]+percend[percWhich]*(1/perctempo)).postln);}.defer;

		{
		percstart[percWhich].yield;
		percalgoTimes.size.do({
		{if(percbuffType[step].notNil, 
			{score.click1Note(percbufferTimes[percbuffType[step]])});
		}.defer;
		node.spawn([\bufnum, bufferArr[percbuffType[step]].bufnum], 1);
		(percalgoTimes[step]*(1/perctempo)).yield;
		step = step + 1;
		});
		(percend[percWhich]-(percalgoTimes.sum+percstart[percWhich])*(1/perctempo)).yield;
		if(withPedal == 1, {{score.tag("Pedal");}.defer;});
		}.fork;
	}
	
	closeTag {	
		score.tagClose;
	}
	
	pedalLow {
		if(partialTrig == 0, {
			
		if(pedalDownMistake.not, {
				
		{pedalDownMistake = true;
		pedalTime.yield; 
		pedalDownMistake = false;
		}.fork;		
				
		countPedalDown = countPedalDown + 1;
		
		'pedalDown '.post; countPedalDown.postln;
		
		if(network.notNil, {
		network.sendMsg('/pedalDown', countPedalDown);
		});
		
		//if((38..59).includes(countPedalDown), {countPedalUp = countPedalDown+5;});
		
		if((2..36).includes(countPedalDown), {motorSound.pedalOff;});
		
		case
		{countPedalDown == 51} {this.funcPerc(0, rrand(0,9), tempo);}
		{countPedalDown == 52} {this.funcPerc(1, rrand(0,9), tempo);}
		{countPedalDown == 53} {this.funcPerc(2, rrand(0,9), tempo);}
		{countPedalDown == 54} {this.funcPerc(3, rrand(0,9), tempo);}
		{countPedalDown == 55} {this.funcPerc(4, rrand(0,9), tempo); this.partialNum(24);}
		{countPedalDown == 56} {this.funcPerc(5, rrand(0,9), tempo); this.partialNum(9);}
		{countPedalDown == 57} {this.funcPerc(6, rrand(0,9), tempo, 0.85); this.partialNum(19);}
		//{countPedalDown == 58} {this.funcPerc(7, 0, tempo); this.partialNum(24);}
		{countPedalDown == 58} {this.funcPerc(7, 0, tempo); this.partialNum(11);}
		{countPedalDown == 59} {this.timerOnly(15.0*(1/tempo))}
		
		{countPedalDown == 66} {this.partialNum(17);}
		
		});
		});
	}
	
	pedalHigh {
		if(partialTrig == 0, {
			
			
		if(pedalUpMistake.not, {
				
		{pedalUpMistake = true;
		pedalTime.yield; 
		pedalUpMistake = false;
		}.fork;	
			
		countPedalUp = countPedalUp + 1;
		
		'pedalUp '.post; countPedalUp.postln;
		
		if(network.notNil, {
		network.sendMsg('/pedalUp', countPedalUp);
		});
		
		case
		{(4..7).includes(countPedalUp)} {motorSound.pedalOn(countPedalUp-3);}
		{(9..19).includes(countPedalUp)} {motorSound.pedalOn(countPedalUp-4);}
		{(21..40).includes(countPedalUp)} {motorSound.pedalOn(countPedalUp-5);};
		
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
	
		{countPedalUp == 43} {this.funcAlgoScore(1,18,tempo);}
		{countPedalUp == 44} {this.funcAlgoScore(2,19,tempo);}
		{countPedalUp == 45} {this.funcAlgoScore(3,20,tempo,2);}
		{countPedalUp == 46} {this.funcAlgoScore(5,21,tempo);}
		{countPedalUp == 47} {this.funcAlgoScore(6,22,tempo, 2);}
		{countPedalUp == 48} {this.funcAlgoScore(8,23,tempo);}
		{countPedalUp == 49} {this.funcAlgoScore(9,24,tempo);}
		{countPedalUp == 50} {this.funcAlgoScore(10,25,tempo, 2);}
		{countPedalUp == 51} {this.funcAlgoScore(12,26,tempo,3);}
		{countPedalUp == 52} {this.funcAlgoScore(15,27,tempo,2);}
		{countPedalUp == 53} {this.funcAlgoScore(17,28,tempo,1);}
		{countPedalUp == 54} {this.funcAlgoScore(18,29,tempo,2);}
		{countPedalUp == 55} {this.funcAlgoScore(20,30,tempo,2);}
		{countPedalUp == 56} {this.funcAlgoScore(22,31,tempo,1);}
		{countPedalUp == 57} {score.tagClose; this.funcAlgoScore(23,32,tempo,1);}
		{countPedalUp == 58} {score.tagClose; this.funcAlgoScore(24,33,tempo,1);}
		{countPedalUp == 59} {score.tagClose; this.funcAlgoScore(25,34,tempo,1,1); this.partialNum(18);}
		{countPedalUp == 60} {score.tagClose; this.funcAlgoScore(26,35,tempo,1);}
		{countPedalUp == 61} {score.tagClose; this.partialNotes(7.17*(1/tempo),36,"c 3".notemidi,"c 6".notemidi, "F");}
		{countPedalUp == 62} {score.tagClose; this.partialNotes(3.07*(1/tempo),37,"c 2".notemidi,"c 7".notemidi, "F");}
		{countPedalUp == 63} {score.tagClose; this.partialNotes(3.42*(1/tempo),38,"c 2".notemidi,"c 7".notemidi, "P");}
		{countPedalUp == 64} {score.tagClose; this.partialNotes(3.0*(1/tempo),39,"c 2".notemidi,"c 7".notemidi, "p", false); 
			//this.funcPerc(8, 0, tempo, 1, 0);
			}
		{countPedalUp == 65} {score.tagClose; this.funcPage(40)}
		{countPedalUp == 66} { this.textOnly("FREE IMPROV", 63.5*(1/tempo), 41); }
		
		{countPedalUp == 67} {this.funcPage(42);}
		{countPedalUp == 68} {this.movie(1,1/tempo,movieScale,43);}
		{countPedalUp == 69} {this.movie(2,1/tempo,movieScale,44);}
		{countPedalUp == 70} { this.textOnly("FREE IMPROV", 14.55*(1/tempo), 45); }
		{countPedalUp == 71} {this.funcPage(46);}
		{countPedalUp == 72} {this.movie(3,1/tempo,movieScale,47);}
		{countPedalUp == 73} {this.funcPage(48);}
		{countPedalUp == 74} {this.partialNotes(5.67*(1/tempo),49,"c 2".notemidi,"c 7".notemidi, "P");}
		{countPedalUp == 75} {this.movie(4,1/tempo,movieScale,50);}
		{countPedalUp == 76} {this.funcAlgoScore(27,34,tempo,1,2); this.partialNum(24); }
		{countPedalUp == 77} {this.funcAlgoScore(28,52,tempo,1)}
		{countPedalUp == 78} {this.funcAlgoScore(29,53,tempo,1)}
		{countPedalUp == 79} {this.funcPage(54);}
		{countPedalUp == 80} {this.funcPage(55);}
		{countPedalUp == 81} { this.textOnly("Don't play, solo electronics", 25*(1/tempo), 56, false, 60); {(26*(1/tempo)).yield; score.text("THE END", "Helvetica", 90, 1, 1); 10.yield; score.textClose;
			}.fork(clock)};
	
			this.arrangePedal;
		
		});
		});
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
		{((43..71) ++ [81]).includes(countPedalUp)}{countPedalDown = countPedalUp - 6}
		{[ 72, 73, 80 ].includes(countPedalUp)}{countPedalDown = countPedalUp-7}
		{(74..79).includes(countPedalUp)}{countPedalDown = countPedalUp-8}
		;
		
		//countPedalDown.postln;
		}
	
	startPedals {var pedalNum=1, number1, number2;

		MIDIdef.cc(\pedal1, {arg val; 
			if(val == 0, {this.pedalLow; this.score.click4Note(0.05);});
			}, 1, srcID: src); 
		MIDIdef.cc(\pedal2, {arg val; 
			if(val == 0, {this.pedalHigh; this.score.click4Note(0.05);});
			}, 2, srcID: src);
			
		MIDIdef.cc(\sensorUp, {arg val; 
				case
				{val < motorSound.lowVal} {number2 = motorSound.lowVal}
				{val > motorSound.highVal} {number2 = motorSound.highVal}
				{(val >= motorSound.lowVal).and(val <= motorSound.highVal)} {number2 = val};
				motorSound.sensorLag1.set(\num, number2);
				
				if(network.notNil, {
				network.sendMsg('/sensorUp', number2);
				});
				
			}, 4, srcID: src);
		MIDIdef.cc(\sensorSide, {arg val; 
				case
					{val < motorSound.leftVal} {number1 = motorSound.leftVal}
					{val > motorSound.rightVal} {number1 = motorSound.rightVal}
					{(val >= motorSound.leftVal).and(val <= motorSound.rightVal)} {number1 = val};
				motorSound.sensorLag2.set(\num, number1);
				
				if(network.notNil, {
				network.sendMsg('/sensorSide', number1);
				});
				
			}, 5, srcID: src);	
	}
	
	removePedals {
	MIDIdef.freeAll;
	}

	remove {	
		score.close;	
		SystemClock.clear;
	}
	
	movie {arg whichMovie=1, rate=1, scale=1, page=43, displayNext=true;
		var moviePath, movieTimes;
		partialTrig = 1;
		moviePath = basicPath ++ "/AlgoScore/videos/battle" ++ whichMovie.asString ++ ".mov";
		movieTimes = [42.706665039062, 33.934603174603, 14.552584157024, 8.3358450927186];
		
		if(network.notNil, {
		network.sendMsg('/page', ('movie ' ++ whichMovie));
		});
		
		{	
		score.clearWindow;
		0.1.yield;
		score.playMovie(moviePath, rate,	(1280-100)*(scale/1.2), (720-100)*(scale/1.2), 50*(scale/1.2), mute: muteMovie);

		this.movieWin(whichMovie, movieWinScale);
		
		(movieTimes[whichMovie-1]/rate).yield;
		0.5.yield;
		score.removeMovie;
		movwin.close;
		if(displayNext, {
		this.funcPage(page);
		});
		partialTrig = 0;
		}.fork(clock);
		
	}
	
	movieWin {arg whichMovie=1, scale=1;
		var movflow, top, left;
		top = score.w.bounds.height - (370*scale) - 100;
		left = score.movie.bounds.left + (50*scale); 
		movwin = Window("", Rect(left, top, 300*scale, 370*scale), border:false).front;
		movwin.view.background_((Color.black));
		movwin.alwaysOnTop = true;
		movflow = movwin.addFlowLayout(10@10, 10@10, 0);
		
		case
		{whichMovie == 1}{
		StaticText(movwin, 240@30*scale).string_("When you hear:").font_(Font("Helvetica", size: 25*scale)).stringColor_(Color.red);
		StaticText(movwin, 280@230*scale).string_("BULLETS - play two notes a semitone a part on in each hand, each hand should not be further than a minor 3rd apart, all notes between c4 and c5.\r\rONE BULLET -> play two hands at the same time.\r\rREPEATED BULLETS -> alternate between two hands.\r\r\r\r").font_(Font("Helvetica", size: 16*scale)).stringColor_(Color.green);
		StaticText(movwin, 280@60*scale).string_("BOMB -> play cluster with one hand bellow c2").font_(Font("Helvetica", size: 16*scale)).stringColor_(Color.yellow);
		}
		{[2,3].includes(whichMovie)}{
		StaticText(movwin, 240@30*scale).string_("When you hear:").font_(Font("Helvetica", size: 25*scale)).stringColor_(Color.red);
		StaticText(movwin, 280@60*scale).string_("YOUR OWN BULLETS - play cluster with one hand between c4 and c5").font_(Font("Helvetica", size: 16*scale)).stringColor_(Color.blue);
		StaticText(movwin, 280@100*scale).string_("BULLETS FROM OTHER COMBATANTS - play two notes a semitone apart alternating between hands, all above c6.\r\r").font_(Font("Helvetica", size: 16*scale)).stringColor_(Color.green);
		StaticText(movwin, 280@60*scale).string_("BOMB -> play cluster with one hand bellow c2").font_(Font("Helvetica", size: 16*scale)).stringColor_(Color.yellow);
	
		}
		{whichMovie == 4}{
		StaticText(movwin, 240@30*scale).string_("When you hear:").font_(Font("Helvetica", size: 25*scale)).stringColor_(Color.red);
		StaticText(movwin, 280@60*scale).string_("YOUR OWN BULLETS - play cluster with one hand between c4 and c5").font_(Font("Helvetica", size: 16*scale)).stringColor_(Color.blue);
		StaticText(movwin, 280@100*scale).string_("BULLETS FROM OTHER COMBATANTS - play two notes a semitone apart alternating between hands, all above c6.\r\r").font_(Font("Helvetica", size: 16*scale)).stringColor_(Color.green);
	
		}
	}
	
	show {
		score.show; motorSound.showWin;
	}
	
	hide {
		score.hide; motorSound.hideWin;
	}
	
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
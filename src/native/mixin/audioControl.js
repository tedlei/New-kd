let audioList = {
	goOutHintinfo:true,   //入库提示
	// pctcInfo: { src: "pctcInfo.mp3", isHint: true },                //请核实快件信息（扫描后）
	// placeSucceed: { src: "placeSucceed.mp3", isHint: true },        //放置成功（点击确认放置——即确认入库，无问题）
	// placeFailed: { src: "placeFailed.mp3", isHint: true },          //放置失败（点击确认放置——即确认入库，有问题（没有输入必填信息“运单号、公司、手机号、放置地址”，或者无网络））
	// repetition: { src: "repetition.mp3", isHint: true },            //重复（点击确认放置——即确认入库，重复扫描快件）
	// enterIntoSucceed: { src: "enterIntoSucceed.mp3", isHint: true },//入库成功（点击确认入库，无问题）
	// enterIntoFailed: { src: "enterIntoFailed.mp3", isHint: true },  //入库失败（点击确认入库，有问题（没有输入必填信息“运单号、公司、手机号”，或者无网络））
	
	retention: { src: "retention.mp3", isHint: true },              //滞留件：

	pvtpInfo: { src: "pvtpInfo.mp3", isHint: true },                //请核实取件者信息（在出库详情界面提示）
	goOutSucceed: { src: "goOutSucceed.mp3", isHint: true },        //出库成功（点击确认出库，无问题）
	goOutFailed: { src: "goOutFailed.mp3", isHint: true },          //出库失败（点击确认出库，无网络）
	alreadyGoOut: { src: "alreadyGoOut.mp3", isHint: true },        //已出库（重复扫描）

	handBackSucceed: { src: "handBackSucceed.mp3", isHint: true },	//退件成功（点击退件出库，无问题）
	handBackFailed: { src: "handBackFailed.mp3", isHint: true },	  //退件失败（点击退件出库，无网络）
	
	//      入库            出库           滞留件             退件
	noHint:{placeHint:true,goOutHint:true,retentionHint:true,handBackHint:true}  
}

const audio = {
	init(){
		this.setItem('audio', audioList);
		let num = 0
		if(!audioList.noHint.placeHint) num = 2;
		else if(!audioList.goOutHintinfo) num = 1;
		this.pushAndroid('setIsPlay', [num])
	},
	//提示音频播放
	audio(value,kind) {
		this.getItem('audio', (result) => {
			let obj = this.stringify(result.data);
			if(!obj['noHint'][kind]) return 
			let audio = weex.requireModule('audio')
			let url = 'root:file/audio/' + obj[value].src;
			if (!obj[value].isHint)url = 'root:file/audio/DropsSound.mp3'
			audio.setUrl({ url, autoPlay: false })
			audio.volume(100)
			let setT = setTimeout(() => {
				audio.play();
				clearTimeout(setT);
			}, 500);
		})
	}
}

export default audio
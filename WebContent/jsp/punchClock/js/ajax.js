ClearAll();
var objectId='reader';
var note;
var y=0;

var count=0;

var CardNo;		
var day;
var timeNow;

var forder="C:\\custduty\\";
var filename4Error="C:\\custduty\\error.csv";
var filename;
var output_filename;

var OnlineSignal="http://ap.cust.edu.tw/CIS/CardReaderSimple";
var OfflineSignal="http://ap.cust.edu.tw/CIS/CardReaderOffline";

function popup(){
        note=window.createPopup();
        var content=note.document.body;
        content.innerHTML=menu.innerHTML;
        y=0;
        setTimeout("move()",1);
}

function move(){
	note.show(window.screen.availWidth-600,window.screen.availHeight-y,menuTb.offsetWidth, y, '');
	y=y+5;
	if(y<220)
	setTimeout("move()",1);
}

function close(){
	note.show(window.screen.availWidth-310,window.screen.availHeight-y,menuTb.offsetWidth, y, '');
	y=y-5;
	if(y>-5)
	setTimeout("close()",1);
}

try{
	var XMLHttpReqDyna=new XMLHttpRequest();
}catch(e){
	var XMLHttpReqDyna=false;
}

var XMLHttpRequest=null;
//建立HttpRequestDyna
function createXMLHttpRequestDyna(){	
	if(window.ActiveXObject){
		XMLHttpReqDyna = new ActiveXObject("Microsoft.XMLHTTP");
	}else if(window.ActiveXObject){
		XMLHttpReqDyna = new ActiveXObject("Msxml2.XMLHTTP");
	}
	//瀏覽器無法建立ajax物件
    if(!XMLHttpReqDyna){
    	alert("AJAX 錯誤2");
    }    
}

//開始主要流程
function proceDyna(){	
	document.getElementById("cardValue").style.display="inline";
	document.getElementById("photo").innerHTML="";
	popup();	
	if(XMLHttpReqDyna.readyState==4){		
		//回傳代碼正常狀態200
 		if(XMLHttpReqDyna.status==200){ 			
			var cname=XMLHttpReqDyna.responseXML.getElementsByTagName("cname")[0].firstChild.data;
			var time=XMLHttpReqDyna.responseXML.getElementsByTagName("time")[0].firstChild.data;
			var info=XMLHttpReqDyna.responseXML.getElementsByTagName("info")[0].firstChild.data;
			var idno=XMLHttpReqDyna.responseXML.getElementsByTagName("idno")[0].firstChild.data;
			
			//若回傳資訊非"none"			
			if(cname!='none'){			
				//刷卡資訊							
				document.getElementById("cname").innerHTML=cname;
				document.getElementById("date").innerHTML=time;
				document.getElementById("info").innerHTML=info;				
				
				//過去資訊
				try{
					document.getElementById("info1").innerHTML=XMLHttpReqDyna.responseXML.getElementsByTagName("time")[1].firstChild.data;
					document.getElementById("info2").innerHTML=XMLHttpReqDyna.responseXML.getElementsByTagName("time")[2].firstChild.data;
					document.getElementById("info3").innerHTML=XMLHttpReqDyna.responseXML.getElementsByTagName("time")[3].firstChild.data;
					document.getElementById("info4").innerHTML=XMLHttpReqDyna.responseXML.getElementsByTagName("time")[4].firstChild.data;			
				}catch(e){
					document.getElementById("info1").innerHTML="找不到前1個工作日出勤資訊";
					document.getElementById("info2").innerHTML="找不到前2個工作日出勤資訊";
					document.getElementById("info3").innerHTML="找不到前3個工作日出勤資訊";
					document.getElementById("info4").innerHTML="找不到今天的工作日出勤資訊";
				}
				
				//照片資訊				
				document.getElementById("photo").innerHTML="<img src='http://cap.cust.edu.tw/CIS/Personnel/getFTPhoto4Empl?idno="+idno+"' width='100' border='1' />";				
			}else{			
				document.getElementById("cardValue").style.display="inline";	
				document.getElementById("cname").innerHTML="無法識別的卡片資料";
				document.getElementById("info").innerHTML="";
				document.getElementById("info1").innerHTML="無";
				document.getElementById("info2").innerHTML="法";
				document.getElementById("info3").innerHTML="取";				
			}			
			popup();			
 		}
 	}
 	count=count+1;
 	//斷線
 	if(count==2&&XMLHttpReqDyna.readyState==4){		
 		writeToErrFile(CardNo, day, timeNow);
 	} 	
}

function CloseCardValue(){//TODO 檢視 
	//清之前將照片塞入
	document.getElementById("cardValue").style.display="none";	
	document.getElementById("cardValue").style.display="none";
	document.getElementById("cname").innerHTML="無法取得資訊,";
	document.getElementById("date").innerHTML="網路異常";
	document.getElementById("info").innerHTML="本機已建立記錄";	
	ClearAll();
	if(note!=null)
	close();		
}

//復原輸入欄位
function ClearAll(){	
	document.getElementById("CardNo").value="";
	document.form1.CardNo.focus();
}

function randomColor(){

	
	var color = new Array();	
	color[0]="71828a";
	color[1]="8a7171";
	color[2]="758a71";
	color[3]="76718a";
	color[4]="8a8a71";
	color[5]="718a82";
	color[6]="b96f47";
	color[7]="832f2f";
	color[8]="71828a";
	color[9]="832f2f";
	
	var r=Math.random()*10;
	var rn=Math.floor(r);
	document.getElementById("pop").bgColor=color[rn];
}

function reading(code){	
	count=0;
	CardNo=document.getElementById("CardNo").value;		
	day=document.getElementById("day").value;
	timeNow=document.getElementById("time").value;
	if(code=='13'){ 
		var n = Math.floor(Math.random()*1000);  
		writeToFile(CardNo, day, timeNow);
		sendDyna(OnlineSignal+"?CardNo="+CardNo+"&day="+day+"&time="+timeNow+"&"+n);						
	}	
}

//傳送器
function sendDyna(url){
	randomColor();
	ClearAll();
	createXMLHttpRequestDyna();
	try{
		XMLHttpReqDyna.open("GET",url,true);
	}catch(e){
		alert("ajax 錯誤1");
	}
	XMLHttpReqDyna.onreadystatechange=proceDyna;	
	XMLHttpReqDyna.send(null);		
}
 
//寫1筆資料
function writeToFile(CardNo, date, time) {
	
	filename=date+".csv";
	output_filename=forder+filename;	
	try {
		var fso;
		var way=8;
	   	fso=new ActiveXObject("Scripting.FileSystemObject");
		var output_stream;
		
		if (!fso.FolderExists(forder)){
			fso.CreateFolder (forder)
		}
		
		if (fso.FileExists(output_filename)){
		   	output_stream = fso.GetFile(output_filename).OpenAsTextStream(way);
		    output_stream.WriteLine(CardNo+","+date+","+time); //寫入
		    output_stream.close(); 
		}else{
			//建立再寫入
		 	output_stream = fso.CreateTextFile(output_filename, true);//建立
		 	output_stream.WriteLine(CardNo+","+date+","+time); //寫入
		 	output_stream.Close();
		}		
	}catch(err){
	  alert(err.description);
	}
}

//寫1筆斷線資料
function writeToErrFile(CardNo, date, time) {

	try {
		var fso;
		var way=8;
	   	fso=new ActiveXObject("Scripting.FileSystemObject");
		var output_stream;
		
		if (!fso.FolderExists(forder)){
			fso.CreateFolder (forder)
		}
		
		if (fso.FileExists(filename4Error)){
		   	output_stream = fso.GetFile(filename4Error).OpenAsTextStream(way);
		    output_stream.WriteLine(CardNo+","+date+","+time); //寫入
		    output_stream.close(); 
		}else{
			//建立再寫入
		 	output_stream = fso.CreateTextFile(filename4Error, true);//建立
		 	output_stream.WriteLine(CardNo+","+date+","+time); //寫入
		 	output_stream.Close();
		}		
	}catch(err){
	  alert(err.description);
	}
}

//試著讀取第1行斷線檔文字
function sendError() {	
	try {
		var fso;
		var way=8;
	   	fso=new ActiveXObject("Scripting.FileSystemObject");
		var output_stream;		
		if (fso.FileExists(filename4Error)){
		   	output_stream = fso.OpenTextFile(filename4Error, 1);		    
		    //讀取第1行
		    var line=output_stream.ReadLine();
		    output_stream.close();
		    //想辦法send的出去
		    document.getElementById("errormessage").innerHTML="正在傳送: "+line;
		    sendOfflineDyna(OfflineSignal+"?line="+line);		    
		}	
	}catch(err){
	  //alert("這裏嗎？"+err.description);
	  document.getElementById("errormessage").innerHTML="沒有資料可供傳送";
	}	
}

//刪除文字第1行
function deleteLine(){
	
	var arrLines= new Array();
	var count=0;
	
	try {
		var fso;
		var way=8;
	   	fso=new ActiveXObject("Scripting.FileSystemObject");
		var output_stream;		
		if (fso.FileExists(filename4Error)){
		   	
		   	//建立暫存
		   	output_stream = fso.OpenTextFile(filename4Error, 1);		    
		    while (!output_stream.AtEndOfStream){
		    	arrLines[count] = output_stream.ReadLine();
		    	count=count+1;
    		}    		
		    output_stream.close();		    
		    
		    //檔案操作
		    fso = new ActiveXObject("Scripting.FileSystemObject");
   			fso.DeleteFile(filename4Error);			
			output_stream = fso.CreateTextFile(filename4Error, true);//建立
		    output_stream.close();
		    
		    output_stream = fso.OpenTextFile(filename4Error, 2);		    
			for ( var i = 1; i < arrLines.length; i++){						
				output_stream.WriteLine(arrLines[i]); //寫入			
			}		    
		    output_stream.close();		    	    
		}	
	}catch(err){
	  alert(err.description);
	}

}

//隨便寫1行文字for錯誤檔
function writeLine(line){
	
	try {
		var fso;
		var way=8;
	   	fso=new ActiveXObject("Scripting.FileSystemObject");
		var output_stream;
		
		if (!fso.FolderExists(forder)){
			fso.CreateFolder (forder)
		}
		
		if (fso.FileExists(filename4Error)){
		   	output_stream = fso.GetFile(filename4Error).OpenAsTextStream(way);
		    output_stream.WriteLine(CardNo+","+date+","+time); //寫入
		    output_stream.close(); 
		}else{
			//建立再寫入
		 	output_stream = fso.CreateTextFile(output_filename, true);//建立
		 	output_stream.WriteLine(CardNo+","+date+","+time); //寫入
		 	output_stream.Close();
		}		
	}catch(err){
	  alert(err.description);
	}

}
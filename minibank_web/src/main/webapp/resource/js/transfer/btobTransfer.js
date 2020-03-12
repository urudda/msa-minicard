$(function() {
	var contextPath = $('#contextPath').val();
	var transferApiUrl = $('#transferApiUrl').val();
	var accountApiUrl = $('#accountApiUrl').val();
	$("#wthdAcntNo").focus();
	$('#stsCd').hide();
	$('#transferMenu').addClass('selected-menu');
	$("#btnSearch").on("click", function(){
		var wthdAcntNo = $('#wthdAcntNo').val();
		if(wthdAcntNo == null || wthdAcntNo == ""){
			alert("계좌번호를 입력해주세요.");
			alert(wthdAcntNo);
			$('#wthdAcntNo').focus();
		}
		else{
			retrieveAccountInfo(wthdAcntNo);
		}
	});
	$('#wthdAcntNo').on('change',function(){
		$('#stsCd').hide();
		$('#btnRequest').hide();
	});
	$('#btnRequest').on("click",function(){
		if(isValidInputValue() == true){
			var wthdAcntNo = $('#wthdAcntNo').val();
			var dpstAcntNo = $('#dpstAcntNo').val();
			var trnfAmt = $('#trnfAmt').val();
			var rcvMm = $('#rcvMm').val();
			var sndMm = $('#sndMm').val();
			var cstmId = $('td[name=cstmId]').text();
			var stsCd = $('#stsCd').val();
			var transferData = {"wthdAcntNo" : wthdAcntNo,
							    "dpstAcntNo" : dpstAcntNo,
							    "trnfAmt" : trnfAmt,
							    "rcvMm" : rcvMm,
							    "sndMm" : sndMm,
							    "cstmId" : cstmId,
							    "stsCd" : stsCd};
			transfer(transferData);
		}
	});
	function retrieveAccountInfo(acntNo){
		$.ajax({
			type : 'GET',
			url : accountApiUrl+'/rest/v0.8/'+acntNo,
			contentType: 'application/json',
			datatype : 'json',
			xhrFields : {
				withCredentials : true
			},
			crossDmain: true,
			beforeSend : function(){
		        $('.wrap-loading').removeClass('display-none');
			},
			complete:function(){
		        $('.wrap-loading').addClass('display-none');
			},
			success : function(data, textStatus, jqXHR) {
				$('td[name=acntNm]').text(data.acntNm);
				$('#acntBlnc').text(data.acntBlnc);
				$('td[name=cstmId]').text(data.cstmId);
				$('td[name=cstmNm]').text(data.cstmNm);
				$('#rcvMm').val(data.cstmNm);
				retrieveTransferLimit(data.cstmId);
			},
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/transfer/view/btobTransfer");
		    }
		});
	}

	function retrieveTransferLimit(cstmId){
		$.ajax({
			type : 'GET',
			url : transferApiUrl+'/transfer-limit/enable/rest/v0.8/' + cstmId,
			contentType: 'application/json',
			datatype : 'json',
			xhrFields : {
				withCredentials : true
			},
			crossDmain: true,
			beforeSend : function(xhr){
		        $('.wrap-loading').removeClass('display-none');
			},
			complete:function(){
		        $('.wrap-loading').addClass('display-none');
			},
			success : function(data) {
				$('#oneDyTrnfLmt').text(data.oneDyTrnfLmt);
				$('#oneTmTrnfLmt').text(data.oneTmTrnfLmt);
				$('#btnRequest').show();
				$('#stsCd').show();
			},
		    error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/transfer/view/btobTransfer");
		    }
		});
	}
	function isValidInputValue(){
		var dpstAcntNo = $('#dpstAcntNo').val();
		var trnfAmt = $('#trnfAmt').val();
		var acntBlnc = $('#acntBlnc').text();
		var oneDyTrnfLmt = $('#oneDyTrnfLmt').text();
		var oneTmTrnfLmt = $('#oneTmTrnfLmt').text();
		
		if(dpstAcntNo == null || dpstAcntNo == ""){
			alert("입금계좌번호를 입력해주세요.");
			$('#dpstAcntNo').focus();
			return false;
		}
		if(trnfAmt == null || trnfAmt == ""){
			alert("이체금액을 입력해주세요.");
			$('#trnfAmt').focus();
			return false;
		}
		if(isNaN(trnfAmt) == true){
			alert("이체금액은 숫자만 입력해주세요.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt <= 0){
			alert("이체금액은 0 보다 큰 금액을 입력해주세요.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt > acntBlnc * 1){
			alert("잔액이 부족합니다.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt > oneTmTrnfLmt * 1){
			alert("1회 이체 한도를 초과하였습니다.");
			$('#trnfAmt').focus();
			return false;
		}
		if(trnfAmt > oneDyTrnfLmt * 1){
			alert("1일 이체 한도를 초과하였습니다.");
			$('#trnfAmt').focus();
			return false;
		}
		
		return true;
	}
	function transfer(transferData){
		$.ajax({
			type : 'POST',
			url : transferApiUrl+'/b2b/rest/v0.8',
			contentType: 'application/json',
			datatype : 'json',
			data : JSON.stringify(transferData),
			xhrFields : {
				withCredentials : true
			},
			crossDmain: true,
			beforeSend : function(xhr){
		        $('.wrap-loading').removeClass('display-none');
			},
			complete:function(){
		        $('.wrap-loading').addClass('display-none');
			},
			success: function(data, textStatus, jqXHR){
		    	alert("이체 요청을 완료했습니다.");
				$(location).attr("href", contextPath + "/transfer/view/retrieveTransferHistory?cstmId="+ $('td[name=cstmId]').text());
		   },
		   error: function (jqXHR, textStatus, errorThrown) {
		    	if(jqXHR.status == '417' || jqXHR.status == '500'){
			    	var responseText = jqXHR.responseText;
			    	var body = JSON.parse(responseText);
			    	alert(body.message);
		    	}
		    	else
		    		alert("[연결 오류]\n서버와 연결에 실패했습니다.");
				$(location).attr("href", contextPath + "/transfer/view/btobTransfer");
		    }
		});
	}
});
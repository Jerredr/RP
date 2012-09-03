<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html>
<html>
<head>
<title>ResearchPad</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/jquery.mobile-1.0.min.css" />" />

<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/skin.css" />" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/style.css"/>" />
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.mobile-1.0.min.js" />"></script>
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/jRating.jquery.css" />" />
<script type="text/javascript"
	src="<c:url value="/resources/js/jRating.jquery.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/researchpad.js" />"></script>


<!--
      jCarousel library
    -->
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.jcarousel.researchpad.js" />"></script>
<!--
      jCarousel skin stylesheet
    -->
 
<script type="text/javascript">
	var leftActiveView = 0;
	var nrViews = 2;
	
	jQuery(document).ready(function() {		
		
		jQuery('#mycarousel').jcarousel({
			initCallback : carousel_callback,
			itemFirstInCallback:  mycarousel_itemFirstInCallback,
			buttonNextHTML: null,
			buttonPrevHTML: null
		});
	});
	
	function mycarousel_itemFirstInCallback(carousel, item, idx, state) {
		if(idx == 1) {
	    	//$('.jcarousel-item-3').remove();
	    	
		    var nrItems = carousel.size();
		    var i = 0;
		    for(i = nrItems; i > 2; i--) {
		    	carousel.remove(i);
		    	nrViews--;
		    }
		}
	};

	function carousel_callback(carousel, state) {
		// Add tab
		$('#add-to')
				.click(
						function(evt) {
							carousel
									.add(
											(jQuery('#mycarousel').jcarousel(
													'size') + 1),
											"<li style='width: 50%;'><div style='background-color: green; height: auto;'>ADDED PAGE</div></li>");
							carousel.size(jQuery('#mycarousel').jcarousel(
									'size') + 1);
							jQuery('#mycarousel').jcarousel('scroll',
									jQuery('#mycarousel').jcarousel('size'));
						});

		function internalLink(ref) {
			var newTabId = jQuery('#mycarousel').jcarousel('size') + 1;
			carousel
					.add(
							newTabId,
							"<li style='width: 50%;'><div id='tab" + newTabId + "' style='height: auto; margin-left: 5px; margin-right: 5px;' class='view'></div></li>");
			// load content
			$("#tab" + newTabId).load("/researchPad" + ref);
			carousel.size(newTabId);
			$.mobile.pageLoading();     	
			jQuery('#mycarousel').jcarousel('scroll', newTabId);
			$.mobile.pageLoading(true);     
			leftActiveView++;
			nrViews++;
		}

		//$('.ref').click(function() {
		/*$('body').on('click', '.ref', function() {
			var parts = $(this).attr('id').split('/ref');
			 $('body').animate({scrollTop: '0px'}, 500, function(){ $('body').clearQueue(); });
			internalLink(parts[parts.length - 1]);
		});*/
	}
	
	function clickFunction(id) {
		$('body').animate({scrollTop: '0px'}, 500, function(){ $('body').clearQueue(); });
		internalLink("/document/" + id);
	}
	
	function authorFunction(id) {
		$('body').animate({scrollTop: '0px'}, 500, function(){ $('body').clearQueue(); });
		internalLink("/author/" + id);
	}
	
	function internalLink(ref) {
		var newTabId = jQuery('#mycarousel').jcarousel('size') + 1;
		var carousel = jQuery('#mycarousel').data('jcarousel');
		carousel
				.add(
						newTabId,
						"<li style='width: 50%;'><div id='tab" + newTabId + "' style='height: auto; margin-left: 5px; margin-right: 5px;' class='view'></div></li>");
		// load content
		$("#tab" + newTabId).load("/researchPad" + ref);
		carousel.size(newTabId);
		jQuery('#mycarousel').jcarousel('scroll', newTabId);
		leftActiveView++;
		nrViews++;
	}
		


	$(document).keydown(function(e){
	    // left
		if (e.keyCode == 37) { 
			jQuery('#mycarousel').jcarousel('prev');
			//prev();
	    // right
		} else if(e.keyCode == 39) {
			jQuery('#mycarousel').jcarousel('next');
			//next();
	    }
	    //return false;
	});
	
	/*$('#slider').change(function(){
	    var slider_value = $(this).val();
	    alert(slide_value);
	    //${pd.id}
	});*/
	var t;
	function update_slider()
	{
		clearTimeout(t);
		t=setTimeout("update_slider2()",1000);
	}
	
	function update_slider2() {
		var val = $('#slider').val();
		$.post("/researchPad/document/submitrating", {
			pdId : <c:out value="${pd.id}"/>,
			rating : val
		}, function(data) {
			
		});
	}

	$(document).ready(function() {
		/**
		 * Swipe left/right
		 **/
		$('.swiping').live('swipeleft swiperight', function(event) {
			//console.log(event.type);
			if (event.type == "swipeleft") {			
				//jQuery('#mycarousel').jcarousel('next');
				next();
			}
			if (event.type == "swiperight") {
				//jQuery('#mycarousel').jcarousel('prev');
				prev();
			}
			event.preventDefault();
		});
		
		/**
		 * Star rating
		 **/
		 $(".basic").jRating({
			 step: true,
			 phpPath : '/researchPad/document/submitrating',
			 bigStarsPath : '/researchPad/resources/icons/stars.png',
			 showRateInfo: false
		 });
	});
	
	/**
	 * Carousel fix functions
	 **/
	function next() {
		jQuery('#mycarousel').jcarousel('next');
		/*
		if(leftActiveView != (nrViews-2)) {
			leftActiveView++;
			jQuery('#mycarousel').jcarousel('next');
		} else {
			return false;
		}*/
	}
	
	function prev() {
		jQuery('#mycarousel').jcarousel('prev');
		/*
		if(leftActiveView != 0) {
			leftActiveView--;
			jQuery('#mycarousel').jcarousel('prev');
		} else {
			return false;
		}*/
	}

	/**
	 * Submit notes
	 **/
	function saveNotes() {
		$.post("/researchPad/document/postnotes", {
			notes : $("#notes").val(),
			pdId : <c:out value="${pd.id}"/>
		}, function(data) {
			$("#notesMessage").html("");	// no message now
		});
	}

	/**
	 * Rating items
	 **/
	function removeListItem(id) {
		$('#ratingBullet' + id).remove();
		$.post("/researchPad/document/removerating", {
			itemId : id
		}, function(data) {
			
		});
	}

	function addListItem() {
		if($.trim($('#input_listName').val()) == "") {
			return;
		}
		// TODO check whether ratingItem != ""
		var ratingItemId = 0;
		
		var params = {
				pdId : <c:out value="${pd.id}"/>,
				ratingItem : $('#input_listName').val()
		}
		
		$.ajax({
			type : "POST",
			url : "/researchPad/document/addrating",
			data: "&" + $.param(params),
			success : function(msg) {
				ratingItemId = msg;
				$('#ratingBullets').append(					
						'<div data-theme="a" class="ui-bar ui-grid-a" id="ratingBullet' + ratingItemId +'">' +
						'<div class="ui-block-a"><input id="ratingItem' + ratingItemId + '" value="'+ $('#input_listName').val() + '" onchange="saveRatingItem(' + ratingItemId + ');" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset" /></div>' +
						'<div class="ui-block-b"><div style="margin:6px 0 0 10px;"><div data-theme="c" class="ui-btn ui-btn-up-c ui-btn-corner-all ui-shadow" aria-disabled="false">' + 
						'<span class="ui-btn-inner ui-btn-corner-all" aria-hidden="true"><span class="ui-btn-text">X</span></span>' + 
						'<button id="' + ratingItemId +'" onclick="removeListItem(' + ratingItemId +')" class="ui-btn-hidden" aria-disabled="false">X</button></div></div></div>' +
					'</div>'
					);	
				
				$('#input_listName').val("");
			}
		});
	}
	
	function saveRatingItem(id) {
		$.post("/researchPad/document/saveratingitem", {
			ratingItemId : id,
			rating : $('#ratingItem' + id).val()
		}, function(data) {
			
		});
	}
	
	
</script>
<script src="<c:url value="/resources/js/jquery.nicescroll.js" />" type="text/javascript"></script>
<script type="text/javascript">
	$(function(){
		$(window).resize(function(){
			$(".view").niceScroll({touchbehavior:true,bouncescroll:false,boxzoom:true,gesturezoom:true,dblclickzoom:true,cursoropacitymax:0.6,cursorwidth:8});
			
			//$("#boxscroll").css("height", screen.availHeight - $("#header2").outerHeight());
			/*$("#boxscroll").css("width", '100%');
			$("#boxscroll").niceScroll({touchbehavior:false,bouncescroll:true,boxzoom:true,cursorcolor:"#0000FF",cursoropacitymax:0.6,cursorwidth:8})*/
		});
	});
	$(document).ready(function() {
		//alert($("#header2").outerHeight());
		//alert(screen.availHeight);
		$(".view").css("height", screen.availHeight - $("#header2").outerHeight() + 10);
		$(".view").niceScroll({touchbehavior:true,bouncescroll:false,boxzoom:true,gesturezoom:true,dblclickzoom:true,cursoropacitymax:0.6,cursorwidth:8});
		
		//$("#boxscroll2").css("height", screen.availHeight - $("#header2").outerHeight() + 10);
		//$("#boxscroll2").niceScroll({touchbehavior:false,bouncescroll:true,boxzoom:true,cursorcolor:"#0000FF",cursoropacitymax:0.6,cursorwidth:8});
	});
</script>
</head>
<body>
	<!-- Start of first page -->
	<div data-role="page" class="swiping">
		<div data-role="header" data-fullscreen="true" id="header2">  
		<a href="#" data-role="button" data-theme="b" class="ui-btn-left" data-icon="arrow-l" id="BackButtonHandler" onclick="prev()">Prev</a>
		<h1>ResearchPad</h1>
		<div data-type="horizontal" data-role="controlgroup" class="ui-btn-right">  
			<a href="${pageContext.request.contextPath}/library/home" data-role="button" data-icon="grid" rel="external">Library</a>
			<a href="#" data-role="button" data-theme="b" data-icon="arrow-r" data-iconpos="right" id="BackButtonHandler" onclick="next()">Next</a>
	  	</div>
		</div>
		<div data-role="content" style="padding: 0px">
			<div id="mainContent" style="float: left; height: 100%; top: 0; bottom: 0; width: 100%; margin-left: 5px; margin-right: 5px;">
				<ul id="mycarousel" class="jcarousel-skin-tango">
					<li style="width: 50%;">
						<div id="boxscroll" class="view">
							<c:choose>
								<c:when test="${pd.document.type == 0}">
									<h2>Document not found</h2>
								</c:when>
								<c:when test="${pd.document.type == 1}">
									${pd.document.content}
								</c:when>
								<c:when test="${pd.document.type == 2}">
									<br>
									<p>
									This document is only available as PDF: <a href="${pd.document.url}" target="_blank">Open PDF</a>
									</p>
								</c:when>
							</c:choose>
						</div>
					</li>
					<li style="width: 50%; margin-left: 5px; margin-right: 5px;">
						<div class="">
							<h3>${pd.document.name}</h3>
							<p>
								<strong>Authors:</strong>
								<c:forEach items="${pd.document.authors}" var="author">
									<span class="ref" id="/ref/author/${author.id}"
										data-role="button" data-inline="true" onclick="authorFunction(${author.id})">${author.firstName}
										${author.lastName}</span>
								</c:forEach>
							</p>

							<div data-role="collapsible-set" data-mini="true">
								<div data-role="collapsible" data-theme="b" class="acc">
									<h3>References</h3>
									<ul data-role="listview" style="margin-top: -10px; margin-bottom: -3px;">
										<c:forEach items="${pd.document.references}" var="reference">
										<li style="margin-left: 15px; margin-right: 15px;">
											<a class="ref" id="/ref/document/${reference.referenced.id}" data-inline="true" onclick="clickFunction(${reference.referenced.id})">
											    <c:choose>
												    <c:when test="${reference.page == '' }">
												    	<p><strong>${reference.name} ${reference.referenced.name} (${reference.referenced.publicationYear})</strong>
												    	<br>
														<c:forEach items="${reference.referenced.authors}" var="author">
															${author.firstName} ${author.lastName}; 
															</c:forEach>
														</p>
												    </c:when>
												    <c:otherwise>
												    	<p><strong>${reference.name}: ${reference.page}</strong></p>
													    </c:otherwise>
											    </c:choose>
											</a>
										</li>
										</c:forEach>
									</ul>
								</div>
								<div data-role="collapsible" data-theme="b" class="acc">
									<h3>Citing papers</h3>
									
									<ul data-role="listview" style="margin-top: -10px; margin-bottom: -3px;">
										<c:forEach items="${pd.document.referenced}" var="referenced">
										<li style="margin-left: 15px; margin-right: 15px;">
											<a class="ref" id="/ref/document/${referenced.document.id}" data-inline="true" onclick="clickFunction(${referenced.document.id})">
											
												<h3>${referenced.document.name}</h3>
												<p><strong>
													<c:forEach items="${referenced.document.authors}" var="author">
													${author.firstName} ${author.lastName}; 
													</c:forEach>
												</strong></p>
												<p class="ui-li-aside"><strong>${referenced.document.publicationYear}</strong></p>
											
											</a>
										</li>
										</c:forEach>
									</ul>
									
									<!--<c:forEach items="${pd.document.referenced}" var="referenced">
										<a class="ref" id="/ref/document/${referenced.document.id}"
											data-role="button" data-inline="true">${referenced.document.name}</a>
										<br>
									</c:forEach>-->
									
									<!--<c:forEach items="${pd.document.referenced}" var="referenced">
										<li style="margin-left: 15px; margin-right: 15px;">
											<span class="ref" id="/ref/document/${referenced.document.id}" data-inline="true">
											
												<h3>${referenced.document.name}</h3>
												<p><strong>
													<c:forEach items="${referenced.document.authors}" var="author">
													${author.firstName} ${author.lastName}; 
													</c:forEach>
												</strong></p>
												<p class="ui-li-aside"><strong>${referenced.document.publicationYear}</strong></p>
											
											</span>
										</li>
									</c:forEach>-->
								</div>
								<!-- <div data-role="collapsible" data-theme="b">
									<h3>Suggestions</h3>
									<p>No suggestions just yet.</p>
								</div> -->
								<div data-role="collapsible" data-theme="b" class="acc">
									<h3>Rating</h3>
									<strong>Your rating: </strong> <br>
									<input type="range" name="slider" id="slider" value="${pd.rating}" min="0" max="10" step="1" data-theme="b" onChange='update_slider()' />
									<br><br>
									<span id="ratingBullets">
										<c:forEach items="${pd.ratingItems}" var="item">
											<div data-theme="a" class="ui-bar ui-grid-a" id="ratingBullet${item.id}">
												<div class="ui-block-a"><input id="ratingItem${item.id}" value="${item.content}" onchange="saveRatingItem(${item.id})" /></div>	 
												<div class="ui-block-b"><div style="margin:6px 0 0 10px;"><button id="${item.id}" onclick="removeListItem(${item.id})">X</button></div></div>
											</div>  
										</c:forEach>
									</span>
									
									<input id="input_listName" />
									<button id="btn_createList" onclick="addListItem()">Add bulletpoint</button>
								</div>
								<div data-role="collapsible" data-theme="b" class="acc">
									<h3>Notes</h3>
									<p>
									<div data-role="fieldcontain">
										<span id="notesMessage"></span>
										<div style="text-align: center;">
											<textarea rows="8" name="notes" id="notes" cols="" style="width: 95%;">${pd.notes}</textarea>
											<input type="submit" value="Save Notes" onclick="saveNotes()" />
										</div>
									</div>
									</p>
								</div>
							</div>
							<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
						</div>
					</li>
				</ul>
			</div>
		</div>
		<!-- /content -->
	</div>
	<!-- /page -->
</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<title>ResearchPad</title>
<meta charset="utf-8">
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


<!--
      jCarousel library
    -->
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.jcarousel.researchpad.js" />"></script>
<!--
      jCarousel skin stylesheet
    -->

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#mycarousel').jcarousel({
			initCallback : carousel_callback,
			buttonNextHTML: null,
			buttonPrevHTML: null
		});
	});

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
							"<li style='width: 50%;'><div id='tab" + newTabId + "' style='height: auto;'></div></li>");
			// load content
			$("#tab" + newTabId).load("/researchPad" + ref);
			carousel.size(newTabId);
			jQuery('#mycarousel').jcarousel('scroll', newTabId);
		}

		$('.ref').click(function() {
			var parts = $(this).attr('id').split('/ref');
			 $('body').animate({scrollTop: '0px'}, 500, function(){ $('body').clearQueue(); });
			internalLink(parts[parts.length - 1]);
		});
	}

	$(document).ready(function() {

		/**
		 * Swipe left/right
		 **/
		$('.foo').live('swipeleft swiperight', function(event) {
			console.log(event.type);
			if (event.type == "swipeleft") {
				jQuery('#mycarousel').jcarousel('next');
			}
			if (event.type == "swiperight") {
				jQuery('#mycarousel').jcarousel('prev');
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
	 * Submit notes
	 **/
	function saveNotes() {
		$.post("/researchPad/document/postnotes", {
			notes : $("#notes").val(),
			pdId : <c:out value="${pd.id}"/>
		}, function(data) {
			$("#notesMessage").html("Notes saved");
		});
	}

	/**
	 * Rating items
	 **/
	function removeListItem(id) {
		$('.ul_current li:#li' + id).remove();
		$.post("/researchPad/document/removerating", {
			itemId : id
		}, function(data) {
			
		});
	}

	function addListItem() {
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
				$('.ul_current').append(
						'<li id="li' + ratingItemId + '"><input id="ratingItem'
								+ ratingItemId + '" value="'
								+ $('#input_listName').val() + '" onchange="saveRatingItem(' + ratingItemId + ')" width="70%" class="ui-input-text ui-body-c ui-corner-all ui-shadow-inset" /><button id="'
								+ ratingItemId + '" onclick="removeListItem('
								+ ratingItemId + ')" class="ui-btn-hidden">X</button></li>');
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


<!-- <script type="text/javascript"
	src="<c:url value="/resources/js/iscroll.js"/>"></script>
<script type="text/javascript">
	var myScroll;
	function loaded() {
		myScroll = new iScroll('wrapper');
	}

	document.addEventListener('touchmove', function(e) {
		e.preventDefault();
	}, false);

	/* * * * * * * *
	 *
	 * Use this for high compatibility (iDevice + Android)
	 *
	 */
	document.addEventListener('DOMContentLoaded', function() {
		setTimeout(loaded, 200);
	}, false);
	/*
	 * * * * * * * */

	/* * * * * * * *
	 *
	 * Use this for iDevice only
	 *
	 */
	//document.addEventListener('DOMContentLoaded', loaded, false);
	/*
	 * * * * * * * */

	/* * * * * * * *
	 *
	 * Use this if nothing else works
	 *
	 */
	//window.addEventListener('load', setTimeout(function () { loaded(); }, 200), false);
	/*
	 * * * * * * * */
</script> -->
</head>
<body>
	<!-- Start of first page -->
	<div data-role="page" class="foo">
		<div data-role="header" data-position="fixed">  
			<a href="../../library/home" data-icon="grid" class="ui-btn-left" rel="external">Library</a>
			<h1>ResearchPad</h1>
		</div>
		<div data-role="content" style="padding: 0px">
			<div id="mainContent" style="float: left; height: 100%; top: 0; bottom: 0; width: 100%;">
				<ul id="mycarousel" class="jcarousel-skin-tango">
					<li style="width: 50%;">
						<div>
							<div id="wrapper">
								<div id="scroller">
									${pd.document.content}
								</div>
							</div>
						</div>
					</li>
					<li style="width: 50%;">
						<div>
							<h3>${pd.document.name}</h3>
							<p>
								<strong>Authors:</strong>
								<c:forEach items="${pd.document.authors}" var="author">
									<span class="ref" id="/ref/author/${author.id}"
										data-role="button" data-inline="true">${author.firstName}
										${author.lastName}</span>
								</c:forEach>
							</p>
							<p>
								<strong>Tags:</strong> TODO
							</p>

							<div data-role="collapsible-set" data-mini="true">
								<div data-role="collapsible" data-collapsed="false">
									<h3>References</h3>
									<ul data-role="listview" style="margin-top: -11px; margin-bottom: -3px;">
										<c:forEach items="${pd.document.references}" var="reference">
										<li style="margin-left: 15px; margin-right: 15px;">
											<span class="ref" id="/ref/document/${reference.referenced.id}" data-inline="true">
											
												<h3>${reference.referenced.name}</h3>
												<p><strong>
													<c:forEach items="${reference.referenced.authors}" var="author">
													${author.firstName} ${author.lastName}; 
													</c:forEach>
												</strong></p>
												<p class="ui-li-aside"><strong>${reference.referenced.publicationYear}</strong></p>
											
											</span>
										</li>
										</c:forEach>
									</ul>
								</div>
								<div data-role="collapsible">
									<h3>Citatations</h3>
									<ul data-role="listview" style="margin-top: -11px; margin-bottom: -3px;">
										<c:forEach items="${pd.document.referenced}" var="referenced">
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
										</c:forEach>
									</ul>
								</div>
								<div data-role="collapsible">
									<h3>Suggestions</h3>
									<p>No suggestions just yet.</p>
								</div>
								<div data-role="collapsible">
									<h3>Rating</h3>
									<div class="basic" id="rating_${pd.rating}_${pd.id}"></div>
									
									<ul class="ul_current">
										<c:forEach items="${pd.ratingItems}" var="item">
											<li id="li${item.id}"><input id="ratingItem${item.id}" value="${item.content}" width="70%" onchange="saveRatingItem(${item.id})") /><button id="${item.id}" onclick="removeListItem(${item.id})">X</button>
										</c:forEach>
									</ul>
									<input id="input_listName" />
									<button id="btn_createList" onclick="addListItem()">add</button>
								</div>
								<div data-role="collapsible">
									<h3>Notes</h3>
									<p>
									<div data-role="fieldcontain">
										<span id="notesMessage"></span> <label for="notes">Notes</label>
										<textarea rows="4" name="notes" id="notes" cols="">${pd.notes}</textarea>
										<input type="submit" value="Save notes" onclick="saveNotes()" />
									</div>
									</p>
								</div>
							</div>

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

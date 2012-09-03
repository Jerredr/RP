<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<title>ResearchPad</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/jquery.mobile-1.0.css" />" />
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery-1.6.4.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.mobile-1.0.min.js" />"></script>

<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/style.css"/>" />
<!--
      jCarousel library
    -->
<script type="text/javascript"
	src="<c:url value="/resources/js/jquery.jcarousel.researchpad.js" />"></script>
<!--
      jCarousel skin stylesheet
    -->
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/css/skin.css" />" />
<style type="text/css">

/**
     * Overwrite for having a carousel with dynamic width.
     */
.jcarousel-skin-tango .jcarousel-container-horizontal {
	width: 100%;
	height: 100%;
}

.jcarousel-skin-tango .jcarousel-clip-horizontal {
	width: 100%;
	height: 100%;
}

div#sidebar {
	position: absolute;
	top: 0;
	bottom: 0;
	left: 0;
	width: 75px;
	height: 100%;
	background-color: yellow;
}
</style>

<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery('#mycarousel').jcarousel({
			initCallback : carousel_callback
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
		$.post("/researchPad/document/removerating", {
			itemId : id
		}, function(data) {
			
		});
		$('.ul_current li:#li' + id).remove();
	}

	function addListItem() {
		// TODO check whether ratingItem != ""
		var ratingItemId = 0;
		$.post("/researchPad/document/addrating", {
			pdId : <c:out value="${pd.id}"/>,
			ratingItem : $('#input_listName').val()
		}, function(data) {
			ratingItemId = data;
		});

		$('.ul_current').append(
				'<li id="li' + ratingItemId + '"><input id="ratingItem'
						+ ratingItemId + '" value="'
						+ $('#input_listName').val() + '" /><button id="'
						+ ratingItemId + '" onclick="removeListItem('
						+ ratingItemId + ')">X</button></li>');
		
	}
</script>


<script type="text/javascript"
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
</script>
</head>
<body>
	<!-- Start of first page -->
	<div data-role="page" class="foo">
		<div data-role="content" style="padding: 0px">
			<div id="sidebar" class="sidebar" style="pfloat: left;">Sidebar</div>
			<div id="mainContent"
				style="float: left; height: 100%; top: 0; bottom: 0; background-color: blue; width: 100%; margin-left: 75px;">
				<ul id="mycarousel" class="jcarousel-skin-tango">
					<li style="width: 50%;">
						<div style="background-color: red; height: auto;">
							<div id="wrapper">
								<div id="scroller">
									${pd.document.content} <a id="add-to" href="#">add</a><br>
									<span class="ref" id="/ref/document/1" data-role="button"
										data-inline="true">Open document</span><br> <span
										class="ref" id="/ref/document/2" data-role="button"
										data-inline="true">Open document 2</span> test<br> test<br>
									test<br> test<br> test<br> test<br> test<br>
									test<br> test<br> test<br> test<br> test<br>
									test<br> test<br> test<br> test<br> test<br>
									test<br> test<br> test<br> test<br> test<br>
									test<br> test<br> test<br> test<br> test<br>
									test<br> test<br> test<br> test<br> test<br>
									test<br> test<br> test<br>
								</div>
							</div>
						</div>
					</li>
					<li style="width: 50%;">
						<div style="background-color: green; height: auto;">
							<h3>${pd.document.name}</h3>
							<p>
								<strong>Authors:</strong>
								<c:forEach items="${pd.document.authors}" var="author">
									<span class="ref" id="/ref/author/${author.id}"
										data-role="button" data-inline="true">${author.firstName}
										${author.lastName}</span>
									<!--  <a href="/ref/author/${author.id}" data-role="button"
										data-inline="true">${author.firstName} ${author.lastName}</a>-->
								</c:forEach>
							</p>
							<p>
								<strong>Tags:</strong> TODO
							</p>

							<div data-role="collapsible-set" data-mini="true">
								<div data-role="collapsible" data-collapsed="false">
									<h3>References</h3>
									<c:forEach items="${pd.document.references}" var="reference">
										<span class="ref"
											id="/ref/document/${reference.referenced.id}"
											data-role="button" data-inline="true">${reference.referenced.name}</span>
										<br>
									</c:forEach>
								</div>
								<div data-role="collapsible">
									<h3>Citatations</h3>
									<c:forEach items="${pd.document.referenced}" var="referenced">
										<span class="ref" id="/ref/document/${referenced.document.id}"
											data-role="button" data-inline="true">${referenced.document.name}</span>
										<br>
									</c:forEach>
								</div>
								<div data-role="collapsible">
									<h3>Suggestions</h3>
									<p>I'm the collapsible set content for section B.</p>
								</div>
								<div data-role="collapsible">
									<h3>Rating</h3>
									<ul class="ul_current">
										<c:forEach items="${pd.ratingItems}" var="item">
											<li id="li${item.id}"><input id="ratingItem${item.id}" value="${item.content}" data-role="none" /><button id="${item.id}" data-role="none" onclick="removeListItem(${item.id})">X</button>
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
										<input type="submit" value="SaveNotes" onclick="saveNotes()" />
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

/*******************************************************************************
 * Variables *
 ******************************************************************************/

// Create a CommunityBuilder instance
var token = localStorage.getItem("token");
var communityBuilder = new CommunityBuilder(token);
var currentPage = 0;

// If there is no token, login first
if (token == null) {
	window.location.href = "login.html";
}

/*******************************************************************************
 * Functions *
 ******************************************************************************/

// 
// Extending string functions
// 
/**
 * Capitalizes the first letter of a string
 * 
 * @returns {String}
 */
String.prototype.capitalizeFirstLetter = function() {
	return this.charAt(0).toUpperCase() + this.slice(1);
}

/**
 * Replaces all wildcards in a string. A wildcard is defined by putting two
 * percentage signs (%) arounds a string.
 * 
 * E.g.: Hello %name%
 * 
 * In this example string %name% will be replaced if a name wildcard is passed
 * in the paramater list
 * 
 * @param wildcards
 *            list of key (wildcard) and values
 * @returns {String}
 */
String.prototype.replaceWildcards = function(wildcards) {
	var str = this;
	for ( var wildcard in wildcards) {
		str = str.replace('%' + wildcard + '%', wildcards[wildcard]);
	}
	return str;
}

//
// Page Load
//

/**
 * When the page is done loading
 */
$(document).ready(generateMenu);

//
// API call failed
// 

/**
 * Something wrong with a call
 */
var fail = function(e) {
	// If we have no access -> token is wrong
	if (e.status === 401) {
		window.location.href = "login.html";
	} else {
		// Other error
		var error = JSON.parse(e.responseText);
		alert(error.errorMessage);
	}
}

// 
// Interface Generation Funcs
//

/**
 * Generates the main menu
 */
function generateMenu() {
	$.each(items, function(i, page) {
		if (!page.menu)
			return;

		var item = $('<li>');
		var link = $('<a href="#">');
		var icon = $('<i class="fa fa-fw">');
		icon.addClass("fa-" + page.icon);
		link.append(icon);
		link.append(' Manage ');
		link.append(page.name.capitalizeFirstLetter());
		link.click(function() {
			loadPage(page, 0);
		})
		item.append(link);
		$(".leftnav ul").append(item);
	})
}

/**
 * Function used to list data
 */
function generateTable(pageInfo, page, props) {
	// Check if there is data
	var data = page.content ? page.content : page;
	if (data.length == 0)
		return;

	// Generate the table

	// Function that returns all the column names
	var getColumnNames = function() {
		var keys = [];
		for ( var j in data[0]) {
			var sub_key = j;
			if ($.inArray(sub_key, [ "id", "members" ]) == -1) {
				keys.push(sub_key);
			}
		}
		return keys;
	}

	// Function that creates a table head
	var generateTableHead = function(columns) {
		var thead = $('<thead class="thead-default">');
		var row = $('<tr>');
		$.each(columns, function(i, column) {
			var td = $("<th>");
			td.html(column.capitalizeFirstLetter());
			row.append(td);
		});
		row.append('<th>Operations</th>');
		return thead.append(row);
	}

	// Function that generates a row
	var generateRow = function(columns, row) {
		var html = $('<tr>');

		if (row.disabled)
			html.addClass('disabled');

		$.each(columns, function(i, column) {
			var val = row[column];
			if (column == 'gender')
				val = val ? 'Female' : 'Male';

			if (typeof val === 'object' && val) {
				if (val.name) {
					val = val.name;
				} else if (val.username) {
					val = val.username;
				} else if (val.fileLocation) {
					val = val.fileLocation;
				} else if (val.title) {
					val = val.title;
				} else {
					val = "object";
				}
			}

			html.append('<td>' + val + '</td>');
		});

		var td = $('<td>');
		td.append(generateButtons(pageInfo, row, props));
		html.append(td);
		return html;
	}

	var columns = getColumnNames();

	// Add rows to table
	var tbody = $("<tbody>");
	$.each(data, function(i, row) {
		tbody.append(generateRow(columns, row));
	});

	$('#list').append(generateTableHead(columns));
	$("#list").append(tbody);
}

/**
 * Generates the buttons for a row in a table
 * 
 * @param pageInfo
 * @param row
 * @param props
 * @returns {String}
 */
function generateButtons(pageInfo, row, props) {
	// Function that generates a button
	var generateButton = function(color, iconName, text, action) {
		var button = $('<button class="btn">');
		var icon = $('<i class="fa">')

		button.addClass("btn-" + color);
		if (typeof action == 'function')
			button.click(action);
		else
			button.attr('onclick', action)

		icon.addClass('fa-' + iconName);

		button.append(icon);
		button.append(' ');
		button.append(text);

		return button;
	}

	var buttonGroup = $('<div class="btn-group btn-group-xs">');

	// If resource is editable
	if (pageInfo.editable) {
		buttonGroup.append(generateButton("primary", "edit", "Edit",
				function() {
					showAdd(pageInfo, row.id, props);
				}));
	}

	// Custom buttons
	$.each(pageInfo.buttons, function(i, button) {
		if (button.condition) {
			button = row[button.condition] ? button.right : button.wrong;
		}

		var action = button.action.replaceWildcards(row);
		action = action.replaceWildcards(props);

		buttonGroup.append(generateButton(button.color, button.icon,
				button.text, action));

	});

	// If resource is deletable
	if (pageInfo.deleteable) {
		buttonGroup.append(generateButton("danger", "close", "Delete",
				function() {
					del(pageInfo, row.id, props);
				}));
	}

	return buttonGroup;
}

/**
 * Generates pagination if there are multiple pages for a resource
 * 
 * @param pageInfo
 * @param page
 * @param props
 */
function generatePagination(pageInfo, page, props) {
	// Deal with the pagination
	if (page.first == undefined || page.totalPages == 1) {
		$('nav.pagination').hide();
		return;
	}

	var props = JSON.stringify(props);

	// Function that adds a pagination item
	var addPagelink = function(pageNumber, text, disabled) {
		var item = $('<li>');
		var link = $('<a href="#">');
		link.html(text);

		if (!disabled)
			link.click(function() {
				loadPage(pageInfo, pageNumber, props);
			});
		if (page.number == pageNumber)
			item.addClass("active");
		if (disabled)
			item.addClass("disabled");

		item.append(link);
		$('ul.pagination').append(item);
	}

	// Which pages do we want to display
	var visiblePageLinks = [ page.number ];
	var firstPageLink = page.number;
	var lastPageLink = page.number;
	var oldLength = 0;
	while (visiblePageLinks.length < 6 && oldLength != visiblePageLinks.length) {
		oldLength = visiblePageLinks.length;
		if (firstPageLink > 0)
			visiblePageLinks.unshift(--firstPageLink)
		if (lastPageLink + 1 < page.totalPages)
			visiblePageLinks.push(++lastPageLink)
	}

	// Add the links to the pages
	addPagelink(page.number - 1, '&laquo;', page.first);
	addPagelink(0, 'First', page.first);
	$.each(visiblePageLinks, function(i, link) {
		addPagelink(link, parseInt(link) + 1, false);
	});
	addPagelink(page.totalPages - 1, 'Last', page.last);
	addPagelink(page.number + 1, '&raquo;', page.last);
}

/**
 * Adds options from the database in a select field
 * 
 * @param resource
 * @param text
 * @param selected
 */
function fillSelect(resource, text, selected) {
	var done = function(data) {
		var data = data.content;
		console.log(data);
		$.each(data, function(index, row) {
			var sel = selected == row.id ? 'selected' : '';
			var option = text.replaceWildcards(row);
			$("select#" + resource).append(
					'<option value="' + row.id + '" ' + sel + '>' + option
							+ '</option>');
		});
	}

	communityBuilder.getPage(resource, 0, done, fail);
}

/**
 * Load a page with data
 * 
 * @param pageInfo
 *            Which resource
 * @param page
 *            Which page
 * @param props
 */
function loadPage(pageInfo, page, props) {
	currentPage = page;

	// Function that builds the page
	var buildPage = function() {
		// First update the standard buttons and text on the list page
		var addFunction = 'showAdd(pageInfo, undefined, props)';

		if (pageInfo.addFunction != undefined)
			addFunction = pageInfo.addFunction.replaceWildcards(props);

		if (pageInfo.addable === false)
			$('.add-button').hide();

		$('.name-of-list').html(pageInfo.nameSingular.capitalizeFirstLetter());
		$('.name-of-list-plural').html(pageInfo.name.capitalizeFirstLetter());
		$('.add-button').click(function() {
			eval(addFunction)
		});

		// Generation after data is loaded
		var done = function(data) {
			generateTable(pageInfo, data, props);
			generatePagination(pageInfo, data, props);
		}

		// Load the data
		var path = pageInfo.path.replaceWildcards(props);
		communityBuilder.getPage(path, page, done, fail);
	}

	$(".main").load("pages/list.html", buildPage);
}

//
// Add / Edit / Delete
//

function showAdd(pageInfo, objectId, props) {
	// Generates an input element
	var generateInput = function(field, type, val){
		var input = $('<input class="form-control">');
		input.attr('type', type);
		input.attr('name', field.name);
		input.attr('placeholder', field.name.capitalizeFirstLetter());
		input.val(val);
		return input;
	}
	
	// Generates a select element
	var generateSelect = function(field, val){
		var select = $('<select class="form-control">');
		select.attr('name', field.name);
		select.attr('id', field.list);
		
		if (field.options != undefined) {
			$.each(field.options, function(i, option){
				var x = $('<option>');
				
				if (field.name == 'gender'){
					val++;
				}
				
				if (val == option.value){
					x.attr('selected', true);
				}
			
				x.val(option.value);
				x.text(option.name);
				
				select.append(x);
			});
		}
		
		return select;
	}
	
	// Generates the page
	var addFields = function(object) {
		var fields = pageInfo.fields;
		$(".objectname").html(pageInfo.nameSingular.capitalizeFirstLetter());

		if (objectId == undefined) {
			$(".add-edit").html('Add');
			$("button.add").click(function() {
				add(pageInfo, props);
			});
		} else {
			$(".add-edit").html('Edit');
			$("button.add").click(function() {
				edit(pageInfo, objectId, props);
			});
		}

		for ( var i in fields) {
			var field = fields[i];

			if (object != undefined && field.noedit)
				continue;

			var inputGroup = $('<div class="input-group">');
			inputGroup.append('<span class="input-group-addon">' + field.name.capitalizeFirstLetter() + '</span>');

			switch (field.type) {
			case 'text':
			case 'password':
				inputGroup.append(generateInput(field, field.type, object != undefined ? object[field.name] : ''));
				break;
			case 'image':
				inputGroup.append(generateInput(field, 'file', ''));
				break;
			case 'select':
				inputGroup.append(generateSelect(field, object != undefined ? object[field.name] : -1));
				break;
			}

			$("#add .fields").append(inputGroup);
			$("#add .fields").append('<br>');
			
			if (field.list)
				fillSelect(field.list, field.optionText, object != undefined && object[field.name] != undefined ? object[field.name].id : 0);
		}
	}

	var loadObject = function() {
		communityBuilder.get(objectId, pageInfo.path.replaceWildcards(props),
				addFields, fail);
		
	}

	var callback = objectId != undefined ? loadObject : function() {
		addFields();
	}

	$(".main").load("pages/add.html", callback);
}

/**
 * Add a new item to the database
 * 
 * @param pageInfo
 * @param props
 */
function add(pageInfo, props) {
	var data = $("form#add").serialize();
	communityBuilder.add(data, pageInfo.path.replaceWildcards(props),
			function() {
				loadPage(pageInfo, currentPage, props)
			}, fail);
}

/**
 * Edit item with id in the database
 * 
 * @param pageInfo
 * @param id
 * @param props
 */
function edit(pageInfo, id, props) {
	var data = $("form#add").serialize();
	communityBuilder.edit(id, data, pageInfo.path.replaceWildcards(props),
			function() {
				loadPage(pageInfo, currentPage, props)
			}, fail);
}

/**
 * Delete item with id from database
 * 
 * @param pageInfo
 * @param id
 * @param props
 */
function del(pageInfo, id, props) {
	communityBuilder.del(id, pageInfo.path.replaceWildcards(props), function() {
		loadPage(pageInfo, currentPage, props)
	}, fail);
}

//
// Change Password
//

/**
 * Show the change password page for user with id and username
 */
function showChangePassword(id, username) {
	$(".main").load("pages/password.html", function() {
		$('#username').text(username);
		$('button.change').click(function() {
			changePassword(id);
		});
	});
}

/**
 * Change the password for a user on the server
 */
function changePassword(id) {
	var password = $('#password').val();
	var confirmPassword = $('#confirm-password').val();

	if (password != confirmPassword) {
		alert("Given passwords do not match");
		return false;
	}

	var data = $("form#change-password").serialize();

	communityBuilder.edit(id, data, items.users.name, function() {
		loadPage(items.users, currentPage)
	}, fail);
}

//
// Enable / Disable User
//

function disableUser(id) {
	if (confirm("Are you sure that you want to disable this user?")) {
		communityBuilder.edit(id, 'disabled=2', 'users', function() {
			loadPage(items.users, currentPage);
		}, fail);
	}
}

function enableUser(id) {
	communityBuilder.edit(id, 'disabled=1', 'users', function() {
		loadPage(items.users, currentPage);
	}, fail);
}

//
// Add / Remove member from a class
//

function addMember(classId, userId) {
	var done = function() {
		loadPage(items.classMembers, 0, {
			'classId' : classId
		});
	}

	communityBuilder.add("user=" + userId, "classes/" + classId + "/members", done, fail);
}

function removeMember(classId, memberId) {
	var done = function() {
		loadPage(items.classMembers, 0, {
			'classId' : classId
		});
	}

	communityBuilder.del(memberId, "classes/" + classId + "/members", done, fail);
}

//
// Other functions -- UNUSED / TODO
//

function showEditMoment(momentId) {
	$(".main")
			.load(
					"pages/editmoment.html",
					function() {
						var done = function(moment) {
							console.log(moment);
							console.log(moment.length);
							var total=moment.length+1;
							$.each(moment,function(index, momentPart) {
										/*$('#moment-content').append(
													'<form class="paragraph" id="paragraph'+(index+1)+'">'    
													+'<h1>Paragraph-'+(index+1)+'</h1>'
													+'<div class="input-group">'
														+'<span class="input-group-addon">Text</span>'
														+'<textarea class="form-control text_content" style="height:100px">'
														+momentPart.text
														+'</textarea>'  
													+'</div>'
													+'<br>'
													+'<div class="input-group">'
														+'<span class="input-group-addon">Picture</span>'
														+'<input type="file" class="form-control post-file" name="file" />'
														+'<input type="button" onclick="uploadImage('+(index+1)+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
														+'<span id="picId'+(index+1)+'" style="display:none;"><span>'
													+'</div>'
													+'<div class="input-group btn-bar">'
														+'<input type="button" onclick="" value="cancel" class="btn btn-primary"/>'
														+'<input type="button" onclick="changeMomentPart('
														+momentId+','
														+momentPart.id+','
														+(index+1)
														+')" value="submit" class="btn btn-primary"/>'							
													+'</div>'
												+'</form>'
										)*/
											var picId;
											if(momentPart.picture==null){
												picId="#";}
											else{
												picId=momentPart.picture.id;
											}
											$('#moment-content').append(
													'<form class="paragraph" id="paragraph'+(index+1)+'">'    
													+'<h1>Paragraph-'+(index+1)+'</h1>'
													+'<div class="input-group">'
														+'<span class="input-group-addon">Text</span>'
														+'<textarea class="form-control text_content" style="height:100px" readonly="readonly">'
														+momentPart.text
														+'</textarea>'  
													+'</div>'
													+'<br>'
													+'<div class="input-group picBar">'
														+'<span class="input-group-addon">Picture</span>'
														+'<img src="http://localhost:8080/daringduckBackend/api/pictures/'
														+picId
														+'" width="500px;"/>'
													+'</div>'
													+'<div class="input-group btn-bar">'
														+'<input type="button" onclick="editStyle('
														+momentId+','
														+momentPart.id+','
														+(index+1)+','
														+'88'+','
														+picId
														+')" value="edit" class="btn btn-primary"/>'				
													+'</div>'
												+'</form>'
										);
										$("#para-menu").append(
											'<li><a href="#paragraph'
												+(index+1)
												+'">Paragraph-'
												+(index+1)
												+'</a></li>'
												);
												
									});
							$('#addParagraph').append(
								'<form class="paragraph" id="paragraph'+(total)+'">'    
								+'<h1>Paragraph-'+(total)+'</h1>'
								+'<div class="input-group">'
									+'<span class="input-group-addon">Text</span>'
									+'<textarea class="form-control" style="height:100px" id="text_content"></textarea>'  
								+'</div>'
								+'<br>'
								+'<div class="input-group">'
									+'<span class="input-group-addon">Picture</span>'
									+'<input type="file" class="form-control post-file" name="file" />'
									+'<input type="button" onclick="uploadImage('+(total)+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
									+'<span id="picId'+(total)+'" style="display:none;"><span>'
								+'</div>'
								+'<div class="input-group btn-bar">'
									+'<input type="button" onclick="" value="cancel" class="btn btn-primary"/>'
									+'<input type="button" onclick="addMomentPart('
									+momentId+","+total
									+')" value="submit" class="btn btn-primary"/>'							
								+'</div>'
							+'</form>'
								)

						}

						communityBuilder.getMomentPart(momentId,"moments",done, fail);
					});

}

/*used to add moment part*/
function addMomentPart(momentId,total){
	var text = $("#text_content").val();
	var part=total;
	var pictureId=$("#picId"+total).val();
	console.log(pictureId);
	var data="text="+text+"&part="+part+"&pictureId="+pictureId;
	communityBuilder.addMomentPart(momentId,data,"moments",
			function(){	
		console.log("jiazaiyemian");
				showEditMoment(momentId);
			}, fail);
}
/*used to change moment part*/
function changeMomentPart(momentId,momentPartId,index){
	console.log("index="+index);
	var text = $('#paragraph'+index+' .text_content').val();
	console.log(text+"===========");
	var part=index+1;
	var pictureId=$("#picId"+(index)).val();
	console.log(pictureId);
	var data="text="+text+"&pictureId="+pictureId;
	communityBuilder.changeMomentPart(momentPartId,data,"moments",
			function(){		
				//loadPage(pageInfo, currentPage, props)
				showEditMoment(momentId);
			}, fail);
}

function uploadImage(index) {
	console.log("Add Picture");

	var data = $('#Paragraph'+index+' .post-file').val();
	console.log(data);
	var form = new FormData();
	form.append('file',$('#paragraph'+index+' .post-file')[0].files[0]);
	
	var done = function(data) {
		console.log(data);
		$("#picId"+index).val(data.id);	}

	communityBuilder.uploadImage(form, done, fail);

}
/*function editStyle(momentId,momentPartId,index,text,picture){
	$('#paragraph'+index+' .text_content').removeAttr("readonly");
	$('#paragraph'+index+' .picBar').html(
			'<span class="input-group-addon">Picture</span>'
			+'<input type="file" class="form-control post-file" name="file" />'
			+'<input type="button" onclick="uploadImage('+(index)+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
			+'<span id="picId'+(index)+'" style="display:none;"><span>');
	$('#paragraph'+index+' .btn-bar').html(
			'<input type="button" onclick="noEditStyle('
			+momentId+','
			+momentPartId+','
			+index+','
			+text+','
			+picture
			+')" value="cancel" class="btn btn-primary"/>'
			+'<input type="button" onclick="changeMomentPart('
			+momentId+','
			+momentPartId
			+','
			+(index)
			+')" value="submit" class="btn btn-primary"/>');
	
}*/
function editStyle(momentId,momentPartId,index,text,picture){
	$('#paragraph'+index+' .text_content').removeAttr("readonly");
	$('#paragraph'+index+' .picBar').html(
			'<span class="input-group-addon">Picture</span>'
			+'<input type="file" class="form-control post-file" name="file" />'
			+'<input type="button" onclick="uploadImage('+(index)+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
			+'<span id="picId'+(index)+'" style="display:none;"><span>');
	$('#paragraph'+index+' .btn-bar').html(
			'<input type="button" onclick="noEditStyle('
			+momentId+','
			+momentPartId+','
			+index+','
			+text+','
			+picture
			+')" value="cancel" class="btn btn-primary"/>'
			+'<input type="button" onclick="changeMomentPart('
			+momentId+','
			+momentPartId
			+','
			+(index)
			+')" value="submit" class="btn btn-primary"/>');
	
}
function noEditStyle(momentId,momentPartId,index,text,picture){
	$('#paragraph'+index+' .text_content').attr("readonly","readonly");
	$('#paragraph'+index+' .picBar').html(
			'<span class="input-group-addon">Picture</span>'
			+'<img src="http://localhost:8080/daringduckBackend/api/pictures/'
			+picture
			+'" width="500px;"/>');
	$('#paragraph'+index+' .btn-bar').html(
			'<input type="button" onclick="editStyle('
			+momentId+','
			+momentPartId+','
			+(index)+','
			+text+','
			+picture
			+')" value="edit" class="btn btn-primary"/>');
}


function addContent() {
	$('#moment-content')
			.append(
					'<br>'
							+ '<div class="input-group">'
							+ '<span class="input-group-addon">Text</span>'
							+ '<textarea class="form-control" style="height:100px"></textarea>'
							+ '</div>'
							+ '<br>'
							+ '<div class="input-group">'
							+ '<span class="input-group-addon">Picture</span>'
							+ '<input type="file" class="form-control" id="post-file" name="file" />'
							+ '</div>');

}
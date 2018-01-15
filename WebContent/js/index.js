/*******************************************************************************
 * Variables *
 ******************************************************************************/

// Create a CommunityBuilder instance
var token = localStorage.getItem("token");
var communityBuilder = new CommunityBuilder(token);
var currentPage = 0;
var prevPage=0;

/*used to text picture uploading*/
//var imgPath="http://203.195.147.70:8080/daringduckBackend/api/pictures/";
var imgPath="http://localhost:8080/daringduckBackend/api/pictures/";

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
/*datestamp to format like yyyy-MM-dd h:m:s*/
function formatDateTime(timeStamp) {   
    var date = new Date();  
    date.setTime(timeStamp * 1000);  
    var y = date.getFullYear();      
    var m = date.getMonth() + 1;      
    m = m < 10 ? ('0' + m) : m;      
    var d = date.getDate();      
    d = d < 10 ? ('0' + d) : d;      
    var h = date.getHours();    
    h = h < 10 ? ('0' + h) : h;    
    var minute = date.getMinutes();    
    var second = date.getSeconds();    
    minute = minute < 10 ? ('0' + minute) : minute;      
    second = second < 10 ? ('0' + second) : second;     
    //return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second; 
    return y + '-' + m + '-' + d; 
}

function isEmptyObject(obj){
	for(var key in obj){
		return false;
	}
	return true;
}
function dateToStamp(dateId,dateStampId){
	var stringDate=$("#"+dateId).val();
	if(stringDate=="")
	{
		console.log("is nan");
		$('#date_field').val(0);
		return;
	}
	var dateStamp=Date.parse(new Date(stringDate));
	dateStamp=dateStamp/1000;
	$("#"+dateStampId).val(dateStamp);
	//console.log(stringDate + "的时间戳为：" + dateStamp);
	var test=formatDateTime(dateStamp);
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
	var enFlag=true,chFlag=false,Neflag=false;
	console.log(pageInfo.name);
	console.log(data);
	if (data.length == 0)
		return;
	if(!isEmptyObject(data) && pageInfo.name=='chapters'){
		$('.add-button').hide();
	}

	// Function that returns all the column names
	var getColumnNames = function() {
		var keys = [];
		for ( var j in data[0]) {
			console.log();
			var sub_key = j;
			if ($.inArray(sub_key, [ "email", "members","password","picturePosition","chapters"]) == -1) {
				keys.push(sub_key);
			}
		}
		/*extra define some pages' column sequence*/
		if(pageInfo.name=="userCourses"){                                                      
			keys=["courseId","picture", "name","date", "teacherId", "teacherName"];
		}
		if(pageInfo.name=="userChapters"){                                                      
			keys=["chapterId", "title","passOrNot","date","passOrNot","score", "teacherId","teacherName",];
		}
		if(pageInfo.name=="courses" || pageInfo.name=="availableCourses"){
			keys=["id","picture","course_name"];		
		}
		if(pageInfo.name=="chapters"){
			keys=[ "courseId","id","chapter_title","requiredOrNot"];
		}
		if(pageInfo.name=="users"){
			keys=["id","picture","username","role","club","gender", "nickname", "phone", "wechat", "disabled"];
		}
		console.log(keys)
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
				//val = val ? 'Female' : 'Male';
				val = val ? 'Male':'Female';
			if(column=='requiredOrNot'){
				val=(val==true)?'yes':'no';
			}
			if(column=='course_name'){
				val=row['english_name']+'<br>'+row['chinese_name']+'<br>'+row['dutch_name'];
			}
			if(column=='chapter_title'){
				val=row['english_title']+'<br>'+row['chinese_title']+'<br>'+row['dutch_title'];
			}
			if(column=='date'){
				if(row['date']!=0){
					/*var date=new Date(row['date']);
					val=date.format('yyyy-MM-dd h:m:s');*/
					val=formatDateTime(row['date']);
				}
			}
			if(column=='picture'){
				console.log(val);
				//val='picture';
				if(val!=null)
				{
					val='<img src="'+imgPath+val.id+'" style="width:40px;height:40px;border-radius:50%;"/>'
				}
				else
					val='no picture';
			}
			if(column=='pictureId'){
				console.log(val);
				//val='picture';
				if(val!='')
				{
					val='<img src="'+imgPath+val+'" style="width:40px;height:40px;border-radius:50%;"/>'
				}
				else
					val='no picture';
			}
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
					if(pageInfo.name=='userCourses'){
						showAdd(pageInfo, row.courseId, props);
					}
					else if(pageInfo.name=='userChapters'){
						showAdd(pageInfo, row.chapterId, props);
					}
					else{
						showAdd(pageInfo, row.id, props);
					}
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
	if (page.first == undefined){
		console.log("warning!!!!!!!!!!!it is hide because of undefine")
	}
	console.log(page);
	if (page.first == undefined || page.totalPages == 1) {
		$('nav.pagination').hide();
		console.log("warning!!!!!!!!!!!it is hide");
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
function fillSelect(name,resource, text, selected) {	
	var done = function(data) {
		if(resource.indexOf("users/getUserOfTeacherAndAdmin")>=0){}
		else
			var data = data.content;
		console.log(data);
		$.each(data, function(index, row) {
			var sel = selected == row.id ? 'selected' : '';
			var option = text.replaceWildcards(row);
			console.log(option);
			$("select#" + name).append(
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
	console.log(props)
	prevPage=currentPage;
	currentPage = page;

	// Function that builds the page
	var buildPage = function() {
		// First update the standard buttons and text on the list page
		var addFunction = 'showAdd(pageInfo, undefined, props)';

		if (pageInfo.addFunction != undefined)
			addFunction = pageInfo.addFunction.replaceWildcards(props);

		if (pageInfo.addable === false)
			$('.add-button').hide();

		// If resource is returnable
		if (pageInfo.returnable) {
			$('.return-button').on('click',function(){
				returnPage(pageInfo.name,props);
			})
		}
		else
			$('.return-button').hide();

		$('.name-of-list').html(pageInfo.nameSingular.capitalizeFirstLetter());
		$('.name-of-list-plural').html(pageInfo.name.capitalizeFirstLetter());
		$('.add-button').click(function() {
			eval(addFunction)
		});

		// Generation after data is loaded
		var done = function(data) {
			generateTable(pageInfo, data, props);
			/*this is used to replace the param and get the correct path*/
			//pageInfo.path = pageInfo.path.replaceWildcards(props);
			generatePagination(pageInfo, data, props);		
		};

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
	console.log(objectId);
	var generateInput = function(field, type, val){	
		console.log(val);
		if(type=="file"){
			var input=$('<div></div>')
			var inputfield = $('<input class="form-control">');
			if(val!="" && val!=null){
				input.append('<img class="imgBar" src="'+imgPath+val.id+'" style="width:60px;height:60px;"/>');
			}
			inputfield.attr('type', type);
			inputfield.attr('name', field.name);
			inputfield.attr('placeholder', field.name.capitalizeFirstLetter());
			input.append(inputfield);
			return input;
			
		}
		else{
			var input = $('<input class="form-control">');
			input.attr('type', type);
			input.attr('name', field.name);
			input.attr('placeholder', field.name.capitalizeFirstLetter());
			input.val(val);
			/*if(field.disabled=="disabled"){
				input.attr('disabled','disabled');
				console.log(field.value.replaceWildcards(props));
			}*/
			return input;
		}
	}
	
	var generateDate = function(field, type, val){
		console.log(field);
		var date_val="";
		if(val!="" && val!=null){
			date_val=formatDateTime(val);
		}
		var input = $('<div class="control-group">'
						+'<div class="controls date form_date" data-date="" data-date-format="yyyy-mm-dd" data-link-format="yyyy-mm-dd" data-link-field="date_val">'
							+'<input class="form-control" size="16" type="text" value="'+date_val+'" style="border-radius: 0 5px 5px 0;" readonly>'
							+'<span style="position:absolute;left: 50px;top: 20px;" class="add-on"><i class="icon-remove"></i></span>'
							+'<span style="position:absolute;left: 50px;top: 20px;" class="add-on"><i class="icon-th"></i></span>'
						+'</div>'
						+'<input type="hidden" id="date_val" value="'+date_val+'"/>'
						+'<input name="'+field.name+'" id="date_field" type="hidden" value="" />'
					+'</div>');
		return input;

	}
	
	// Generates a radio element
	var generateRadio = function(field, val){
		var radio = $('<div></div>');
		
		if (field.options != undefined) {
			$.each(field.options, function(i, option){
				var label=$('<label class="radio-inline"></label>')
				var x = $('<input type="radio">');
				x.attr('name', field.name);
				
				if (val == option.value){
					x.attr('checked', true);
				}
				x.val(option.value);
				
				radio.append(label.append(x).append(option.name));
			});
		}
		
		return radio;
	}
	
	// Generates a select element
	var generateSelect = function(field, val){
		console.log(val);
		console.log(field);
		var select = $('<select class="form-control">');
		select.attr('name', field.name);
		select.attr('id', field.name);
		
		if (field.options != undefined) {
			$.each(field.options, function(i, option){
				var x = $('<option>');
				
				if (field.name == 'gender' && val == (option.value-1)){
					x.attr('selected', true);
				}
							
				if (field.name != 'gender' && val == option.value){
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
		console.log(object);
		var tempUserId;//to be a para of uploadImage
		var userFlag=false;
		var tempCourseId;//to be a para of uploadImage
		var courseFlag=false;
		if(pageInfo.name=="users"){
			if(objectId != undefined){
				tempUserId=object.id;
				$('.fields').prepend(
					'<div class="input-group">'
						+'<span class="input-group-addon">Id</span>'
						+'<input class="form-control" type="text" readonly="true" value="'+object.id+'">'
					+'</div><br>')
			}
			/*else{
				$('input[type="password"]').val('dd246');
				console.log($('input[type="password"]').val());
			}*/
			userFlag=true;
		}
		if(pageInfo.name=="courses"){
			if(objectId != undefined){
				tempCourseId=object.id;
				$('.fields').prepend(
						'<div class="input-group">'
							+'<span class="input-group-addon">Id</span>'
							+'<input class="form-control" type="text" readonly="true" value="'+object.id+'">'
						+'</div><br>')
			}
			courseFlag=true;
		}
		/*add course name(no-edit field) in user course page*/
		if(pageInfo.name=="userCourses"){
			if(object != undefined){
				$('.fields').prepend(
						'<div class="input-group">'
							+'<span class="input-group-addon">Course</span>'
							+'<input class="form-control" type="text" readonly="true" value="'+object.name+'">'
						+'</div><br>')
			}
			courseFlag=true;
		}
		/*add chapter name(no-edit field) in user course page*/
		if(pageInfo.name=="userChapters"){
			if(object != undefined){
				$('.fields').prepend(
						'<div class="input-group">'
							+'<span class="input-group-addon">Chapter</span>'
							+'<input class="form-control" type="text" readonly="true" value="'+object.chapterTitle+'">'
						+'</div><br>')
			}
			courseFlag=true;
		}
		
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
		$('.return').on('click',function(){
			formReturnPage(pageInfo.name,props);
		})
		
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
				inputGroup.append(generateInput(field, 'file', object != undefined ? object[field.name] : ''));
				break;
			case 'select':
				inputGroup.append(generateSelect(field, object != undefined ? object[field.name] : -1));
				break;
			case 'date':
				inputGroup.append(generateDate(field, 'date',object != undefined ? object[field.name] : ''));
				break;
			case 'radio':
				inputGroup.append(generateRadio(field, object != undefined ? object[field.name] : -1));
				break;
			}

			$("#add .fields").append(inputGroup);
			$("#add .fields").append('<br>');
			
			if (field.list)
				fillSelect(field.name,field.list, field.optionText, object != undefined && object[field.name] != undefined ? object[field.name].id : 0);
			
			/*call the datepicker*/
			$('.form_date').datetimepicker({
		        language:  'fr',
		        weekStart: 1,
		        todayBtn:  1,
		        autoclose: 1,
		        todayHighlight: 1,
		        startView: 2,
		        minView: 2,
		        forceParse: 0
		    });
		}
		$(document).on("click","input[type='file']",function(e){
			if(userFlag==true){
				$(this).after('<input type="button" onclick="uploadUserImage('+tempUserId+')" value="UploadFile" class="btn btn-primary upload-btn"/>');
				/*hide the img here*/
				userFlag=false;
			}
			if(courseFlag==true){
				$(this).after('<input type="button" onclick="uploadCourseImage('+tempCourseId+')" value="UploadFile" class="btn btn-primary upload-btn"/>');
				courseFlag=false;
			}
		})
	}

	var loadObject = function() {
		//console.log(pageInfo.path.replaceWildcards(props));
		communityBuilder.get(objectId, pageInfo.path.replaceWildcards(props),
				addFields, fail);
	}

	var callback = objectId != undefined ? loadObject : function() {
		addFields();
	}
	
	/*$(document).on("click","input[type='file']",function(e){
		if(userFlag==true){
			$(this).after('<input type="button" onclick="uploadUserImage('+tempUserId+')" value="UploadFile" class="btn btn-primary upload-btn"/>');
			console.log(tempUserId+"wbuzhid222223");
		}
		else
			$(this).after('<input type="button" onclick="uploadCourseImage('+tempCourseId+')" value="UploadFile" class="btn btn-primary upload-btn"/>');
	})*/
	
	

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
	var path;
	console.log(pageInfo.path.replaceWildcards(props));
	/*if add user course, need gengerate a new path*/
	if(pageInfo.path.replaceWildcards(props).indexOf("users/getUserCourse")>=0){
		console.log("change path");
		path="users/addUserCourse";
		/*add userId to data*/
		var userId=pageInfo.path.replaceWildcards(props).substring(20,pageInfo.path.replaceWildcards(props).length-1);
		data=data+"&userId="+userId;
	}
	else if(pageInfo.path.replaceWildcards(props).indexOf("courses/")>=0 && pageInfo.path.replaceWildcards(props).indexOf("/chapters")>=0){
		var courseId=pageInfo.path.replaceWildcards(props).substring(8,pageInfo.path.replaceWildcards(props).length-9);
		console.log(courseId);
		communityBuilder.addChapterStep1(courseId, data, "courses", function(data){		
			/*insert new chapter id to the chapterid array(at "flag" position)*/
			var data2="lists="+data.id;
			communityBuilder.addChapterStep2(courseId, data2, "courses", function(data){
				loadPage(items.chapters,0,{
					'courseId' : courseId
				})
				console.log(data);
			}, fail)
		}, fail);	
	}
	else{
		path=pageInfo.path.replaceWildcards(props);
	}
	console.log(data);
	communityBuilder.add(data,path,function() {
				loadPage(pageInfo, currentPage, props)
			}, fail);
}

/**
 * Edit item with id in the database
 * 
 * @param pageInfo
 * 
 * @param id
 * @param props
 */
function edit(pageInfo, id, props) {
	var data;
	var path;
	if(pageInfo.path.replaceWildcards(props).indexOf("users/getUserCourse/")>=0){
		dateToStamp("date_val","date_field");
		data = $("form#add").serialize();
		path="users/changeUserCourse";
		/*add userId to data*/
		var userId=pageInfo.path.replaceWildcards(props).substring(20,pageInfo.path.replaceWildcards(props).length);
		var courseId=id;
		data="userId="+userId+"&courseId="+courseId+"&"+data;
		console.log(data);
		communityBuilder.editUserLink(data, path,
				function() {
					loadPage(pageInfo, currentPage, props)
				}, fail);
	}
	else if(pageInfo.path.replaceWildcards(props).indexOf("users/getUserChapter/")>=0){
		console.log($("#data_field").val());
		/*if($('#data_val').val()==undefined){
			console.log("yesssssssssss");
		}*/
		dateToStamp("date_val","date_field");
		if($("input[name='date']").val()==0){
			alert("Please fill date filed first!");
			return;
		}
		data = $("form#add").serialize();
		console.log(data);
		path="users/changeUserChapter";
		/*add userId to data*/
		console.log(pageInfo);
		console.log(pageInfo.path.replaceWildcards(props));
		console.log(id);
		/*get userId in string temp*/
		var temp=pageInfo.path.replaceWildcards(props).substring(21,pageInfo.path.replaceWildcards(props).length);
		endFlag=temp.indexOf("/");
		var userId=temp.substring(0,endFlag);
		var chapterId=id;
		data="userId="+userId+"&chapterId="+chapterId+"&"+data;
		//data="userId=1&chapterId=2&teacherId=14&score=3&passOrNot=false&date=123455555&comment=test";
		communityBuilder.editUserLink(data, path,
				function() {
					loadPage(pageInfo, currentPage, props)
				}, fail);
	}
	else{
		dateToStamp("date_val","date_field");
		data = $("form#add").serialize();
		path=pageInfo.path.replaceWildcards(props);
		communityBuilder.edit(id, data, path,
				function() {
					loadPage(pageInfo, currentPage, props)
				}, fail);
	}
	
}

/**
 * Delete item with id from database
 * 
 * @param pageInfo
 * @param id
 * @param props
 */
function del(pageInfo, id, props) {
	console.log(pageInfo)
	var result=confirm("Delete this "+pageInfo.nameSingular+"?");
	if(result==true){
		communityBuilder.del(id, pageInfo.path.replaceWildcards(props), function() {
			loadPage(pageInfo, currentPage, props)
		}, fail);
	}
}
function deleteMomentPart(momentId,momentPartId) {
	var result=confirm("Delete this moment paragragh?");
	if(result==true){
		communityBuilder.del(momentPartId, "moments/deleteMomentPart", function() {
			showEditMoment(momentId);
		}, fail);
	}
}
function deleteChapterPart(courseId,chapterId,chapterPartId){
	var result=confirm("Delete this chapter Part?");
	if(result==true){
		communityBuilder.del(chapterPartId, 'courses/deleteChapterPart/'+chapterId+'/parts', function() {
			showEditChapterPart(courseId,chapterId);
		}, fail);
	}
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
//Add / Remove course from a user
//

function addUserCourse(courseId, userId) {
	var done = function() {
		loadPage(items.userCourses, 0, {'userId': userId})
	}
	var data="userId="+userId+"&courseId="+courseId+"&teacherId=&passOrNot=false&date="
	console.log(data);
	communityBuilder.add(data,"users/addUserCourse", done, fail);
}

//
// Other functions -- UNUSED / TODO
//

function showEditMoment(momentId,userId) {
	console.log(userId+"!!!!!!");
	$(".main")
			.load(
					"pages/editmoment.html",
					function() {
						var done = function(moment) {
							console.log(moment);
							console.log(moment.content.length);
							var total=moment.content.length+1;
							$('.whereComeFrom').text(
									'userId:'
									+moment.userId+' /user nickname: '
									+moment.userName+' /moment title: '
									+moment.momentTitle);
							$.each(moment.content,function(index, momentPart) {
											var picId;
											var imagePart;
											if(momentPart.picture==null){
												picId="#";
												imagePart='<span>no picture</span>';
												}
											else{
												picId=momentPart.picture.id;
												imagePart='<img src="'
													+imgPath+picId
													+'" width="100%;"/>';
											}
											$('#moment-content').append(
													'<form class="paragraph" id="paragraph'+(index+1)+'">'    
													+'<h1>Paragraph-'+(index+1)+'</h1>'
													+'<div class="row">'
														+'<div class="col-sm-6">'
															+'<div class="input-group">'
																+'<span class="input-group-addon">Text</span>'
																+'<textarea class="form-control text_content" style="height:100px" readonly="readonly">'
																+momentPart.text
																+'</textarea>'  
															+'</div>'
														+'</div>'
														+'<div class="col-sm-6">'
															+'<div class="input-group picBar momentPic">'
																+imagePart
																//+'<span id="picId'+(index+1)+'" style="display:none;"></span>'
															+'</div>'
														+'</div>'
													+'</div>'
													+'<div class="btn-bar">'
														+'<button onclick="deleteMomentPart('
														+momentId+','
														+momentPart.id
														+')" type="button" class="btn btn-danger">'
														+'<i class="fa fa-close"></i> Delete</button>'
														+'<button onclick="editStyle('
														+userId+','
														+momentId+','
														+momentPart.id+','
														+(index+1)+','
														+'88'+','
														+picId
														+')" type="button" class="btn btn-primary">'
														+'<i class="fa fa-edit"></i> Edit</button>'
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
							$("#momentReturn").on("click",function(){
								loadPage(items.userMoments,0,{
									'userId' :userId
								})
							})
							$('#addParagraph').append(
								'<form class="paragraph" id="paragraph'+(total)+'">'    
								+'<h1>Paragraph-'+(total)+'</h1>'
								+'<div class="row">'
									+'<div class="col-sm-6">'
										+'<div class="input-group">'
											+'<span class="input-group-addon">Text</span>'
											+'<textarea class="form-control" style="height:300px" id="text_content"></textarea>'  
										+'</div>'
									+'</div>'
									+'<div class="col-sm-6">'
										+'<div class="input-group picBar momentPic">'
											+'<input type="file" class="form-control post-file" name="file" />'
											+'<input type="button" onclick="uploadImage('+(total)+',\'user\','+(userId)+','+(momentId)+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
											+'<span id="picId'+(total)+'" style="display:none;"></span>'
											+'<img class="imgBar" width="100%;" style="height:260px">'
										+'</div>'
									+'</div>'
								+'</div>'
								+'<div class="btn-bar">'
									+'<button onclick="addMomentPart('
									+momentId+","+total
									+')" type="button" class="btn btn-success"><i class="fa fa-check-square-o"></i> Submit</button>'							
								+'</div>'
							+'</form>'
								)

						};

						communityBuilder.getMomentPart(momentId,"moments",done, fail);
					});

}

/*used to add moment part*/
//function addMomentPart(momentId,total){
/*	var text = $("#text_content").val();
	var part=total;
	var pictureId=$("#picId"+total).val();
	console.log(pictureId);
	var data="text="+text+"&part="+part+"&pictureId="+pictureId;
	communityBuilder.addMomentPart(momentId,data,"moments",
			function(){	
		console.log("jiazaiyemian");
				showEditMoment(momentId);
			}, fail);
}*/
/*used to change moment part*/
function changeMomentPart(momentId,momentPartId,index){
	console.log("index="+index);
	var text = $('#paragraph'+index+' .text_content').val();
	console.log(text+"===========");
	var part=index+1;
	var pictureId=$("#picId"+index).val();
	console.log(pictureId);
	var data="text="+text+"&pictureId="+pictureId+"&part="+index;
	communityBuilder.changeMomentPart(momentPartId,data,"moments",
			function(){		
				//loadPage(pageInfo, currentPage, props)
				showEditMoment(momentId);
			}, fail);
}

/*function uploadImage(index,userId,type,typeId) {
	console.log("Add Picture");

	var form = new FormData();
	form.append('file',$('#paragraph'+index+' .post-file')[0].files[0]);
	form.append('id',userId);
	form.append('universalField',type+'_'+typeId);
	
	var done = function(data) {
		console.log(data);
		alert("upload successfully");
		$("#picId"+index).val(data.id);	}
	

	communityBuilder.uploadImage(form, done, fail);
}*/
/*for user type,typeId is userId,subId is momentId
 * for course type, typeId is courseId,subId is chapterId*/
function uploadImage(index,type,typeId,subId) {
	console.log("Add Picture");
	if(type=='user')
		sub='moment';
	else
		sub='chapter';
	
	var form = new FormData();
	oFile=$('#paragraph'+index+' .post-file')[0].files[0];
	fileType = oFile.type;
	imgSize=oFile.size;
	console.log(imgSize);
	if(imgSize < 256 * 1024){
		form.append('file',$('#paragraph'+index+' .post-file')[0].files[0]);
		form.append('type',type);
		form.append('id',type+'_'+typeId);
		form.append('subDirectory',sub+'_'+subId);
		console.log(form);
		
		var done = function(data) {
			console.log(data);
			alert("upload successfully");
			$("#picId"+index).val(data.id);	
			if(type=='user')
				$('#paragraph'+(index)+' .imgBar').attr('src',imgPath+data.id);
			else
				$('.imgBar').attr('src',imgPath+data.id);
		}
		communityBuilder.uploadImage(form, done, fail);
    } 
	else{
		console.log("pic resize");
		var fileReader   = new FileReader();
		fileReader.readAsDataURL(oFile);
		fileReader.onload = function(event){
			var result = event.target.result;   //返回的dataURL
			var image = new Image();
			image.src = result;
			image.onload = function(){  //创建一个image对象，给canvas绘制使用
				var cvs = document.createElement('canvas');
				var scale = 1;  
				if(this.width > 1280 || this.height > 1000){  //1000只是示例，可以根据具体的要求去设定  
					if(this.width > this.height){  
						scale = 1280 / this.width;
					}else{  
						scale = 1000 / this.height;  
					}  
				}
				cvs.width = this.width*scale;  
    			                cvs.height = this.height*scale;     //计算等比缩小后图片宽高
				var ctx = cvs.getContext('2d');  
				ctx.drawImage(this, 0, 0, cvs.width, cvs.height);   
                var newImageData = cvs.toDataURL(fileType, 0.8);   //重新生成图片，<span style="font-family: Arial, Helvetica, sans-serif;">fileType为用户选择的图片类型</span>
				var blob = dataURLtoBlob(newImageData);
				form.append('file',blob);
				form.append('type',type);
				form.append('id',type+'_'+typeId);
				form.append('subDirectory',sub+'_'+subId);
				console.log(form);
				
				var done = function(data) {
					console.log(data);
					alert("upload successfully");
					$("#picId"+index).val(data.id);	
					if(type=='user')
						$('#paragraph'+(index)+' .imgBar').attr('src',imgPath+data.id);
					else
						$('.imgBar').attr('src',imgPath+data.id);
				}
				communityBuilder.uploadImage(form, done, fail);
			}
			
		}
	}	
}

function uploadUserImage(userId) {
	console.log("Add User Picture");

	var form = new FormData();
	form.append('file',$('input[type="file"]')[0].files[0]);
	form.append('type','user');
	form.append('id','user_'+userId);
	form.append('subDirectory','');
	
	var done = function(data) {
		console.log(data);
		alert("upload successfully");
		$('input[type="file"]').after('<input type="text" name="pictureId" style="visibility:hidden;position: absolute;" value="'+data.id+'">');
		$('.imgBar').attr('src',imgPath+data.id)
	}

	communityBuilder.uploadImage(form, done, fail);

}
function uploadCourseImage(courseId)
{
	console.log("Add Course Picture");

	var form = new FormData();
	form.append('file',$('input[type="file"]')[0].files[0]);
	form.append('type','course');
	form.append('id','course_'+courseId);
	form.append('subDirectory','');
	
	var done = function(data) {
		alert("upload successfully");
		console.log(data);
		$('input[type="file"]').after('<input type="text" name="pictureId" style="visibility:hidden;position: absolute;" value="'+data.id+'">');
		$('.imgBar').attr('src',imgPath+data.id);
	}

	communityBuilder.uploadImage(form, done, fail);
}
/*function uploadImage(position) {
	console.log(position);

	var data = $('#Paragraph'+index+' .post-file').val();
	console.log(data);
	var form = new FormData();
	form.append('file',$('#paragraph'+index+' .post-file')[0].files[0]);
	
	var done = function(data) {
		console.log(data);
		$("#picId"+index).val(data.id);	}

	communityBuilder.uploadImage(form, done, fail);

}*/

function editStyle(userId,momentId,momentPartId,index,text,picture){
	$('#paragraph'+index+' .text_content').removeAttr("readonly");
	$('#paragraph'+index+' .picBar').html(
			'<input type="file" class="form-control post-file" name="file" />'
			+'<input type="button" onclick="uploadImage('+(index)+',\'user\','+(userId)+','+(momentId)+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
			+'<span id="picId'+(index)+'" style="display:none;"></span>'
			+'<img class="imgBar" src="'+imgPath+picture+'" width="100%" style="height:260px">');
		
	$('#paragraph'+index+' .btn-bar').html(
			'<button onclick="noEditStyle('
			+userId+','
			+momentId+','
			+momentPartId+','
			+index+','
			+text+','
			+picture
			+')" type="button" class="btn btn-primary">'
			+'<i class="fa fa-reply"></i> Cancel</button>'
			+'<button onclick="changeMomentPart('
			+momentId+','
			+momentPartId
			+','
			+(index)
			+')" type="button" class="btn btn-success">'
			+'<i class="fa fa-check-square-o"></i> Submit</button>');
	
}
function noEditStyle(userId,momentId,momentPartId,index,text,picture){
	$('#paragraph'+index+' .text_content').attr("readonly","readonly");
	$('#paragraph'+index+' .picBar').html(
			'<img src="'
			+imgPath+picture
			+'" width="100%;"/>');
	$('#paragraph'+index+' .btn-bar').html(
			'<button onclick="deleteMomentPart('
			/*+userId+','
			+momentId+','
			+momentPart.id+','
			+(index+1)+','*/
			+')" type="button" class="btn btn-danger">'
			+'<i class="fa fa-close"></i> Delete</button>'
			+'<button onclick="editStyle('
			+userId+','
			+momentId+','
			+momentPartId+','
			+(index)+','
			+text+','
			+picture
			+')" type="button" class="btn btn-primary">'
			+'<i class="fa fa-edit"></i> Edit</button>');
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

/*add paragragh in chapter part*/
function showEditChapterPart(courseId,chapterId) {
	$(".main")
			.load(
					"pages/editChapterPart.html",
					function() {
						var count=-1;
						var done = function(chapterParts) {
							console.log(chapterParts);
							console.log(chapterParts.content.length);
							var total=chapterParts.content.length+1;
							$('.whereComeFrom').text(
									'course id:'
									+chapterParts.courseId
									+' /course name: '
									+chapterParts.courseName+' /chapter title: '
									+chapterParts.chapterTitle);
							$.each(chapterParts.content,function(index, chapterPart) {
								var picId;
								count=index;
								//picId=chapterPart.picture.id;
								if(chapterPart.picture==null){
									picId="#";}
								else{
									picId=chapterPart.picture.id;
								}
								index=parseInt(index);
								$('#chapter-content').append(
										'<form class="paragraph" id="paragraph'+(index+1)+'">'										
										+'<h1>Chapter part-'+(index+1)+'</h1>'
										+'<span class="chapterPartId" style="display:none;">'+chapterPart.id+'</span>'
										+'<div class="row">'
										+'<div class="col-sm-6">'
											+'<ul class="nav nav-tabs">'
												+'<li class="active"><a href="#engText'+(index+1)+'" data-toggle="tab">English Text</a></li>'
												+'<li><a href="#chnText'+(index+1)+'" data-toggle="tab">Chinese Text</a></li>'
												+'<li><a href="#dutText'+(index+1)+'" data-toggle="tab">Dutch Text</a></li>'
											+'</ul>'
											+'<div class="tab-content">'
												+'<div class="tab-pane fade in active" id="engText'+(index+1)+'">'
													+'<div class="input-group">'
														/*+'<span class="input-group-addon">Text</span>'*/
														+'<textarea class="form-control text_content" readonly="readonly">'
														+chapterPart.english_text
														+'</textarea>'  
													+'</div>'
												+'</div>'
												+'<div class="tab-pane fade" id="chnText'+(index+1)+'">'
													+'<div class="input-group">'
														/*+'<span class="input-group-addon">Text</span>'*/
														+'<textarea class="form-control text_content" readonly="readonly">'
														+chapterPart.chinese_text
														+'</textarea>'  
													+'</div>'
												+'</div>'
												+'<div class="tab-pane fade" id="dutText'+(index+1)+'">'
													+'<div class="input-group">'
														/*+'<span class="input-group-addon">Text</span>'*/
														+'<textarea class="form-control text_content" readonly="readonly">'
														+chapterPart.dutch_text
														+'</textarea>'  
													+'</div>'
												+'</div>'
											+'</div>'
										+'</div>'
										+'<div class="col-sm-6">'
											+'<div class="input-group picBar">'
												//+'<span class="input-group-addon">Picture</span>'
												+'<img src="'
												+imgPath+picId
												+'" width="100%;"/>'
											+'</div>'
										+'</div>'
										+'</div><br>'
										+'<div class="row">'
											+'<div class="col-sm-6">'
												+'<div class="input-group">'
													+'<span class="input-group-addon">Course</span>'
													+'<select class="form-control" name="courseId"></select>'
												+'</div>'
											+'</div>'
										+'</div>'
										+'<div class="btn-bar">'
											+'<button onclick="deleteChapterPart('
											+courseId+','
											+chapterId+','
											+chapterPart.id
											+')" type="button" class="btn btn-danger">'
											+'<i class="fa fa-close"></i> Delete</button>'
											+'<button onclick="editChapterStyle('
											+courseId+','
											+chapterId+','
											+chapterPart.id+','
											+(index+1)+','
											+'88'+','
											+picId
											+')" type="button" class="btn btn-primary">'
											+'<i class="fa fa-edit"></i> Edit</button>'
											+'<button onclick="addNewChapter('
											+courseId+','
											+chapterId+','
											+(index+1)
											+')" type="button" class="btn btn-primary">'
											+'<i class="fa fa-plus"></i> Add</button>'
										+'</div>'
									+'</form>'
								);
								$("#para-menu").append(
									'<li><a href="#paragraph'
										+(index+1)
										+'">Chapter part-'
										+(index+1)
										+'</a></li>'
								);
										
							});
							$("#chapterReturn").on("click",function(){
								loadPage(items.chapters,currentPage,{'courseId':courseId});
							})
							if(count==-1){
								$('#chapter-content').append(
									'<form class="paragraph" id="paragraph1">'    
										+'<h1>Chapter part-1</h1>'
										+'<span class="chapterPartId" id="chapterPartId1" style="display:none;"></span>'
										+'<div class="row">'
											+'<div class="col-sm-6">'
												+'<ul class="nav nav-tabs">'
													+'<li class="active"><a href="#engText1" data-toggle="tab">English Text</a></li>'
													+'<li><a href="#chnText1" data-toggle="tab">Chinese Text</a></li>'
													+'<li><a href="#dutText1" data-toggle="tab">Dutch Text</a></li>'
												+'</ul>'
												+'<div class="tab-content">'
													+'<div class="tab-pane fade in active" id="engText1">'
														+'<div class="input-group">'
															+'<textarea class="form-control text_content"></textarea>'  
														+'</div>'
													+'</div>'
													+'<div class="tab-pane fade" id="chnText1">'
														+'<div class="input-group">'
															+'<textarea class="form-control text_content"></textarea>'  
														+'</div>'
													+'</div>'
													+'<div class="tab-pane fade" id="dutText1">'
														+'<div class="input-group">'
															+'<textarea class="form-control text_content"></textarea>'  
														+'</div>'
													+'</div>'
												+'</div>'
											+'</div>'
											+'<div class="col-sm-6">'
												+'<div class="input-group picBar">'
													+'<input type="file" class="form-control post-file" name="file" />'
													+'<input type="button" onclick="uploadImage(1,\'course\','+courseId+','+chapterId+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
													+'<span id="picId1" style="display:none;"></span>'
													+'<img class="imgBar" width="100%;">'
												+'</div>'
											+'</div>'
										+'</div><br>'
										+'<div class="row">'
											+'<div class="col-sm-6">'
												+'<div class="input-group">'
													+'<span class="input-group-addon">Course</span>'
													+'<select class="form-control" name="courseId"></select>'
												+'</div>'
											+'</div>'
										+'</div>'
										+'<div class="btn-bar">'
											+'<button onclick="addChapterPart('
											+courseId+","
											+chapterId+","+1
											+')" type="button" class="btn btn-success">'
											+'<i class="fa fa-check-square-o"></i> Submit</button>'							
										+'</div>'
									+'</form>'
								);
							}
						};

						communityBuilder.getChapterPartList(chapterId,"courses",done, fail);
					});
}

function addNewChapter(courseId,chapterId,index){
	$('#paragraph'+index).after(
			'<form class="paragraph" id="paragraph'+(index+1)+'">'    
			+'<h1>Chapter part-'+(index+1)+'</h1>'
			+'<span class="chapterPartId" id="chapterPartId'+(index+1)+'" style="display:none;"></span>'
			+'<div class="row">'
				+'<div class="col-sm-6">'
					+'<ul class="nav nav-tabs">'
						+'<li class="active"><a href="#engText'+(index+1)+'" data-toggle="tab">English Text</a></li>'
						+'<li><a href="#chnText'+(index+1)+'" data-toggle="tab">Chinese Text</a></li>'
						+'<li><a href="#dutText'+(index+1)+'" data-toggle="tab">Dutch Text</a></li>'
					+'</ul>'
					+'<div class="tab-content">'
						+'<div class="tab-pane fade in active" id="engText'+(index+1)+'">'
							+'<div class="input-group">'
								+'<textarea class="form-control text_content"></textarea>'  
							+'</div>'
						+'</div>'
						+'<div class="tab-pane fade" id="chnText'+(index+1)+'">'
							+'<div class="input-group">'
								+'<textarea class="form-control text_content"></textarea>'  
							+'</div>'
						+'</div>'
						+'<div class="tab-pane fade" id="dutText'+(index+1)+'">'
							+'<div class="input-group">'
								+'<textarea class="form-control text_content"></textarea>'  
							+'</div>'
						+'</div>'
					+'</div>'
				+'</div>'
				+'<div class="col-sm-6">'
					+'<div class="input-group picBar">'
						+'<input type="file" class="form-control post-file" name="file" />'
						+'<input type="button" onclick="uploadImage('+(index+1)+',\'course\','+courseId+','+chapterId+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
						+'<span id="picId'+(index+1)+'" style="display:none;"></span>'
						+'<img class="imgBar" width="100%;">'
					+'</div>'
				+'</div>'
			+'</div><br>'
			+'<div class="row">'
				+'<div class="col-sm-6">'
					+'<div class="input-group">'
						+'<span class="input-group-addon">Course</span>'
						+'<select class="form-control" name="courseId"></select>'
					+'</div>'
				+'</div>'
			+'</div>'
			+'<div class="btn-bar">'
				+'<button onclick="cancelAddChapterPart('
				+chapterId+","+(index+1)
				+')" type="button" class="btn btn-primary">'
				+'<i class="fa fa-reply"></i> Cancel</button>'
				+'<button onclick="addChapterPart('
				+courseId+","
				+chapterId+","+(index+1)
				+')" type="button" class="btn btn-success">'
				+'<i class="fa fa-check-square-o"></i> Submit</button>'
			+'</div>'
		+'</form>'
			);
	/*update the following chapter parts*/
	$('.paragraph').each(function(i,element){
		if(i>index){
			$(element).children("h1").text('Chapter part-'+(i+1));
			$(element).attr('id','paragraph'+(i+1));
			$(element).children(".row").children().children("ul").children("li").eq(0).children("a").attr('href','#engText'+(i+1));
			$(element).children(".row").children().children("ul").children("li").eq(1).children("a").attr('href','#chnText'+(i+1));
			$(element).children(".row").children().children("ul").children("li").eq(2).children("a").attr('href','#dutText'+(i+1));
			$(element).children(".row").children().children(".tab-content").children(".tab-pane").eq(0).attr('id','engText'+(i+1));
			$(element).children(".row").children().children(".tab-content").children(".tab-pane").eq(1).attr('id','chnText'+(i+1));
			$(element).children(".row").children().children(".tab-content").children(".tab-pane").eq(2).attr('id','dutText'+(i+1));

		}
	})
}
function cancelAddChapterPart(chapterId,index){
	/*update the following chapter parts*/
	$('.paragraph').each(function(i,element){
		if(i>index-1){
			$(element).children("h1").text('Chapter part-'+(i));
			$(element).attr('id','paragraph'+(i));
			$(element).children(".row").children().eq(0).children("ul").children("li").eq(0).children("a").attr('href','#engText'+(i));
			$(element).children(".row").children().eq(0).children("ul").children("li").eq(1).children("a").attr('href','#chnText'+(i));
			$(element).children(".row").children().eq(0).children("ul").children("li").eq(2).children("a").attr('href','#dutText'+(i));
			$(element).children(".row").children().eq(0).children(".tab-content").children(".tab-pane").eq(0).attr('id','engText'+(i));
			$(element).children(".row").children().eq(0).children(".tab-content").children(".tab-pane").eq(1).attr('id','chnText'+(i));
			$(element).children(".row").children().eq(0).children(".tab-content").children(".tab-pane").eq(2).attr('id','dutText'+(i));

		}
	})
	$('#paragraph'+index).remove();
}
function editChapterStyle(courseId,chapterId,chapterPartId,index,text,picture){	
	$('#paragraph'+index+' .text_content').removeAttr("readonly");
	$('#paragraph'+index+' .picBar').html(
			'<input type="file" class="form-control post-file" name="file" />'
			+'<input type="button" onclick="uploadImage('+(index)+',\'course\','+courseId+','+chapterId+')" value="UploadFile" class="btn btn-primary upload-btn"/>'	
			+'<span id="picId'+(index)+'" style="display:none;"></span>'
			+'<img class="imgBar" src="'
			+imgPath+picture+'" width="100%;"/>');
			
	$('#paragraph'+index+' .btn-bar').html(
			'<button onclick="noEditChapterStyle('
			+courseId+','
			+chapterId+','
			+chapterPartId+','
			+index+','
			+text+','
			+picture
			+')" type="button" class="btn btn-primary">'
			+'<i class="fa fa-reply"></i> Cancel</button>'
			+'<button onclick="changeChapterPart('
			+courseId+','
			+chapterId+','
			+chapterPartId+','
			+(index)
			+')" type="button" class="btn btn-success">'
			+'<i class="fa fa-check-square-o"></i> Submit</button>');
	
}
function noEditChapterStyle(courseId,chapterId,chapterPartId,index,text,picture){
	$('#paragraph'+index+' .text_content').attr("readonly","readonly");
	$('#paragraph'+index+' .picBar').html(
			//'<span class="input-group-addon">Picture</span>'
			'<img src="'
			+imgPath+picture
			+'" width="100%;"/>');
	$('#paragraph'+index+' .btn-bar').html(
			'<button onclick="editChapterStyle('
			+courseId+','
			+chapterId+','
			+chapterPartId+','
			+(index)+','
			+text+','
			+picture
			+')" type="button" class="btn btn-primary">'
			+'<i class="fa fa-edit"></i> Edit</button>'
			+'<button onclick="addNewChapter('
			+chapterId+','
			+chapterPartId+','
			+(index)
			+')" type="button" class="btn btn-primary">'
			+'<i class="fa fa-plus"></i> Add</button>');
}
function changeChapterPart(courseId,chapterId,chapterPartId,index){
	//var text = $('#paragraph'+index+' .text_content').val();
	var english_text=$('#paragraph'+index+' #engText'+index+' .text_content').val();
	var chinese_text=$('#paragraph'+index+' #chnText'+index+' .text_content').val();
	var dutch_text=$('#paragraph'+index+' #dutText'+index+' .text_content').val();
	var pictureId=$("#picId"+(index)).val();
	console.log(pictureId);
	var data="english_text="+english_text+"&chinese_text="+chinese_text+"&dutch_text="+dutch_text+"&pictureId="+pictureId;
	communityBuilder.changeChapterPart(chapterId,chapterPartId,data,"courses",
			function(data){		
				showEditChapterPart(courseId,chapterId);
			}, fail);
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

/*used to add chapter*/
function addChapter(courseId,chapterId){
	console.log(courseId+"+"+chapterId);
	/*put chapterId into an array*/
	var chapterIdList=[];
	var flag;
	$('#list tbody tr').each(function(index){
		var id=parseInt($(this).children('td:nth-child(2)').text());
		chapterIdList.push(id);
		if(id==parseInt(chapterId))
			flag=index;
	})
	var callback=function(){
		$('.return').on('click',function(){
			formReturnPage('chapters',{'courseId':courseId});
		})
		$(".add").on('click',function(){
			var data = $("form#add").serialize();	
			communityBuilder.addChapterStep1(courseId, data, "courses", function(data){		
				/*insert new chapter id to the chapterid array(at "flag" position)*/
				chapterIdList.splice(flag+1,0,data.id);
				chapterIdList=chapterIdList.join(",");
				var data2="lists="+chapterIdList;
				console.log(data2);
				communityBuilder.addChapterStep2(courseId, data2, "courses", function(data){
					loadPage(items.chapters,0,{
						'courseId' : courseId
					})
					console.log(data);
				}, fail)
			}, fail);		
		})
	};
	$(".main").load("pages/addChapter.html",callback);
}

/*used to add chapterPart*/
function addChapterPart(courseId,chapterId,flag){
	/*put chapterId into an array*/
	var chapterPartIdList=[];
	$('.paragraph .chapterPartId').each(function(index){
		var id=$(this).text();
		if(id!="")
			chapterPartIdList.push(parseInt(id));
	})
	//var text = $('#paragraph'+flag+' .text_content').val();
	var english_text=$('#paragraph'+flag+' #engText'+flag+' .text_content').val();
	var chinese_text=$('#paragraph'+flag+' #chnText'+flag+' .text_content').val();
	var dutch_text=$('#paragraph'+flag+' #dutText'+flag+' .text_content').val();

	var pictureId=$("#picId"+(flag)).val();
	 
	var data="english_text="+english_text+"&chinese_text="+chinese_text+"&dutch_text="+dutch_text+"&pictureId="+pictureId;
	communityBuilder.addChapterPartStep1(chapterId, data, "courses", function(data){		
		/*insert new chapter id to the chapterid array(at "flag" position)*/
		$("#chapterPartId"+flag).text(data.id);
		chapterPartIdList.splice(flag-1,0,data.id);
		console.log(chapterPartIdList);
		chapterPartIdList=chapterPartIdList.join(",");
		var data2="lists="+chapterPartIdList;
		communityBuilder.addChapterPartStep2(chapterId, data2, "courses", function(data){
			showEditChapterPart(courseId,chapterId);
			console.log(data);
		}, fail)
	}, fail);		
}
/*define the return buttons in list*/
function returnPage(pageType,props){
	console.log(props);
	console.log(pageType);
	if(pageType=="userMoments" || pageType=="userCourses")
		loadPage(items.users,prevPage);
	if(pageType=="userChapters" || pageType=="availableCourses"){
		loadPage(items.userCourses,0,{'userId': props.userId});
	}
	if(pageType=="chapters")
		loadPage(items.courses,currentPage);
	if(pageType=="members"){
		loadPage(items.classes,currentPage);
	}		
}
/*define the return buttons in form*/
function formReturnPage(pageType,props){
	console.log(props);
	console.log(pageType);
	if(pageType=="users")
		loadPage(items.users,currentPage);
	if(pageType=="userMoments")
		loadPage(items.userMoments,currentPage,{'userId': props.userId});
	if(pageType=="userCourses")
		loadPage(items.userCourses,currentPage,{'userId': props.userId});
	if(pageType=="userChapters")
		loadPage(items.userChapters,currentPage,{'userId': props.userId,'courseId': props.courseId});
	if(pageType=="courses")
		loadPage(items.courses,currentPage);
	if(pageType=="chapters")
		loadPage(items.chapters,currentPage,{'courseId': props.courseId});
	if(pageType=="classes")
		loadPage(items.classes,currentPage);
	if(pageType=="clubs")
		loadPage(items.clubs,currentPage);
}
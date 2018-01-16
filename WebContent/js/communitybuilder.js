function CommunityBuilder(token) {
	this.token = token;
};

CommunityBuilder.prototype.constructor = CommunityBuilder;

/**
 * Load a page with data from the server
 * 
 * @param resource
 *            The URL to the resource
 * @param page
 *            Page number
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.getPage = function(resource, page, done, fail) {
	$.ajax({
		type : "GET",
		url : 'api/' + resource + '?page=' + page,
		headers : {
			'auth-token' : this.token
		},
		error : fail,
		success : done
	});
}

/**
 * Load a single item from the server
 * 
 * @param id
 *            ID of the item
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.get = function(id, resource, done, fail) {
	$.ajax({
		type : "GET",
		url : "api/" + resource + "/" + id,
		headers : {
			'auth-token' : this.token
		},
		error : fail,
		success : done
	});
}

/**
 * Add a single item to the server
 * 
 * @param data
 *            list of data changes seperated by & for example:
 *            foo=hello&bar=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.add = function(data, resource, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/" + resource,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}


/**
 * Edit a single item on the server
 * 
 * @param id
 *            ID of the item
 * @param data
 *            list of data changes seperated by & for example:
 *            foo=hello&bar=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.edit = function(id, data, resource, done, fail) {
	$.ajax({
		type : "PUT",
		url : "api/" + resource + "/" + id + "?" + data,
		headers : {
			'auth-token' : this.token
		},
		error : fail,
		success : done
	});
}
CommunityBuilder.prototype.editUserLink = function(data, resource, done, fail) {
	$.ajax({
		type : "PUT",
		url : "api/" + resource + "?" + data,
		headers : {
			'auth-token' : this.token
		},
		error : fail,
		success : done
	});
}

/**
 * Delete a single item from the server
 * 
 * @param id
 *            ID of the item
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.del = function(id, resource, done, fail) {
	$.ajax({
		type : "DELETE",
		url : "api/" + resource + "/" + id,
		headers : {
			'auth-token' : this.token
		},
		error : fail,
		success : done
	});
}

/**
 * TODO: Make this work
 * 
 * Upload a picture to the server
 * 
 * @param data
 *            The image
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.uploadImage = function(data, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/pictures",
		contentType : 'multipart/form-data',
		processData : false,
		headers : {
			'auth-token' : this.token
		},
		data : data
	});
}
/**
 * get a moment item to the server
 * 
 * @param data
 *            list of data changes seperated by & for example:
 *            foo=hello&bar=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.getMomentPart = function(id, resource, done, fail) {
	$.ajax({
		type : "GET",
		url : "api/" + resource + "/" + id+ "/getMomentPartWeb",
		headers : {
			'auth-token' : this.token
		},
		error : fail,
		success : done
	});
}

/**
 * Add a single momentpart to the server
 * 
 * @param data
 *            list of data changes seperated by & for example:
 *            foo=hello&bar=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.addMomentPart = function(id, data, resource, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/" + resource+ "/" + id+ "/add"/*+"?" + data*/,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}

/**
 * Edit a single momentpart on the server
 * 
 * @param id
 *            ID of the item
 * @param data
 *            list of data changes seperated by & for example:
 *            foo=hello&bar=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.changeMomentPart = function(id, data, resource, done, fail) {
	$.ajax({
		type : "POST",
		url :"api/" + resource+ "/" + id+ "/change"/*+"?" + data*/,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}

/**
 * Upload a picture to the server
 * 
 * @param data
 *            list of data changes seperated by & for example:
 *            foo=hello&bar=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.uploadImage = function(data, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/pictures",
		headers : {
			'auth-token' : this.token
		},
		data : data,
		processData:false,
		contentType:false,
		error : fail,
		success : done
	});
}

/**
 * get a chapter from the server
 * 
 * @param data
 *            list of data changes seperated by & for example:
 *            foo=hello&bar=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.getChapterPartList = function(chapterId,resource, done, fail) {
	$.ajax({
		type : "GET",
		url : "api/" + resource + "/getChapterPartListWeb/" + chapterId,
		headers : {
			'auth-token' : this.token
		},
		error : fail,
		success : done
	});
}

/**
 * Add a chapter to the server
 * 
 * @param data
 *            list of data changes seperated by & for example:
 *            title=hello&requireOrNot=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.addChapterStep1 = function(id, data, resource, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/" + resource+ "/addChapterStep1/" + id,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}
CommunityBuilder.prototype.addChapterStep2 = function(id, data, resource, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/" + resource+ "/addChapterStep2/" + id,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}

/**
 * Edit a single chapterpart on the server
 * 
 * @param id
 *            ID of the item
 * @param data
 *            list of data changes seperated by & for example:
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.changeChapterPart = function(chapterId,chapterPartId, data, resource, done, fail) {
	$.ajax({
		type : "PUT",
		url :"api/" + resource+ "/chapters/"+chapterId+"/parts/"+chapterPartId+"?" + data,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}

/**
 * Add a chapter part to the server
 * 
 * @param data
 *            list of data changes seperated by & for example:
 *            title=hello&requireOrNot=world
 * @param resource
 *            The URL to the list of resources
 * @param done
 *            Function executed when request was successful
 * @param fail
 *            Function executed when request failed
 */
CommunityBuilder.prototype.addChapterPartStep1 = function(id, data, resource, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/" + resource+ "/addChapterPartStep1/" + id,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}
CommunityBuilder.prototype.addChapterPartStep2 = function(id, data, resource, done, fail) {
	$.ajax({
		type : "POST",
		url : "api/" + resource+ "/addChapterPartStep2/" + id,
		headers : {
			'auth-token' : this.token
		},
		data : data,
		error : fail,
		success : done
	});
}
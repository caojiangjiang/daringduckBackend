/**
 * This file contains all the resources for the system
 * All the pages can be generated from this data
 */

items = {
	clubs : {
		path : 'clubs',
		name : 'clubs',
		nameSingular : 'club',
		icon : 'map',
		menu : true,
		editable : true,
		deleteable : true,
		fields : [ {
			name : 'code',
			type : 'text',
		}, {
			name : 'name',
			type : 'text'
		}, {
			name : 'location',
			type : 'text'
		} ]
	},
	classes : {
		path : "classes",
		name : "classes",
		nameSingular : "class",
		icon : 'users',
		menu : true,
		editable : true,
		buttons : [ {
			color : 'success',
			action : "loadPage(items.classMembers, 0, {'classId': %id%})",
			icon : "users",
			text : "Members"
		} ],
		fields : [ {
			name : 'code',
			type : 'text'
		}, {
			name : 'name',
			type : 'text'
		}, {
			name : 'club',
			type : 'select',
			list : 'clubs',
			optionText : '%name% (%location%)'
		} ]
	},
	users : {
		path : "users",
		name : "users",
		nameSingular : "user",
		icon : 'user',
		menu : true,
		editable : true,
		buttons : [ {
			color : 'warning',
			action : "showChangePassword(%id%, '%username%')",
			icon : 'key',
			text : 'Password'
		}, {
			color : 'success',
			action : 'loadPage(items.userMoments, 0, {userId: %id%})',
			icon : 'area-chart',
			text : 'Moments'
		}, {
			color : 'primary',
			action : "loadPage(items.userCourses, 0, {'userId': %id%})",
			icon : "bars",
			text : "Courses"
		},{
			condition : 'disabled',
			wrong : {
				color : 'danger',
				action : "disableUser(%id%)",
				icon : "close",
				text : "Disable"
			},
			right : {
				color : 'success',
				action : "enableUser(%id%)",
				icon : "check",
				text : "Enable"
			}
		} ],
		fields : [ {
			name : 'role',
			type : 'select',
			list : 'roles',
			optionText : '%name%',
		}, {
			name : 'username',
			type : 'text'
		}, 
		{
			name : 'password',
			type : 'password',
			noedit : true
		}, {
			name : 'gender',
			type : 'select',
			options : [ {
				name : 'Male',
				value : 1
			}, {
				name : 'Female',
				value : 2
			} ]
		}, {
			name : 'nickname',
			type : 'text'
		}, {
			name : 'phone',
			type : 'text'
		}, {
			name : 'wechat',
			type : 'text'
		}, {
			name : 'email',
			type : 'text'
		}, {
			name : 'picture',
			type : 'image'
		}, {
			name : 'club',
			type : 'select',
			list : 'clubs',
			optionText : '%name% (%location%)',
			options : [ {
				name : '-- No Club --',
				value : -1
			} ]
		}]
	},
	courses : {
		path : "courses",
		name : "courses",
		nameSingular : "course",
		icon : 'graduation-cap',
		menu : true,
		editable : true,
		fields : [ {
			name : 'name',
			type : 'text'
		} ,{
			name : 'picture',
			type : 'image'
		}],
		buttons : [ {
			color : 'success',
			action : "loadPage(items.chapters, 0, {'courseId': %id%})",
			icon : "bars",
			text : "Chapters"
		} ]
	},
	chapters : {
		path : "courses/%courseId%/chapters",
		name : "chapters",
		nameSingular : "chapter",
		editable: true,
		fields : [ { 
			name : 'title',
			type : 'text'
		} ,{
			name : 'RequiredOrNot',
			type : 'select',
			list : 'RequiredOrNot',
			//optionText : '%name% (%location%)',
			options : [ {
				name : 'true',
				value : true
			},{
				name : 'false',
				value : false
			}  ]
		}],
		buttons : [  
		{
			color : 'success',
			action : "showEditChapterPart(%courseId%,%id%)",
			icon : "check",
			text : "Show"
		}
	]	
	},
	userMoments : {
		path : "users/%userId%/moments",
		name : "userMoments",
		nameSingular : "moment",
		editable : true,
		fields : [ {
			name : 'title',
			type : 'text'
		}, {
			name : 'privacy',
			type : 'select',
			options : [ {
				name : 'Public',
				value : 'PUBLIC'
			}, {
				name : 'Club',
				value : 'CLUB'
			}, {
				name : 'Friends',
				value : 'FRIENDS'
			}, {
				name : 'Private',
				value : 'PRIVATE'
			} ]
		} ],
		/*buttons : [ {
			condition : 'hidden',
			wrong : {
				color : 'danger',
				action : "hideMoment(%id%)",
				icon : "close",
				text : "Hide"
			},
			right : {
				color : 'success',
				action : "showEditMoment(%id%)",
				icon : "check",
				text : "Show"
			}
		} ]*/
		buttons : [  {
				color : 'danger',
				action : "hideMoment(%id%)",
				icon : "close",
				text : "Hide"
			},
			{
				color : 'success',
				action : "showEditMoment(%id%)",
				icon : "check",
				text : "Show"
			}
		]
	},
	classMembers : {
		path : "classes/%classId%/members",
		name : "members",
		nameSingular : "member",
		addFunction : 'loadPage(items.classAvailableMembers, 0, {classId: %classId%})',
		buttons : [ {
			color : 'danger',
			action : "removeMember(%classId%, '%id%')",
			icon : 'close',
			text : 'Remove'
		} ]
	},
	classAvailableMembers : {
		path : "classes/%classId%/availableMembers",
		name : "non-members",
		nameSingular : "non-member",
		addable : false,
		buttons : [ {
			color : 'success',
			action : "addMember(%classId%, '%id%')",
			icon : 'Check',
			text : 'Add'
		} ]
	}
}
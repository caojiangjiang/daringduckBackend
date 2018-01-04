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
		fields : [{
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
				value : 2
			}, {
				name : 'Female',
				value : 1
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
		language:true,
		menu : true,
		editable : true,
		fields : [ {
			name : 'english_name',
			type : 'text'
		} ,{
			name : 'chinese_name',
			type : 'text'
		} ,{
			name : 'dutch_name',
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
		//addable:false,
		fields : [ { 
			name : 'english_title',
			type : 'text'
		} ,{ 
			name : 'chinese_title',
			type : 'text'
		} ,{ 
			name : 'dutch_title',
			type : 'text'
		} ,{
			name : 'requiredOrNot',
			type : 'radio',
			//list : 'RequiredOrNot',
			//optionText : '%name% (%location%)',
			options : [ {
				name : 'yes',
				value : true
			},{
				name : 'no',
				value :false
			}  ]
		}],
		buttons : [  
		{
			color : 'success',
			action : "showEditChapterPart(%courseId%,%id%)",
			icon : "check",
			text : "Show"
		},{
			color : 'primary',
			action : "addChapter(%courseId%,%id%)",
			icon : "edit",
			text : "Add"
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
		buttons : [  {
				color : 'danger',
				action : "hideMoment(%id%)",
				icon : "close",
				text : "Hide"
			},
			{
				color : 'success',
				action : "showEditMoment(%id%,%userId%)",
				icon : "check",
				text : "Show"
			}
		]
	},
	userCourses : {
		path : "users/getUserCourse/%userId%",
		name : "userCourses",
		nameSingular : "courses",
		editable : true,
		addFunction : 'loadPage(items.userAvailableCourses, 0, {userId: %userId%})',
		fields : [ {
				name:'date',
				type:'date'
			},
			{
				name : 'teacherId',
				type : 'text'
			},{
				name : 'passOrNot',
				type : 'radio',
				options : [ {
					name : 'yes',
					value : true
				},{
					name : 'no',
					value :false
				}  ]
			}],
		buttons : [  
			{
				color : 'success',
				action : "loadPage(items.userChapters, 0, {'userId': %userId%,'courseId':%courseId%})",
				icon : "check",
				text : "Chapter"
			}
		]
	},
	userAvailableCourses: {
		path : "courses",
		name : "availableCourses",
		nameSingular : "availableCourses",
		addable : false,
		buttons : [ {
			color : 'success',
			action : "addUserCourse(%id%,%userId%)",
			icon : 'Check',
			text : 'Add'
		} ]
	},
	userChapters : {
		path : "users/getUserChapter/%userId%/%courseId%",
		name : "userChapters",
		nameSingular : "chapters",
		editable : true,
		addable:false,
		fields : [ {
			name:'date',
			type:'date'
		},
		{
			name : 'teacherId',
			type : 'text'
		},
		{
			name : 'score',
			type : 'radio',
			options : [ {
				name : '1',
				value :1
			},{
				name : '2',
				value :2
			} ,{
				name : '3',
				value :3
			} ,{
				name : '4',
				value :4
			} ,{
				name : '5',
				value :5
			}  ]
		},
		{
			name : 'passOrNot',
			type : 'radio',
			options : [ {
				name : 'yes',
				value : true
			},{
				name : 'no',
				value :false
			}  ]
		},
		{
			name : 'comment',
			type : 'text'
		}],
		buttons: [  
			{
				color : 'success',
				action : "",
				icon : "check",
				text : "comment"
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
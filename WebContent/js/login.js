function login() {
	var username = $('#username').val();
	var password = $('#password').val();

	$.ajax({
		url: "api/login?username=" + username + "&password="+ password,
		dataType: "text"
	})
	.done(function(token) {
		//Save the token to localStorage 
		localStorage.setItem("token", token);
		window.location.href = "index.html";
	})
	.fail(function() {
		$('#username').css({'color':'#e33','border-color':'#e33'});
		$('#password').css({'color':'#e33','border-color':'#e33'});
		$('span').css('color','#e33');
		$('input[type="submit"]').effect( "shake", {}, 300);
	});
}
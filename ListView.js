 // Use local storage & the document thing to determine if the user is LOGGED IN 
 // Logged In users will be able to view PUBLIC and PRIVATE POST of everyone.
 // Guest users will be able to view ONLY PUBLIC POST of everyone.
 document.addEventListener("DOMContentLoaded", function () {
	var userID = localStorage.getItem("userID");
	
	// XMLHttpRequest to ListViewServlet to get JSON array string of all posts. 
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "ListViewServlet", true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	xhr.onreadystatechange = function() {
		if (xhr.readyState === XMLHttpRequest.DONE) {
			if (xhr.status === 200) {
				// This should get the JSON Array as a string. 
				var response = xhr.responseText;
				
				var entryInfo = JSON.parse(response);
				
				// Need to parse JSON and have HTML of posts as a list pop up. 
				var count = 0;
				var entryContent = "";

				// For every entry in the JSON array, parse information and put into HTML. 
				entryInfo.forEach(function(entry) {
					// Making sure it is the correct info per post.
					console.log("entry id:" + entry.id)
					console.log("user:" + entry.user);
					console.log("image:" + entry.image);
					console.log("caption:" + entry.caption);
					console.log("time:" + entry.time);
					console.log("longititde:" + entry.longitude);
					console.log("latitude:" + entry.latitude);
					console.log("like:" + entry.likeCount);
					console.log("privacy:" + entry.privacy);
					
					// List view will have the USER of post, small preview of the post, and link to view post. 
					var entry = 
					'<div id="entry' + count + '"style:"display: inline-block;">' +
					'<p> User: ' + entry.user + '</p>' +
					'<img src="' + entry.image + '"' + '>' +
					
					// Hyperlink to ViewPost.html, sending entry ID for ViewPost.html.
					'<a href="ViewPost.html" onclick="sessionStorage.setItem("EntryID", ' + entry.id + ')"> View Post </a> </div>'; 
					
					entryContent = entryContent + entry;
				});
			
				// Fix element ID based on ListView.html
				var getContent = document.getElementById("content");
				// All entry posts will be displayed upon loading of ListView.html
				getContent.innerHTML = entryContent;	
			} else {
				console.error("Error in getting JSON array of posts");
			}
		}
	};
	// Sending userID to Listview Servlet.
	xhr.send(userID);	
 });

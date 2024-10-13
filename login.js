/**
 * 
 */
	//make the submit button have an onclick="validate()"
 function loginUser(){
			let url="http://localhost:8080/final/LoginServlet?user="
			+document.loginForm.loginUsername.value+"&pass="
			+document.loginForm.loginPassword.value;
			console.log("here1");
			fetch(url, {method:"POST"})
			.then(response=> response.text())
			.then((result) => {
				console.log("here");
				if (Number.isNaN(parseInt(result))){
					
					alert(result);
				} else {
					//result is the user id, store user id in local storage
					//change page to the logged in view
					
					sessionStorage.setItem("userID", result);
					window.location.assign("NewFile1.html");
				}
				
			
			
			}).catch(function(error){
			
					console.log("request failed", error)
			});
		}
 
  function showSignup(){
        document.getElementById("LOG IN CONTAINER").style.display = "none";
        document.getElementById("SIGN UP CONTAINER").style.display = "flex";
    }

    function showLogin(){
        document.getElementById("LOG IN CONTAINER").style.display = "flex";
        document.getElementById("SIGN UP CONTAINER").style.display = "none";
    }

  
    async function registerUser(){	
	var username = document.getElementById("signup-username").value;
	var pass = document.getElementById("signup-password").value;
    var confirmPass = document.getElementById("signup-password-confirm").value;
    
	if (username == ""){
		alert("Username field must not be empty.");
		return;
	}
	
	console.log(username);
    console.log(pass);
    console.log(confirmPass);
    
    if (pass == "" || confirmPass == ""){
		alert("Password or Confirm Password field must not be empty.");
		return;
	}
	
	if (confirmPass != pass){
		alert("Passwords do not match. Try again.");
		return;
	}
	
	// Convert JS info into JSON
	const userData = {
		user: username,
		pass: pass,
	};
	
	console.log(userData);
	
	const jsonData = JSON.stringify(userData);
	console.log(jsonData);
	
	// Took lines 38-70 from Kathy's Assignment 4
	try{
		const response = await fetch('/knhang_CSCI201_Assignment4/RegisterUserServlet', { // Have to change this line
	    method: 'POST',
	    headers: {
	      'Content-Type': 'application/json'
	    },
	    body: jsonData
        });
        
        const data = await response.json();
        console.log(data);
        if (data == "Username is taken."){
			alert("Username is already taken.");
			return;
		}
		else if(data == "User information is missing."){
			alert("User information is missing.");
			return;
		}
		else{
			// BREH HOW DO I USE COOKIES
        	localStorage.setItem("userID", data);
        	window.location.href = "home.html";
		}
    }
    catch (error){
		console.error('Error:', error);
	}
}

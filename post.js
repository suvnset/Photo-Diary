const image_input = document.querySelector("#imageUpload");
var uploaded_image = null; // Initialize as null

image_input.addEventListener("change", function(){
    uploaded_image = this.files[0]; // Save the File object
    const reader = new FileReader();
    reader.addEventListener("load", () => {
        document.querySelector("#display_image").style.backgroundImage = `url(${reader.result})`;
    });
    reader.readAsDataURL(uploaded_image);
});

const caption_input = document.querySelector("#caption");
const caption_display = document.querySelector("#display_caption");
const longitude_input = document.querySelector("#longitude");
const latitude_input = document.querySelector("#latitude");


caption_input.addEventListener("input", function() {
    if (this.value.length > 255) {
        this.value = this.value.slice(0, 255);
    }
    caption_display.textContent = this.value; // Update the text content of the display element
});

// Function to send data to server
function sendDataToServer() {
    if (!uploaded_image) {
        console.error("No image selected");
        return;
    }
	var id = sessionStorage.getItem("userID");
    
    const formData = new FormData();
    formData.append('userID', id); 
    formData.append('image', uploaded_image); 
    formData.append('caption', caption_input.value);
    formData.append('longitude', longitude_input.value);
    formData.append('latitude', latitude_input.value);
    
    var publicRadio = document.getElementById("public");
    var privateRadio = document.getElementById("private");
    var friendsRadio = document.getElementById("friends");

    // Check which radio button is checked
    if (publicRadio.checked)
    {
    	formData.append('privacy', "public");
	} 
	else if (privateRadio.checked) 
	{
    	formData.append('privacy', "private");
        console.log("Private option is selected");
    } 
    else if (friendsRadio.checked) 
    {
    	formData.append('privacy', "friends");
        console.log("Friends Only option is selected");
    }
    
    

    // Send the data to the server using AJAX
    const xhr = new XMLHttpRequest();
    xhr.open('POST', 'UploadServlet', true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
				const responseMessage = xhr.responseText;
	            const matches = responseMessage.match(/image ID: (\d+)/);
	            if (matches) {
	                const imageID = matches[1];
	                console.log('Image ID:', imageID);
        			sessionStorage.setItem("EntryID", imageID);
	                // Now you can use the image ID as needed
	            } else {
	                console.error('Failed to extract image ID from response');
	            }
                window.location.replace("http://localhost:8080/201_NewProject/ViewPost.html");
              
        }
    };
    xhr.send(formData);
}

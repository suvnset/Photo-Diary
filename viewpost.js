function displayImage() {
	var id = sessionStorage.getItem("EntryID");
    var url = '/201_NewProject/DisplayImageServlet?imageID=' + id;
    
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = 'blob'; 
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Success: Display the image
                var blob = xhr.response;
                var imageUrl = URL.createObjectURL(blob);
                document.getElementById('display_image').src = imageUrl;
        
            } else {
                // Error: Log status and display error message
                console.error("Error:", xhr.status);
                alert("Error fetching image");
            }
        }
    };
    xhr.send();
}

function editCaption(){
	var id = sessionStorage.getItem("EntryID");
    var caption = document.getElementById('caption').value;
    // Construct the URL with image ID and caption
    var url = '/201_NewProject/EditCaptionServlet?imageID=' + id + '&newCaption=' + encodeURIComponent(caption);
     var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                displayCaption();
            } else {
                console.log("Error:", xhr.status);
                var errorResponse = JSON.parse(xhr.responseText);
                alert(errorResponse); 
            }
        }
    };
    xhr.send();
}


function displayCaption() {
	var id = sessionStorage.getItem("EntryID");
    var url = '/201_NewProject/DisplayCaptionServlet?imageID=' + id;
     var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var caption = xhr.responseText;
                document.getElementById('display_caption').textContent = caption;
            } else {
                console.log("Error:", xhr.status);
                var errorResponse = JSON.parse(xhr.responseText);
                alert(errorResponse); 
            }
        }
    };
    xhr.send();
}

displayCaption();
displayImage();

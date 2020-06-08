//Problem is getting data from servlet when comments are empty

async function getDataFromServlet() {
    var numOfComments = document.getElementById("commentNumberTextBox").value;
    var dataLocation = "data?numComments=" + numOfComments;
    fetch(dataLocation).then(response => response.json()).then((data) => {
        console.log(data);
        const commentsElement = document.getElementById("data-container");

        //Clear comments section
        while(commentsElement.firstChild)
            commentsElement.removeChild(commentsElement.firstChild)

        //Populate comments section
        for(var i = 0; i < data.comments.length; i++){
            var node = document.createElement("LI")
            var textnode = document.createTextNode(data.comments[i]); 
            node.appendChild(textnode);
            commentsElement.appendChild(node);
        }
    });
}

function deleteDataFromServlet(){
    fetch('delete-data').then(response => response.text()).then((text) => {
        console.log(text);
        console.log("Delete data has finished running");
        getDataFromServlet();
    });
}

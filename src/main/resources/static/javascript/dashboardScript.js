function displayViewingModal() {
	var form = document.getElementById("viewing-modal");
	//document.querySelector("#viewing-modal").style.height = "600px";		
	//form.style.display = "block";
	//form.style.height = "auto";
	form.style.visibility = "visible";
	form.style.opacity = "1";

}
function closeSigninForm() {
	var form = document.getElementById("viewing-modal");
	form.style.opacity = "0";
	form.style.visibility = "hidden";
	//form.style.height = "0px";		
	//form.style.display = "none";
}

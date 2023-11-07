

// function to set a given theme/color-scheme
function setTheme(themeName) {
	localStorage.setItem('theme', themeName);
	document.documentElement.className = themeName;
}

// function to toggle between light and dark theme
function switchTheme() {
	const toggle = document.getElementById("theme");

	if (toggle.checked === false) {
		
		setTheme('light-mode');
	} else{
		
	setTheme('dark-mode');
	}
}

// Immediately invoked function to set the theme on initial load
(function() {
	const toggle = document.getElementById("theme");
	const all_theme_toggle = document.getElementsByClassName("theme__toggle");
	/*document.getElementsByClassName("theme__toggle").forEach((tog)=>{
		tog.checked = true;
	})*/
	if (localStorage.getItem('theme') === 'dark-mode') {
		toggle.checked = true;
		switchTheme();
		//setTheme('dark-mode');
	} else {
		toggle.checked = false;
		switchTheme();
		//setTheme('light-mode');
	}
})();



//var switchTheme = () => alert("hello");s
/*const currentTheme = window.localStorage.getItem("current-theme");
const themeToggle = document.getElementById("theme");
if(currentTheme == "dark-mode"){
	themeToggle.checked = true;
	//switchTheme();
}
function switchTheme() {
	var selectedTheme;

	const allDivs = document.getElementsByName("div");
	if (currentTheme == null || currentTheme == "") {
		document.body.classList.add("dark-mode");
		allDivs.forEach((val, index) => {
			val.classList.add("dark-mode");
			alert("val: "+val+" index: "+index);
		});
		selectedTheme = "dark-mode";
	}
	else {
		//selectedTheme = "light-mode";
		document.body.classList.remove("dark-mode");
		allDivs.forEach((val, index) => {
			val.classList.remove("dark-mode");
		});
		selectedTheme = "";
	}
	window.localStorage.setItem("current-theme", selectedTheme);



}*/
 //switchTheme();
 //window.localStorage.clear();

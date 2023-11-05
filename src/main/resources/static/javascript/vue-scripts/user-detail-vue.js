const authUserDetailUrl = "/user/detail"
const userConnectionsUrl = "/user/connections";
const userProjectsUrl = "/user/projects";
//userAppElementExists = document.getElementById('user-app-element');
userAppElementExists = document.getElementById('user-details');

isOwnAccount = ownAccount;
profileUsername = profileUsernameValue

const userDetail = Vue.createApp({
	data() {
		return {
			isOwnAccount: ownAccount,
			authUserId: null,
			authUsername: null,
			authFirstName: null,
			authLastName: null,
			authEmail: null,
			authOnlineStatus: null,
			authRole: null,
			authEnabled: null,
			authProfilePicture: null,

			userProjectList: [],
			userProjectCount: 0,
			userConnectionsList: [],
			userConnectsCount: 0,

		}
	},
	created() {
		//alert(profileUsernameValue+" "+ownAccount)
		//alert(isOwnAccount)
		this.getUserDetail(profileUsername);
		this.getUserProjects(profileUsername);
		this.getUserConnections(profileUsername);
		//alert(this.authUsername)

	},
	updated() {

	},
	methods: {

		getUserDetail(profileUsername) {
			let userDetailurl = null;
			if (isOwnAccount == 'true') {
				userDetailurl = authUserDetailUrl
			}
			else {
				userDetailurl = authUserDetailUrl + "/" + profileUsername
			}
			fetch(userDetailurl)
				.then(response => response.json())
				.then(data => {

					this.authUserId = data.userId
					this.authUsername = data.username
					this.authFirstName = data.firstName
					this.authLastName = data.lastName
					this.authOnlineStatus = data.onlineStatus
					if (data.profilePicture == null) {
						//alert(data.firstName[0])
						this.authProfilePicture = data.firstName[0].toUpperCase();
					} else {
						this.authProfilePicture = data.profilePicture
					}

					if (isOwnAccount == 'true') {
						this.authEmail = data.email
						this.authRole = data.authRole
						this.authEnabled = data.Enabled


					}
					//alert("new sjo>>"+isOwnAccount)
					//alert(data.username)
				})

		},
		getUserProjects(profileUsername) {
			let userProjecturl = null;
			userProjecturl = userProjectsUrl + "/" + profileUsername
			fetch(userProjecturl)
				.then(response => response.json())
				.then(data => {
					this.userProjectList = data;
					this.userProjectCount = this.userProjectList.length;

				})
		},
		getUserConnections(profileUsername) {
			let userDetailurl = null;
			userConnection = userConnectionsUrl + "/" + profileUsername

			fetch(userConnection)
				.then(response => response.json())
				.then(data => {
					this.userConnectionsList = data
					this.userConnectionsList.forEach(conn => {
						if (conn.profilePicture == null) {
							//alert(data.firstName[0])
							conn.profilePicture = conn.connectedUserFirstName[0].toUpperCase();
						} /*else {
							const img = createElement("img");
							img.setAttribute("img", "conn.profilePicture")
							conn.profilePicture = document.append(img);
							conn.profilePicture = conn.profilePicture;
						}*/
						
					}
					
					)



					this.userConnectsCount = this.userConnectionsList.length;
				})
		},
		connectWithUser(authUserId) {
			alert("conn " + authUserId)
		},
	},

})
if (userAppElementExists)
	userDetail.mount('#user-details');



function login() {
    const contact = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ contact, password })
    })
        .then(response => response.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem('jwt', data.token);
                Toastify({
                    text: "You are logged successfully!",
                    duration: 3000,
                    close: true,
                    gravity: "top",
                    position: "right",
                    backgroundColor: "#16a34a",
                    stopOnFocus: true,
                }).showToast();

                setTimeout(() => {
                    window.location.href = '/home.html';
                }, 1000);
            } else {
                Toastify({
                    text: "Invalid login credentials",
                    duration: 3000,
                    close: true,
                    gravity: "top",
                    position: "right",
                    backgroundColor: "#dc2626",
                    stopOnFocus: true,
                }).showToast();


            }
        })
        .catch(error => {
            console.error("Error during login:", error);
            Toastify({
                text: "Error during login",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#dc2626",
                stopOnFocus: true,
            }).showToast();
        });
}

function register() {
    const name = document.getElementById("fullName").value.trim();
    const contact = document.getElementById("username").value.trim();
    const email = document.getElementById("email").value.trim();
    const gender = document.getElementById("gender").value;
    const dateOfBirth = document.getElementById("dob").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (password !== confirmPassword) {
        Toastify({
            text: "Passwords do not match!",
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: "#dc2626",
            stopOnFocus: true,
        }).showToast();
        return;
    }

    const userData = {
        name,
        contact,
        email,
        gender,
        dateOfBirth,
        password,
    };

    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(err => { throw new Error(err); });
            }
            return response.text();
        })
        .then(data => {
            Toastify({
                text: "Registration successful!!",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#16a34a",
                stopOnFocus: true,
            }).showToast();
            setTimeout(() => {
                window.location.href = '/';
            },1000);
        })
        .catch(error => {
            console.error("Error during registration:", error);
            // alert("Registration failed: " + error.message);
            Toastify({
                text: error.message,
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#dc2626",
                stopOnFocus: true,
            }).showToast();
        });
}

function checkAuth() {
    const token = localStorage.getItem('jwt');

    if (!token) {
        Toastify({
            text: "You aren't logged in",
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: "#dc2626",
            stopOnFocus: true,
        }).showToast();
        setTimeout(() => {
            window.location.href = '/login';
        }, 1000);
    } else {
        fetch('/api', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                console.log(data); // Do something with the protected data
            })
            .catch(error => {
                console.error("Error fetching protected data:", error);
            });
    }
}

function logout() {
    const token = localStorage.getItem('jwt');
    if (!token) {
        window.location.href = '/';
        return;
    }

    fetch('/signout', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Logout failed");
            }
            Toastify({
                text: "Logged out successfully!",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#16a34a",
                stopOnFocus: true,
            }).showToast();
            return response.text();
        })
        .then(data => {
            console.log("Logged out:", data);
        })
        .catch(error => {
            console.error("Error during logout:", error);
        })
        .finally(() => {
            localStorage.removeItem('jwt');
            window.location.href = '/';
        });
}


function loadHomeData() {
    const token = localStorage.getItem('jwt');
    if (!token) {
        Toastify({
            text: "You are not logged in",
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: "#dc2626",
            stopOnFocus: true,
        }).showToast();
        setTimeout(() => {
            window.location.href = '/login';
        },1000);
        return;
    }

    fetch('/home', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => {
            if (response.status === 403 || response.status === 401) {
                throw new Error("Unauthorized");
            }
            return response.text();
        })
        .then(html => {
            document.getElementById('home-content').innerHTML = html;
        })
        .catch(error => {
            console.error("Error loading home content:", error);
            Toastify({
                text: "Failed to load home content. Redirecting to login page...",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#dc2626",
                stopOnFocus: true,
            }).showToast();
            setTimeout(() => {
                window.location.href = '/login';
            },1000);
        });
}


function loadHomeDataTwo() {
    const token = localStorage.getItem('jwt');
    if (!token) {
        Toastify({
            text: "You are not logged in",
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: "#dc2626",
            stopOnFocus: true,
        }).showToast();
        setTimeout(() => {
            window.location.href = '/login';
        }, 1000);
    }

    const decodedToken = parseJwt(token);
    if (!decodedToken || decodedToken.exp<Math.floor(Date.now() / 1000)) {
        localStorage.removeItem('jwt');
        Toastify({
            text: "You are logged out by our system.Please login again.",
            duration: 3000,
            close: true,
            gravity: "top",
            position: "right",
            backgroundColor: "#dc2626",
            stopOnFocus: true,
        }).showToast();
        setTimeout(() => {
            window.location.href = '/login';
        }, 1000);
    }


    fetch('/fetch/user', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
        .then(res => res.json())
        .then(data => {
            const profileIconName = document.getElementById("first-name").innerHTML =data.name;
            const profileIcon = document.getElementById("profile-pic-view").src =data.profilePictureUrl || "/images/logo.jpg";
            const name = document.getElementById("name-heading").innerHTML =
                `<h2>Welcome Back, ${data.name}</h2>`;
        })
        .catch(() => {
            Toastify({
                text: "User Name Failed Loading!",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#dc2626",
                stopOnFocus: true,
            }).showToast();
        });







    fetch('/home', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => {
            if (response.status === 403 || response.status === 401) {
                throw new Error("Unauthorized");
            }
            return response.text();
        })
        // .then(html => {
        //     document.getElementById('home-content').innerHTML = html;
        // })
        .catch(error => {
            console.error("Error loading home content:", error);
            Toastify({
                text: "Failed to load home content. Please login again...",
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#dc2626",
                stopOnFocus: true,
            }).showToast();
        });



}
function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
        atob(base64)
            .split('')
            .map(c => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
            .join('')
    );
    return JSON.parse(jsonPayload);
}


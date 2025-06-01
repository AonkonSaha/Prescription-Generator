async function loadProfileData() {
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



    try {
        const response = await fetch('/fetch/user', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' +token
            }
        });

        if (!response.ok) {
            throw new Error('Failed to load profile data');
        }

        const data = await response.json();

        const name = document.getElementById("first-name").innerHTML =data.name || "User";
        const profilePic = document.getElementById("profile-pic-view").src =data.profilePictureUrl || "/images/logo.jpg";
        document.getElementById('fullName').value = data.name || '';
        document.getElementById('email').value = data.email || '';
        document.getElementById('dob').value = data.dateOfBirth || '';
        document.getElementById('gender').value = data.gender || '';

        if (data.profilePictureUrl) {
            document.getElementById('profilePreview').src = data.profilePictureUrl || "/images/logo.jpg";
        }

    } catch (error) {
        if (!token) {
            Toastify({
                text: "Error loading profile: "+ error,
                duration: 3000,
                close: true,
                gravity: "top",
                position: "right",
                backgroundColor: "#dc2626",
                stopOnFocus: true,
            }).showToast();
    }
}}

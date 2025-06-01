function doctorLoadAndRecentAppointments() {
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
        return;
    }

    fetch('/api/doctor/fetch/all', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
        .then(response => {
            if (response.status === 403 || response.status === 401) {
                throw new Error("Unauthorized");
            }
            return response.json();
        })
        .then(data => {
            const container = document.querySelector('.doctor-cards');
            container.innerHTML = ""; // Clear existing hardcoded cards

            data.doctors.forEach(doctor => {
                // alert(doctor.profilePictureUrl);
                const card = `
                <div class="card">
                
                    <img src="${doctor.profilePictureUrl}" alt="${doctor.doctorName}">
                    <h3>${doctor.doctorName}</h3>
                    <p>${doctor.degreesString}</p>
                    <div class="specialty">${doctor.designation}</div>
                    <button onclick="window.location.href='/appointments/doctor/book/${doctor.id}'">Book Appointment</button>
                </div>`;
                container.innerHTML += card;
            });
        })
        .catch(error => {
            console.error("Error loading home content:", error);
            Toastify({
                text: "Failed to load home content. Please login again.",
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
        });
}
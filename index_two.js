document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("patientRegistrationForm");

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        // Get input values
        const firstName = document.getElementById("username").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        // Create JSON object
        const patientData = {
            username,
            email,
            password
        };

        // Send JSON to Java backend
        fetch("http://localhost:8080/api/patients/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(patientData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Something went wrong");
                }
                return response.json();
            })
            .then(data => {
                alert("Registration successful!");
                console.log("Server response:", data);
                form.reset(); // Clear form
            })
            .catch(error => {
                alert("Error occurred: " + error.message);
                console.error("Error:", error);
            });
    });
});

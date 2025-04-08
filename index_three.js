document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("doctorRegistrationForm");

    form.addEventListener("submit", function (e) {
        e.preventDefault();

        // Get input values
        const firstName = document.getElementById("firstName").value;
        const lastName = document.getElementById("lastName").value;
        const email = document.getElementById("email").value;
        const phone = document.getElementById("phone").value;
        const address = document.getElementById("address").value;
        const specialization = document.getElementById("specialization").value;
        const password = document.getElementById("password").value;

        // Create JSON object
        const doctorData = {
            firstName,
            lastName,
            email,
            phone,
            address,
            specialization,
            password
        };

        // Send JSON to Java backend
        fetch("http://localhost:8080/api/patients/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(doctorData)
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

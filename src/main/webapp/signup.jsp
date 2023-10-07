<!DOCTYPE html>
<html lang="en">

<head>

    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign up</title>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="sign.css">
</head>

<body>
    <div class="main-container">
        <div class="signup-tab form-container">
            <form class="signup-form" method="post">
                <h1>Sign up</h1>

                <p>Please fill in this form to create an account.</p>
                <hr>

                <div class="form-group">
                    <label for="name"><b>Enter name</b></label>
                    <input type="text" name="name" id="name" required>
                </div>

                <div class="form-group">
                    <label for="email"><b>Enter email</b></label>
                    <input type="email" name="email" id="email" required>
                </div>

                <div class="form-group">
                    <label for="password"><b> Enter Password</b></label>
                    <input type="password" name="password" id="password" required>
                </div>

                <div class="form-group">
                    <label for="confirm-password"><b> Confirm Password</b></label>
                    <input type="password" name="confirm-password" id="confirm-password" required>
                </div>

                <div class="form-group btn-group">
                    <button type="submit">
                        Sign Up
                    </button>
                </div>

                <div class="form-group">
                    <p>Already registered ?<a class="sign-span" href="login.jsp">Log in</a></p>
                </div>
            </form>
        </div>
    </div>
</body>
<script>
    "use strict"

    const form = document.querySelector(".signup-form");
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        const data = new FormData(form);
        if (data.get("password") === data.get("confirm-password")) {
            data.delete("confirm-password");
            const postString = new URLSearchParams(data).toString();

            const request = new XMLHttpRequest();
            request.onload = () => {
                if (request.status == 200) {
                    const response = JSON.parse(request.responseText);
                    if (response.success) {
                        alert("Account Created. You may login now");
                    }
                    if (response.alreadyExist) {
                        alert("Email already registered.");
                    }
                    if (!response.name) {
                        alert("Enter a valid name");
                    }
                    if (!response.email) {
                        alert("Enter a valid email");
                    }
                    if (!response.password) {
                        alert("Enter a valid password");
                    }
                } else if (request.status == 500) {
                    alert("Internal Server Error");
                } else {
                    alert("Error: " + request.status);
                }
            }
            request.open("POST", "createAccount");
            request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            request.send(postString);
        }
        else {
            form.querySelector("#confirm-password").setCustomValidity("Passwords do not match");
        }
    })
</script>

</html>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log in</title>
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="sign.css">
</head>

<body>
    <div class="main-container">
        <div class="login-tab form-container">
            <form action="login" class="login-form" method="post">
                <h1>Log in</h1>
                <hr>
                <div class="form-group">
                    <label for="email"><b>Email</b></label>
                    <input type="email" placeholder="Enter Email" name="email" required>
                </div>

                <div class="form-group">
                    <label for="password"><b>Password</b></label>
                    <input type="password" placeholder="Enter Password" name="password" required>
                </div>
                <div class="form-group">
                    <button type="submit">Login</button>
                </div>
                <div class="form-group">
                    <p>New user ?
                        <a href="signup.jsp" class="sign-span">Create a Account</a>
                    </p>
                </div>
            </form>
        </div>
    </div>
</body>
<script>
    "use strict"
    const form = document.querySelector(".login-form");
    form.addEventListener("submit", (e) => {
        e.preventDefault();

        const data = new FormData(form);
        const postString = new URLSearchParams(data);
        const request = new XMLHttpRequest();

        request.onload = () => {
            if (request.status == 200) {
                const response = JSON.parse(request.responseText);
                console.log(response);
                if (response.success) {
                    window.location.href = response["redirectUrl"];
                }
                if (response.accountDoesNotExist === true) {
                    alert("Account Does not exist. Please Enter a registered Email");
                }
                if (response.wrongPassword === true) {
                    alert("Wrong Password");
                }
            } else {
                alert("Error " + request.status + " " + request.statusText);
            }
        }
        request.open("POST", "login");
        request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        request.send(postString);
    })
</script>

</html>
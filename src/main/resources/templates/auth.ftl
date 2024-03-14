<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>Signin Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="/static/css/style.css">
</head>

<body class="text-center">
<form class="form-signin" onsubmit="submitHandler(); return false;">
    <a class="d-inline-flex mb-12 justify-content-center w-100 rst-btn mb-5" href="/">
        <img src="static/img/logo.png" height="52" alt="logo">
    </a>
    <img class="mb-4" src="https://getbootstrap.com/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
    <label for="inputEmail" class="sr-only">Email address</label>
    <input type="email" name="email" id="inputEmail" class="form-control" placeholder="Email address" required
           autofocus>
    <label for="inputName" class="sr-only">Name</label>
    <input type="text" name="name" id="inputName" class="form-control" placeholder="Name" required>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required>
    <div class="submit-wrapper">
        <input class="btn btn-lg btn-primary btn-block" id="btn-submit" value="Sign In" type="submit"/>
        <p class="msg-error"></p>
    </div>
    <p class="mt-5 mb-3 text-muted">&copy; Tinder 2018</p>
</form>
<script>
    async function submitHandler() {
        const email = document.querySelector("#inputEmail")
        const name = document.querySelector("#inputName")
        const password = document.querySelector("#inputPassword")

        fetch(`/login-form?email=${email.value}&name=${name.value}&password=${password.value}`, {
            method: 'POST',
        }).then(res => {
            const msgText = res.headers.get("message")

            if (msgText) {
                const errorText = document.querySelector(".msg-error")
                errorText.textContent = msgText
                email.classList.add("error-border")
            } else {
                window.location.href = "/users"
            }
        })
    }
</script>
</body>
</html>
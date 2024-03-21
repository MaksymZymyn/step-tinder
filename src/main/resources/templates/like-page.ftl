<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Like page</title>
    <!-- Bootstrap core CSS -->
    <link href="/static/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome CSS -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="/static/css/style.css">
</head>
<body style="background-color: #f5f5f5;">

<div class="container">
    <div class="row">
            <div class="col-md-4 mb-3">
                <div class="card">
                    <img src="${picture}" class="card-img-top" alt="${fullName}">
                    <div class="card-body">
                        <h1 class="card-title">${fullName}</h1>
                        <form action="/users" method="post">
                            <input type="hidden" name="id" value="${id}">
                            <button href="/users" type="submit" name="action" value="like" class="btn btn-outline-success"><span class="fa fa-heart"></span> Like</button>
                            <button href="/users" type="submit" name="action" value="dislike" class="btn btn-outline-danger"><span class="fa fa-times"></span> Dislike</button>
                        </form>
                    </div>
                </div>
            </div>
    </div>
</div>
</body>

</html>

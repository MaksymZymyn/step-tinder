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

<div class="col-4 offset-4">
    <div class="card">
        <div class="card-body">
            <div class="link-page-l">
                <a class="link_regLogin-l" href="/liked"><i class="fas fa-heart"></i></a>
            </div>
            <div class="row">
                <div class="col-12 col-lg-12 col-md-12 text-center">
                    <img src="${picture}" alt="" class="mx-auto rounded-circle img-fluid">
                    <h3 class="mb-0 text-truncated">${fullName}</h3>
                    <br>
                </div>
                <div class="col-12 col-lg-6">
                    <form action="/users" method="post">
                        <button name="action" value="dislike" type="submit" class="btn btn-outline-danger btn-block">
                            <span class="fa fa-times"></span> Dislike
                        </button>
                        <button name="action" value="like" type="submit" class="btn btn-outline-success btn-block">
                            <span class="fa fa-heart"></span> Like
                        </button>
                    </form>
                </div>
                <!--/col-->
            </div>
            <!--/row-->
        </div>
        <!--/card-block-->
    </div>
</div>

</body>

</html>

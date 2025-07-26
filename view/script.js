const url = "http://localhost:8080/task/user/4"

function hideLoader(){
    document.getElementById("loading").style.display= "none";
}

function show(tasks){
    let tab = `<thead>
        <th scope="col">#</th>
        <th scope="col">Descripition</th>
        <th scope="col">User</th>
        <th scope="col">User id</th>
    </thead>`;

    for(let task of tasks){
        tab+= `
            <tr>
                <td scope="row">${task.id}</td>
                <td scope="row">${task.description}</td>
                <td scope="row">${task.user.username}</td>
                <td scope="row">${task.user.id}</td>
            </tr>
        `
    }

    document.getElementById("tasks").innerHTML = tab;
}

async function getAPI(url) {
   const response = await fetch(url, { method: "GET" });

    var data = await response.json();
    console.log(data)
    if(response){
        hideLoader();
        show(data);
    }
}

getAPI(url);
window.onload = () => {

    document.getElementById("editEmplInfo").addEventListener("click", () => {
        let lastName = document.getElementById("lastName").value;
        let firstName = document.getElementById("firstName").value;
        let email = document.getElementById("email").value;

        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                console.log(data);

                if (data.message === "Edit success") {
                  sessionStorage.setItem('employeeLastName', lastName);
                  sessionStorage.setItem('employeeFirstName', firstName);
                  sessionStorage.setItem('employeeEmail', email);

                  document.getElementById("editInfoMessage").innerHTML = '<span class="label label-success label-center">Succeded</span>';
                }
                else {
                  document.getElementById("editInfoMessage").innerHTML = '<span class="label label-danger label-center">Failed</span>';
                }
            }
        };

        xhr.open("POST",`editInfo.do?lastname=${lastName}&firstname=${firstName}&email=${email}`);

        xhr.send();
    });
}

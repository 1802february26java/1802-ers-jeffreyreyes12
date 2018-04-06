window.onload = () => {

  document.getElementById("submit").addEventListener("click", () => {
    let type = document.getElementById("type").value;
    let amount = document.getElementById("amount").value;
    let description = document.getElementById("description").value;

    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
      if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
        let data = JSON.parse(xhr.responseText);
        console.log(data);

        document.getElementById("status").innerHTML = data.message;
      }
    };

    xhr.open("POST",`submitReimbursement.do?type=${type}&amount=${amount}&description=${description}`);

    xhr.send();
  });
}

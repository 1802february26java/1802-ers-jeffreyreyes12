window.onload = () => {
  document.getElementById('username').innerHTML = sessionStorage.getItem("employeeUsername");
  
  document.getElementById('pending').addEventListener('click', fetchReimbursements('pending'));
  document.getElementById('resolved').addEventListener('click', fetchReimbursements('resolved'));
}

function populateTable(reimbursements) {
  let tbody = document.getElementById('tableBody');

  tbody.innerHTML = '';

  let i = 0;

  for (; i < reimbursements.length; i++) {
	   tbody.appendChild(createRow(reimbursements[i]));
   }
}

function createRow(reimbursement) {
  let tr = document.createElement('tr');

	let id = document.createElement('td');
	id.innerHTML = reimbursement.id;

	let name = document.createElement('td');
	name.innerHTML = reimbursement.requester.firstName + ' ' + reimbursement.requester.lastName;

	let amount = document.createElement('td');
	amount.innerHTML = reimbursement.amount;

	let description = document.createElement('td');
	description.innerHTML = reimbursement.description;

	let type = document.createElement('td');
	type.innerHTML = reimbursement.type.type;

	let status = document.createElement('td');
	status.innerHTML = reimbursement.status.status;

	tr.appendChild(id);
	tr.appendChild(name);
	tr.appendChild(amount);
	tr.appendChild(description);
	tr.appendChild(type);
	tr.appendChild(status);

	return tr;
}

function fetchReimbursements(which) {
  let xhr = new XMLHttpRequest();

  xhr.onreadystatechange = () => {
    if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
      let data = JSON.parse(xhr.responseText);
      console.log(data);

      populateTable(data);
    }
  };

  return () => {
    xhr.open("GET", `allReimbursements.do?fetch=${which}`);
    xhr.send();
  };
}

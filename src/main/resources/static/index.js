let baseUrl = () => {
    let pathArray = window.location.href.split( '/' );
    let protocol = pathArray[0];
    let host = pathArray[2];
    return protocol + '//' + host;
}

const loadAccountDetails = () => {
     axios.get(baseUrl() + '/api/account/balance')
     .then(response => {
          console.log(response.data);
          setAccountElements(response.data);
    })
     .catch(error => {
        showErrorMessage(error);
     });
}

const setAccountElements = (data) => {
    let accountNumber = document.getElementById("account-number");
    accountNumber.innerText = data.accountNumber;

    let accountBalance = document.getElementById("account-balance");
    accountBalance.innerText = data.balance;

    let accountOverdraft = document.getElementById("account-overdraft");
    accountOverdraft.innerText = data.overdraft;
}

const loadAvailableBills = () => {
     axios.get(baseUrl() + '/api/getAvailableBills')
     .then(response => {
          console.log(response.data);
          setBillElements(response.data);
    })
     .catch(error => {
        showErrorMessage(error);
     });
}

const showErrorMessage = (error) => {
    let errorMessage = error.response.data;
    console.error(errorMessage);
    let errorMessageDiv = document.getElementById("error-message");
    errorMessageDiv.innerText = errorMessage;
}

const checkIfKeyEnterWithdraw = (event) => {
    if (event.keyCode == 13) {
        withdraw();
    }
}

const setBillElements = (data) => {
    let fifty = document.getElementById("fifty-total-atm");
    fifty.innerText = data[0].quantity;

    let twenty = document.getElementById("twenty-total-atm");
    twenty.innerText = data[1].quantity;

    let ten = document.getElementById("ten-total-atm");
    ten.innerText = data[2].quantity;

    let five = document.getElementById("five-total-atm");
    five.innerText = data[3].quantity;

    let sum = 0;
    for (let i = 0; i < data.length; i++) {
        sum += data[i].quantity * data[i].billType;
    }
    let total = document.getElementById("total-bills-atm");
    total.innerText = sum;
}

const withdraw = () => {
    let amount = document.querySelector("#amount").valueAsNumber;
    let overdraft = document.querySelector("#overdraft").checked;
     axios.get(baseUrl() + '/api/account/withdraw?amount=' + amount + "&useOverdraft=" + overdraft)
     .then(response => {
          console.log(response.data);
          localStorage.setItem('lastWithdraw', JSON.stringify(response.data));
          location.reload(true);
    })
     .catch(error => {
        showErrorMessage(error);
     });
}

const setWithdrawElements = (lastWithdraw) => {
    let fifty = document.getElementById("fifty-to-dispense");
    fifty.innerText = lastWithdraw[0].quantity;

    let twenty = document.getElementById("twenty-to-dispense");
    twenty.innerText = lastWithdraw[1].quantity;

    let ten = document.getElementById("ten-to-dispense");
    ten.innerText = lastWithdraw[2].quantity;

    let five = document.getElementById("five-to-dispense");
    five.innerText = lastWithdraw[3].quantity;

    let sum = 0;
    for (let i = 0; i < lastWithdraw.length; i++) {
        sum += lastWithdraw[i].quantity * lastWithdraw[i].billType;
    }
    let total = document.getElementById("total-bills-dispense");
    total.innerText = sum;
}



const logout = () => {
     axios.get(baseUrl() + '/logout')
     .then(response => {
      console.log(`logged out`);
       window.location.replace(baseUrl() + "/login");
    })
     .catch(error => {
        showErrorMessage(error);
     });

};

const h2console = () => {
    window.open(baseUrl() + "/h2-console", '_blank');
}

const swagger = () => {
    window.open(baseUrl() + "/swagger-ui.html", '_blank');
}

document.onreadystatechange = function(){
     if(document.readyState === 'complete'){
         loadAccountDetails();
         loadAvailableBills();
         let lastWithdraw = localStorage.getItem('lastWithdraw');
         if (!lastWithdraw.includes("DOCTYPE")) {
            setWithdrawElements(JSON.parse(lastWithdraw));
         }
     }
 }

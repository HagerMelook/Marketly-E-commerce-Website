function VendorBasicSignUp(businessname, password, taxnumber) {
    fetch('http://localhost:8080/SignUp/VendorBasicSignUp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            businessname: businessname,
            password: password,
            taxnumber: taxnumber,
        }),
    })
        .then(response => response.text())
        .then(data => {
            console.log("Response from the backend:", data);
        })
}

export default VendorBasicSignUp;